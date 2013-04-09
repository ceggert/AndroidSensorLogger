package com.guseggert.sensorlogger;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.util.Log;
import android.util.SparseArray;

public class TimeWindowMaker implements Observer {
	// each sensor is indexed by its type constant (e.g. Sensor.TYPE_*):
	
	private SparseArray<Sensor> mSensors;
	private ArrayList<TimeWindow> mTimeWindows = new ArrayList<TimeWindow>();
	
	private int mTimeWindowLength = 5000; // milliseconds
	private TimeWindow mCurrentTimeWindow = null;
	
	
	public TimeWindowMaker(SparseArray<Sensor> sensors) {
		mSensors = sensors;
	}
	
	@Override
	public void update(Observable observable, Object data) {
		SensorEvent msgData = (SensorEvent)data;
		int type = msgData.sensor.getType();
		float[] values = (float[])msgData.values;
		
		addPoint(values, type, msgData.timestamp);
	}
	
	// finishes the current time window and makes a new one
	// TODO: calculate features in another thread, then write it to a file and erase
	//		 it from memory
	private void newTimeWindow(long time) {
		if (mCurrentTimeWindow != null)
			mTimeWindows.add(mCurrentTimeWindow);
		mCurrentTimeWindow = new TimeWindow(time);

		Log.v("TimeWindowMaker", "New time window issued: " + mCurrentTimeWindow.getStartTime());

	}
	
	private void addPoint(float[] values, int type, long time) {
		if (needNewTimeWindow(time))
			newTimeWindow(time);
		mCurrentTimeWindow.addDataPoint(values, type, time);
		// Log.v("TimeWindowMaker", "Data point added: " + type + " " + time);
	}
	
	// checks if the current time window is expired or null
	private boolean needNewTimeWindow(long time) {
		// note that the sensor timestamp is in nanoseconds
		long interval = (time - mCurrentTimeWindow.getStartTime())/1000000;
		return mCurrentTimeWindow == null || interval >= mTimeWindowLength;
	}

}
