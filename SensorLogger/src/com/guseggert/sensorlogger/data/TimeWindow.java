package com.guseggert.sensorlogger.data;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import android.util.SparseArray;

import com.guseggert.sensorlogger.feature.Feature;

public class TimeWindow implements Observer {
	private long mStartTime; // nanoseconds
	private long mLength; 
	private SparseArray<ArrayList<DataPoint>> mSensorData = 
			new SparseArray<ArrayList<DataPoint>>();
	private boolean empty = true;
	private SparseArray<Feature> mFeatures = new SparseArray<Feature>();
	
	public TimeWindow(long time, long length) {
		mStartTime = time;
		mLength = length;
	}
	
	public void setStartTime(long t) {
		mStartTime = t;
	}
	
	public long getStartTime() {
		return mStartTime;
	}
	
	public SparseArray<ArrayList<DataPoint>> getSensorData() {
		return mSensorData;
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
		long difference = dataPoint.getTimestamp() - mStartTime;
		if (dataPoint.getTimestamp() - mStartTime >= mLength) {
			((TimeWindowMaker)observable).onTimeWindowFinished(this);
		}
	}
	
}
