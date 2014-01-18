package com.mhacksW14.snapshots;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class Snapcam extends Activity {
	
	SurfaceView mSurface;
	SurfaceHolder mHolder;
	static Context context;
	Camera bcamera;

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

}
