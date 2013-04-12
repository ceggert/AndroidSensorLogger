package com.guseggert.sensorlogger.data;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import com.guseggert.sensorlogger.SensorID;
import com.guseggert.sensorlogger.feature.FeatureSet;

public class TimeWindow implements Observer {
	private long mStartTime; // nanoseconds
	private long mLength; 
	// we're using sparse arrays instead of hash tables for performance
	private HashMap<SensorID, TimeSeries> mTimeSeries = new HashMap<SensorID, TimeSeries>();
	private FeatureSet mFeatureSet;
	
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
	
	public void addTimeSeries(SensorID id, TimeSeries timeSeries) {
		mTimeSeries.put(id, timeSeries);
	}
	
	public TimeSeries getTimeSeries(SensorID id) {
		return mTimeSeries.get(id);
	}

	@Override
	public void update(Observable observable, Object data) {
		DataPoint dataPoint = (DataPoint) data;
		SensorID sensorID = dataPoint.getSensorID();
		long difference = dataPoint.getTimestamp() - mStartTime;
		if (difference >= mLength) {
			((TimeWindowMaker)observable).onTimeWindowFinished(this);
		}
		else {
			// if the ts is not in the hash table, make a new one and add it
			if (mTimeSeries.get(sensorID.ordinal()) == null) {
				TimeSeries timeSeries = new TimeSeries();
				timeSeries.add(dataPoint);
			}
			else {
				mTimeSeries.get(sensorID.ordinal()).add(dataPoint);
			}
		}
	}
	
}
