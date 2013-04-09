package com.guseggert.sensorlogger;

import android.util.SparseArray;

public class TimeWindow {
	private long mStartTime;
	private SparseArray<DataPoint[]> mSensorData = new SparseArray<DataPoint[]>();
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
	}
	
	
}
