package com.guseggert.sensorlogger.data;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import android.util.SparseArray;

public class TimeWindow {
	private long mStartTime;
	private SparseArray<ArrayList<DataPoint>> mSensorData = 
			new SparseArray<ArrayList<DataPoint>>();
	private boolean empty = true;
	
	public TimeWindow(long time) {
		mStartTime = time;
	}
	
	public void setStartTime(long t) {
		mStartTime = t;
	}
	
	public long getStartTime() {
		return mStartTime;
	}
	
	public void addDataPoint(float[] values, int type, long time) {
		if (empty) {
			mStartTime = time;
			empty = false;
		}
		DataPoint dp = new DataPoint(values, type, time);
		
		if (mSensorData.get(type) == null) // create the ArrayList, if needed
			mSensorData.append(type, new ArrayList<DataPoint>());
		
		mSensorData.get(type).add(dp);
//		Log.v("TimeWindow", "Added data: " 
//				+ mSensorData.get(type).get(mSensorData.get(type).size()-1).getValues()[0]);
	}
	
}
