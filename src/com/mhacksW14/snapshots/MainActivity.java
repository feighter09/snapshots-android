package com.mhacksW14.snapshots;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.mhacksW14.snapshots.util.Snap;

@EActivity
public class MainActivity extends ListActivity {

//	String jsonString = "{'Snaps':[{'from': 'afeight9', 'sent_time': '1350921506492'}, {'from': 'afeight9', 'sent_time': '1350921506492'}]}";
	@ViewById(android.R.id.list)
	ListView snapsListView;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Map<String, String> userPass = new TreeMap<String, String>();
        userPass.put("username", "shotsshots");
        userPass.put("password", "shotsshots");
        
        GetAllAsync getSnaps = new GetAllAsync().execute()
        snapsListView.setAdapter( new SnapArrayAdapter(this, snapsList) );
        
        LocalBroadcastManager.getInstance(this).registerReceiver(mCellHeldReceiver, new IntentFilter("snap-held"));
        LocalBroadcastManager.getInstance(this).registerReceiver(mCellReleasedReceiver, new IntentFilter("snap-released"));
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
    }
    
    private BroadcastReceiver mCellHeldReceiver = new BroadcastReceiver() {
	  @Override
	  public void onReceive(Context context, Intent intent) {
		  Snap snap = (Snap) intent.getSerializableExtra("snap");
		  Log.d("receiver", "Cell held down");
		  ImageView snapImage = new ImageView(getBaseContext());
		  snap.setPhoto(snap.getPhoto());
		  setContentView(snapImage);
	  }
	};
	private BroadcastReceiver mCellReleasedReceiver = new BroadcastReceiver() {
	  @Override
	  public void onReceive(Context context, Intent intent) {
	    Log.d("receiver", "Cell released");
	  }
	};
	
	// network
	
	private class GetAllAsync extends AsyncTask<Object, Void, String> {
        @Override
        protected String doInBackground(Object... objects) {
        	int responseCode = -1;
        	
        	try {
        		
        		URL url = new URL("http://snapshot.herokuapp.com/getall");
        		HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        		connection.connect();
        		responseCode = connection.getResponseCode();
        		Log.d("URL", "Code: " + responseCode);
        		
        		if (responseCode == HttpsURLConnection.HTTP_OK) {
        			InputStream inputStream = connection.getInputStream();
        			Reader reader = new InputStreamReader(inputStream);
        			int contentLength = connection.getContentLength();
        			char[] charArray = new char[contentLength];
        			reader.read(charArray);
        			
        			String responseData = new String(charArray);
        			Log.v("response", responseData);
        			
        		} else {
        			Log.d("API Connection", "Request failed: " + responseCode);
        		}
	        		
	        } catch (MalformedURLException e) {
	        	Log.d("API Connection", "Request failed, something with malformed url. here's stuff:");
	        	e.printStackTrace();
	        } catch (IOException e) {
	        	Log.d("API Connection", "Request failed, IOException. what a bummer. read all about it:");
				e.printStackTrace();
			}
        	return "success";
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            // TODO 
            
       }
    }
	
	
	
	@Override
	protected void onDestroy() {
	  // Unregister since the activity is about to be closed.
	  LocalBroadcastManager.getInstance(this).unregisterReceiver(mCellHeldReceiver);
	  LocalBroadcastManager.getInstance(this).unregisterReceiver(mCellReleasedReceiver);
	  super.onDestroy();
	}
}
