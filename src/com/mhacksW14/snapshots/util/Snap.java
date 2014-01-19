package com.mhacksW14.snapshots.util;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;

public class Snap implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1414671420101566050L;
	
	String fromName;
	Date timeSent;
	Bitmap photo;
	boolean viewed = false;
	
	public Map<String, String> getHash() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("from", fromName);
		
		long diff = (new Date().getTime()) - timeSent.getTime();
		int hours = (int) (diff / 1000 / 60 / 60);
		int days = (int) (diff / 1000 / 60 / 60 / 24);
		
		if (days > 0) {
			String value = days + (days > 1 ? " days" : " day") + " ago";
			map.put("timeAgo", value);
		} else {
			String value = hours + (hours > 1 ? " hours" : " hour") + " ago";
			map.put("timeAgo", value);
		}
		
		return map;
	}
	
	public String getFromName() {
		return fromName;
	}
	public void setFromName(String fromName) {
		this.fromName = fromName;
	}
	public String getTimeSent() {
		long diff = (new Date().getTime()) - timeSent.getTime();
		int hours = (int) (diff / 1000 / 60 / 60);
		int days = (int) (diff / 1000 / 60 / 60 / 24);
		
		if (days > 0) {
			return days + (days > 1 ? " days" : " day") + " ago";
		} else {
			return hours + (hours > 1 ? " hours" : " hour") + " ago";
		}
	}
	public void setTimeSent(String timeSent) {
		this.timeSent = new Date(Long.valueOf(timeSent));
	}
	public Bitmap getPhoto() {
		return photo;
	}
	public void setPhoto(Bitmap photo) {
		this.photo = photo;
	}
	public boolean isViewed() {
		return viewed;
	}

	public void setViewed(boolean viewed) {
		this.viewed = viewed;
	}
}
