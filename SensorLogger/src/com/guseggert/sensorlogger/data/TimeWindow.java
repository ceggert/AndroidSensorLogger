package com.guseggert.sensorlogger.data;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import android.util.SparseArray;

public class TimeWindow implements Observer {
	private long mStartTime; // nanoseconds
	private long mLength; 
	private SparseArray<ArrayList<DataPoint>> mSensorData = 
			new SparseArray<ArrayList<DataPoint>>();
	private boolean empty = true;
	
	public TimeWindow(long time, long length) {
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

	@Override
	public void update(Observable observable, Object data) {
		DataPoint dataPoint = (DataPoint) data;
		if (dataPoint.getTimestamp() - mStartTime >= mLength) {
			((TimeWindowMaker)observable).onTimeWindowFinished(this);
		}
	}
	
}
