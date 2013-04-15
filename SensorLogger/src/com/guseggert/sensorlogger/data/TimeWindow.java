package com.guseggert.sensorlogger.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import android.util.Log;

import com.guseggert.sensorlogger.SensorID;

public class TimeWindow extends HashMap<SensorID, TimeSeries> implements Observer {
	private static final long serialVersionUID = 4834473217118341864L;
	private long mStartTime; // nanoseconds
	private long mLength; 
	
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
			if (this.get(sensorID) == null) {
				TimeSeries timeSeries = new TimeSeries();
				timeSeries.add(dataPoint);
				this.put(sensorID, timeSeries);
			}
			else {
				this.get(sensorID).add(dataPoint);
			}
		}
	}
	
	public void logContents() {
		for (Map.Entry<SensorID, TimeSeries> tw : this.entrySet()) {
			TimeSeries timeSeries = tw.getValue();
			for (DataPoint dp : timeSeries) {
				Log.v("TimeWindow", "Value: " + dp.getValue() + " Sensor: " + dp.getSensorID() +
						" Time: " + dp.getTimestamp());
			}
		}
	}
	
}
