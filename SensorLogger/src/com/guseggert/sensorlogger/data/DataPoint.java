package com.guseggert.sensorlogger.data;

public class DataPoint {
	private int mSensorType;
	private float[] mValues;
	private long mTimestamp;
	
	public DataPoint(float[] values, int type, long time) {
		mSensorType = type;
		mValues = values;
		mTimestamp = time;
	}
	
	public long getTimestamp() {
		return mTimestamp;
	}
	
	public float[] getValues() {
		return mValues;
	}
	
	public int getSensorType() {
		return mSensorType;
	}
	
	
}
