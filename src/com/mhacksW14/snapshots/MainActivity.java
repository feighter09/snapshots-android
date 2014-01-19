package com.mhacksW14.snapshots;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.mhacksW14.snapshots.util.Snap;
import com.mhacksW14.snapshots.util.SnapArrayAdapter;


@EActivity
public class MainActivity extends ListActivity {

//	String jsonString = "{'Snaps':[{'from': 'afeight9', 'sent_time': '1350921506492'}, {'from': 'afeight9', 'sent_time': '1350921506492'}]}";
//	@ViewById(android.R.id.list)
//	ListView snapsListView;
	
	MediaRecorder recorder;
	static final int REQUEST_VIDEO_CAPTURE = 1;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getAllSnaps("shotsshots", "shotsshots");
        
        LocalBroadcastManager.getInstance(this).registerReceiver(mCellHeldReceiver, new IntentFilter("snap-held"));
        LocalBroadcastManager.getInstance(this).registerReceiver(mCellReleasedReceiver, new IntentFilter("snap-released"));
        
        ListView snapsListView = (ListView) findViewById(android.R.id.list);
        snapsListView.setOnTouchListener( new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if ( event.getAction() == MotionEvent.ACTION_UP ) {
					Intent intent = new Intent("snap-released");
					LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(intent);
					
					
				}
				return false;
			}
		});
    }

    @Background
    public void getAllSnaps(String username, String password) {
    	String url = "http://snapchatshots.herokuapp.com/getall";
    	
    	HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000);
        JSONObject json = new JSONObject();

        try {
            HttpPost post = new HttpPost(url);
            json.put("username", username);
            json.put("password", password);
            StringEntity se = new StringEntity( json.toString() );  
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            post.setEntity(se);
            
//            HttpResponse response = client.execute(post);
//            String responseStr = EntityUtils.toString(response.getEntity());
//            JSONObject JSON = new JSONObject("{'Snaps':[{'from': 'afeight9', 'sent_time': '1350921506492'}, {'from': 'afeight9', 'sent_time': '1350921506492'}]}");
            initList("{'Snaps':[{'from': 'afeight9', 'sent_time': '1350921506492'}, {'from': 'afeight9', 'sent_time': '1350921506492'}]}");

        } catch(Exception e) {
            e.printStackTrace();
//            createDialog("Error", "Cannot Estabilish Connection");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    List<Snap> snapsList = new ArrayList<Snap>();
    private void initList(String jsonString){
	  
		try{
			JSONObject jsonResponse = new JSONObject(jsonString);
			JSONArray jsonMainNode = jsonResponse.optJSONArray("Snaps");
		  
			for(int i = 0; i < jsonMainNode.length(); i++){
				JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
				  
				Snap snap = new Snap();
				snap.setFromName( jsonChildNode.optString("from") );
				snap.setTimeSent( jsonChildNode.optString("sent_time") );
				if ( jsonChildNode.optString("sent_time") == "yes" ){
					snap.setViewed(true);
				} else {
					snap.setViewed(false);
				}
				snap.setPhoto(null);
				snapsList.add(snap);
			}
		} catch(JSONException e) {
			Toast.makeText(getApplicationContext(), "Error"+e.toString(), Toast.LENGTH_SHORT).show();
		}
		ListView snapsListView = (ListView) findViewById(android.R.id.list);
		snapsListView.setAdapter( new SnapArrayAdapter(getBaseContext(), snapsList) );
    }
    
    private BroadcastReceiver mCellHeldReceiver = new BroadcastReceiver() {
	  @Override
	  public void onReceive(Context context, Intent intent) {
		Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
			startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
		}
		
		recorder = new MediaRecorder();
		
		Camera camera = Camera.open(0);
		camera.unlock();
		recorder.setCamera( camera );
		
		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
		recorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
		
		recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		recorder.setOutputFile("/sdcard/recordvideooutput.3gpp");
		
		try {
			recorder.prepare();
			recorder.start();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
		Snap snap = (Snap) intent.getSerializableExtra("snap");
		Log.d("receiver", "Cell held down");
		ImageView snapImage = (ImageView) findViewById(R.id.snapView);
		snapImage.setImageResource(R.drawable.images);
		snapImage.setVisibility(ImageView.VISIBLE);
	  }
	};
	private BroadcastReceiver mCellReleasedReceiver = new BroadcastReceiver() {
	  @Override
	  public void onReceive(Context context, Intent intent) {
	    Log.d("receiver", "Cell released");
	    ImageView snapImage = (ImageView) findViewById(R.id.snapView);
	    snapImage.setVisibility(ImageView.INVISIBLE);
	    
	    recorder.stop();
	  }
	};
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
	    if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
	        Uri videoUri = intent.getData();
//	        mVideoView.setVideoURI(videoUri);
	    }
	}
	
	// network

	@Override
	protected void onDestroy() {
	  // Unregister since the activity is about to be closed.
	  LocalBroadcastManager.getInstance(this).unregisterReceiver(mCellHeldReceiver);
	  LocalBroadcastManager.getInstance(this).unregisterReceiver(mCellReleasedReceiver);
	  super.onDestroy();
	}
}
