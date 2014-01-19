package com.mhacksW14.snapshots;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class Snapcam extends Activity {
	
	
	SurfaceView mSurface;
	SurfaceHolder mHolder;
	static Context context;
	Camera bcamera;
	File picture;
	static int MEDIA_TYPE_IMAGE = 1;
	private static final int MEDIA_TYPE_VIDEO = 2;
	String TAG = "Snapshots";
	Uri here;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_snapcam);
		
		Snapcam.context = getApplicationContext();
		
		mSurface = (SurfaceView) findViewById(R.id.surfaceView1);
		mHolder = mSurface.getHolder();
		mHolder.addCallback(surfaceCallback);
		
		bcamera = Camera.open();
		initPreview();
		startPreview();
	}
	
	public void takePic(View view) {
		bcamera.takePicture(null, null, null, new PictureCallback() {
			
			@Override
			public void onPictureTaken(byte[] data, Camera camera) {
			

			            picture = getOutputMediaFile(MEDIA_TYPE_IMAGE);
			            if (picture == null){
			                Log.d(TAG, "Error creating media file");
			                return;
			            }
			            
			            here = Uri.fromFile(picture);
			            
			            try {
			                FileOutputStream fos = new FileOutputStream(picture);
			                fos.write(data);
			                fos.close();
			            } catch (FileNotFoundException e) {
			                Log.d(TAG, "File not found: " + e.getMessage());
			            } catch (IOException e) {
			                Log.d(TAG, "Error accessing file: " + e.getMessage());
			            }
			            
			            Intent intent = new Intent(getBaseContext(), Picture.class);
			    		intent.setData(here);
			    		
			    		bcamera.stopPreview();
			    		bcamera.release();
			    		
			    		startActivity(intent);
			}
		});

		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.snapcam, menu);
		return true;
	}
	
	
	private void initPreview() {
        if (bcamera != null && mHolder.getSurface() != null) {
            try {
                bcamera.setPreviewDisplay(mHolder);
            } catch (Throwable t) {
            
            }
            
            bcamera.setDisplayOrientation(90);
        }
    }

    private void startPreview() {
        if (bcamera != null) {
            bcamera.startPreview();
        }
    }
    
	SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            // no-op -- wait until surfaceChanged()
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            initPreview();
            startPreview();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // no-op
        }
    };
    
    
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                  Environment.DIRECTORY_PICTURES), "Snapshots");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("Snapshots", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
            "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
            "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

}
