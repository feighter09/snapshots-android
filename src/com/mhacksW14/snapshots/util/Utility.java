package com.mhacksW14.snapshots.util;

import java.util.Iterator;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

public class Utility {

	public static HttpResponse makeRequest(String path, Map<String, String> params) throws Exception {
	    DefaultHttpClient httpclient = new DefaultHttpClient();
	    HttpPost httpost = new HttpPost(path);
	    JSONObject holder = getJsonObjectFromMap(params);
	    StringEntity se = new StringEntity(holder.toString());

	    httpost.setEntity(se);
	    httpost.setHeader("Accept", "application/json");
	    httpost.setHeader("Content-type", "application/json");
 
	    ResponseHandler responseHandler = new BasicResponseHandler();
	    return httpclient.execute(httpost, responseHandler);
	}
	
	private static JSONObject getJsonObjectFromMap(Map params) throws JSONException {

	    Iterator iter = params.entrySet().iterator();
	    JSONObject holder = new JSONObject();

	    //using the earlier example your first entry would get email
	    //and the inner while would get the value which would be 'foo@bar.com' 
	    //{ fan: { email : 'foo@bar.com' } }

	    while (iter.hasNext()) {
	        Map.Entry pairs = (Map.Entry)iter.next();
	        String key = (String)pairs.getKey();
	        String value = (String) pairs.getValue();

	        holder.put(key, value);
	    }
	    return holder;
	}
}
