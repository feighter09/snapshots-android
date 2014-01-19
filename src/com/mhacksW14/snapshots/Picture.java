package com.mhacksW14.snapshots;

import java.io.File;
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
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;

public class Picture extends Activity {
	
	ImageView image;
	Uri pic;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_picture);
		
		// Get the message from the intent
        Intent intent = getIntent();
        pic = intent.getData();
        
        image = (ImageView) findViewById(R.id.imageView1);
        image.setImageURI(pic);
        image.setRotation(90);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.picture, menu);
		return true;
	}
	
	public void send() throws JSONException, UnsupportedEncodingException {
		String url = "http://snapchatshots.herokuapp.com/send";
		File file = new File(pic.getPath());
		JSONObject json = new JSONObject();
		
		json.put(new String("file"), file);
		json.put(new String("username"), new String("shotsshots"));
		json.put(new String("password"), new String("shotsshots"));
		json.put(new String("recipient"), new String("ckushna"));
		StringEntity se = new StringEntity(json.toString());
		
		
		try {
		    HttpClient httpclient = new DefaultHttpClient();

		    HttpPost httppost = new HttpPost(url);

		    httppost.setEntity(se);
		    
		    HttpResponse response = httpclient.execute(httppost);
		    //Do something with response...

		} catch (Exception e) {
		    // show error
		}
	}

}
