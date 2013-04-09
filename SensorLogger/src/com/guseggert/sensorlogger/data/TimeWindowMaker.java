package com.guseggert.sensorlogger.data;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.util.Log;
import android.util.SparseArray;

public class TimeWindowMaker extends Observable implements Observer {
	private SparseArray<Sensor> mSensors;
	private ArrayList<TimeWindow> mTimeWindows = new ArrayList<TimeWindow>();
	private int mTimeWindowLength = 5000; // milliseconds
	private TimeWindow mCurrentTimeWindow = null;
	private float mTimeWindowOverlap = 0.5f; // overlap of time windows
	private long mTimeOfLastTimeWindow = 0;
	private TimeWindow mLastTimeWindow;
	
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
	
	private void newTimeWindow(long time) {
		
		mTimeOfLastTimeWindow = time;
	}
	
	private void addPoint(float[] values, int type, long time) {
		if (needNewTimeWindow(time)) {
			newTimeWindow(time);
			this.setChanged();
			notifyObservers(new DataPoint(values, type, time));
		}
			
		// mCurrentTimeWindow.addDataPoint(values, type, time);
		// Log.v("TimeWindowMaker", "Data point added: " + type + " " + time);
	}
	
	// checks if we need to create a new time window
	private boolean needNewTimeWindow(long time) {
		long interval = time - mLastTimeWindow.getStartTime();
		return interval*mTimeWindowOverlap >= m
	}
	
	
}
