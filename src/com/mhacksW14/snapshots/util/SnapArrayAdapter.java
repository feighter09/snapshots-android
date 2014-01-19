package com.mhacksW14.snapshots.util;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mhacksW14.snapshots.R;

public class SnapArrayAdapter extends ArrayAdapter<Snap> {
	
	private final Context context;
	private final List<Snap> values;
 
	public SnapArrayAdapter(Context context, List<Snap> snapsList) {
		super(context, android.R.layout.activity_list_item, snapsList);
		this.context = context;
		this.values = snapsList;
	}
 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View rowView = convertView;
		if (rowView == null) {
			rowView = inflater.inflate(R.layout.list_item, parent, false);
		}
		
		TextView nameView = (TextView) rowView.findViewById(R.id.name);
		nameView.setText( values.get(position).getFromName() );
		TextView sentView = (TextView) rowView.findViewById(R.id.sent);
		sentView.setText( values.get(position).getTimeSent() );
		
		final int index = position;
		rowView.setOnTouchListener( new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if ( event.getAction() == MotionEvent.ACTION_DOWN ) {
					showPicture(index);
					return true;
				} else if ( event.getAction() == MotionEvent.ACTION_UP ) {
					hidePicture();
					return false;
				}
				return true;
			}
		});
 
		return rowView;
	}
	
	public void showPicture(int index) {
		Snap snap = values.get(index);
		Intent intent = new Intent("snap-held");
		intent.putExtra("snap", snap);
		LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
	}
	
	public void hidePicture() {
		Intent intent = new Intent("snap-released");
		LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
	}
	
}