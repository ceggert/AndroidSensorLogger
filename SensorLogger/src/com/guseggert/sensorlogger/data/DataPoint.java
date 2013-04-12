package com.guseggert.sensorlogger.data;

import com.guseggert.sensorlogger.SensorID;

public class DataPoint implements Comparable<DataPoint> {
	private SensorID mSensorID;
	private float mValue;
	private long mTimestamp;
	
	public DataPoint(float value, SensorID sensorID, long time) {
		mSensorID = sensorID;
		mValue = value;
		mTimestamp = time;
	}
	
	public long getTimestamp() {
		return mTimestamp;
	}
	
	public float getValue() {
		return mValue;
	}
	
	public SensorID getSensorID() {
		return mSensorID;
	}
	
	@Override
	public int compareTo(DataPoint p) {
		if (p == null) {
			throw new IllegalArgumentException("compare with null value");
		} else if (mTimestamp < p.getTimestamp()) {
			return -1;
		} else if (mTimestamp > p.getTimestamp()) {
			return 1;
		} else { 
			return 0;
		}		
	}
}
