package com.mhacksW14.snapshots;


import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.widget.EditText;

public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	public void login() throws JSONException, UnsupportedEncodingException {
		String url = "http://snapshothack.herokuapp.com/login";
		
		EditText usernameText = (EditText) findViewById(R.id.editText2);
		String username = usernameText.getText().toString();
		EditText passwordText = (EditText) findViewById(R.id.editText1);
		String password = passwordText.getText().toString();
		
		JSONObject json = new JSONObject();		
		json.put(new String("username"), new String("shotsshots"));
		json.put(new String("password"), new String("shotsshots"));
		StringEntity se = new StringEntity(json.toString());
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url);
			httppost.setEntity(se);
			HttpResponse response = httpclient.execute(httppost);
				//Do something with response...
			if(response != null) {
				SharedPreferences info = getSharedPreferences("loginInfo", 0);
				SharedPreferences.Editor editor = info.edit();
				editor.putString("username", username);
				editor.putString("password", password);
				editor.commit();
			}
		} catch (Exception e) {
			// show error
		}
		
		Intent intent = new Intent(getBaseContext(), Snapcam.class);
		startActivity(intent);
	}

}
