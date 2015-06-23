package com.example.client;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class ConnectServer extends Activity implements RecognitionListener {

	SpeechRecognizer speechRecognizer = null;
	RecognitionListener rListener = null;
	static int checkUpdate = 0;
	static String resultGesture = "";
	Intent intent = null;
    ImageButton speak = null;
    TextView result = null;
    String ip = null;
    MyClientTask connectServer = null;
    Context context = null;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.speak);
		Log.d("ConnectServer", "In Connect Server");
		intent = getIntent();
		context = this;
		speak = (ImageButton)findViewById(R.id.speak);
		result = (TextView)findViewById(R.id.textResult);
		Log.d("ConnectServer", "Adding Speech Recognizer");
		speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
		rListener = this;
		Log.d("ConnectServer", "After Speech Recognizer");
		speechRecognizer.setRecognitionListener(rListener);
		Log.d("ConnectServer", "Added recognition listener");
		speak.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				Log.d("TouchListener", "In Touch Event");
				speechRecognizer.startListening(intent);
				Log.d("TouchListener", "After Touch Event");
				return false;
			}
		});
		speak.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				speechRecognizer.stopListening();
				
			}
			
		});

		ip = intent.getStringExtra("ip");
		connectServer = new MyClientTask(ip, 1234);
		Log.d("Server IP", ip);
		connectServer.execute();	
		
			
	}
	

	public class MyClientTask extends AsyncTask<Void, Void, Void> {

		String dstAddress;
		int dstPort;
		String response = "";

		MyClientTask(String addr, int port) {
			dstAddress = addr;
			dstPort = port;
		}

		@Override
		protected Void doInBackground(Void... arg0) {

			OutputStream outputStream;
			Socket socket = null;

			try {
				Log.d("Creating Socket", dstAddress);
				socket = new Socket(dstAddress, dstPort);
				Log.d("MyClienet Task", "Destination Address : " + dstAddress);
				Log.d("MyClient Task", "Destination Port : " + dstPort + "");
				outputStream = socket.getOutputStream();
				PrintStream printStream = new PrintStream(outputStream);
				while (!socket.isClosed()) {
					if (ConnectServer.checkUpdate == 1) {
						Log.d("Gesture Sent", ConnectServer.resultGesture);
						System.out.println("Gesture Sent : "
								+ ConnectServer.resultGesture);
						printStream.print(ConnectServer.resultGesture);
						// printStream.close();
						ConnectServer.checkUpdate = 0;
						ConnectServer.resultGesture = "";
					}
					
				}

			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				Log.d("ConectServer", "Unknown Host");
				/*AlertDialog.Builder dlgAlert = new AlertDialog.Builder(context);
				String dispText = "IP Address is incorrect";
				dlgAlert.setMessage(dispText);
				dlgAlert.setTitle("Network Error");
				dlgAlert.setPositiveButton("OK", null);
				dlgAlert.setCancelable(true);
				dlgAlert.create().show();
				e.printStackTrace();
				Intent intent = new Intent(context, MainActivity.class);
				startActivity(intent);
				*/
				Log.d("ConnectService", e.toString());
				response = "UnknownHostException: " + e.toString();
			} catch (IOException e) {
				Log.d("CoonectServer", "IO Excpetion");
				// TODO Auto-generated catch block
				/*Intent intent = new Intent(context, MainActivity.class);
				startActivity(intent);*/
				
				Log.d("ConnectService", e.toString());
				response = "IOException: " + e.toString();
			} finally {
				if (socket != null) {
					try {
						socket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				Log.d("ConnectServer", "In Final Clinet");
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			Log.d("ConnectServer", "Socket closed");
			super.onPostExecute(result);
		}

	}

	@Override
	public void onBeginningOfSpeech() {
		// TODO Auto-generated method stub
          result.setText("Recording Speech ...");
	}

	@Override
	public void onBufferReceived(byte[] arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEndOfSpeech() {
		// TODO Auto-generated method stub
		
		result.setText("Speech Ended..");

	}

	@Override
	public void onError(int arg0) {
		// TODO Auto-generated method stub
		
		//result.setText("Erro while recording..");

	}

	@Override
	public void onEvent(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPartialResults(Bundle arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReadyForSpeech(Bundle arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResults(Bundle arg0) {
		// TODO Auto-generated method stub
		ArrayList<String> text = new ArrayList<String>();
		ArrayList<Float> confidence = new ArrayList<Float>();
		text = arg0.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
		result.setText(text.get(0));
		ConnectServer.checkUpdate = 1;
		ConnectServer.resultGesture = text.get(0); 

	}

	@Override
	public void onRmsChanged(float arg0) {
		// TODO Auto-generated method stub

	}

}
