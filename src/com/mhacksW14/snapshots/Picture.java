package com.mhacksW14.snapshots;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;

public class Picture extends Activity {
	
	ImageView image;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_picture);
		
		// Get the message from the intent
        Intent intent = getIntent();
        Uri pic = intent.getData();
        
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

}
