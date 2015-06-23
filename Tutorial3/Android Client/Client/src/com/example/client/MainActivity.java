package com.example.client;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

	EditText editTextAddress;
	Button buttonConnect, buttonClear;
	Intent intent = null;
	Context context = null;
	TextView response = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = this;
		response = (TextView) findViewById(R.id.response);
		editTextAddress = (EditText) findViewById(R.id.address);
		buttonConnect = (Button) findViewById(R.id.connect);
		buttonClear = (Button) findViewById(R.id.clear);
		buttonConnect.setOnClickListener(buttonconnectOnClickListener);
		buttonClear.setOnClickListener(buttonclearClickListener);
	}

	OnClickListener buttonclearClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			editTextAddress.clearComposingText();
			editTextAddress.setHint("Server Address");
		}

	};

	OnClickListener buttonconnectOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			if (editTextAddress != null
					&& !editTextAddress.getText().toString().isEmpty()) {
				intent = new Intent(context, ConnectServer.class);
				intent.putExtra("ip", editTextAddress.getText().toString());
				startActivity(intent);
			}
			else
			{
				response.setText("IP Address field is empty");
			}
		}
	};
}
