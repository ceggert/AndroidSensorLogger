package com.guseggert.sensorlogger.data;

import java.util.Observable;
import java.util.Observer;

import android.hardware.SensorEvent;
import android.util.Log;

public class TimeWindowMaker extends Observable implements Observer {
	private long mTimeWindowLength = 5000000000l; // nanoseconds
	private float mTimeWindowOverlap = 0.5f; // overlap of time windows
	private TimeWindow mLastTimeWindow;
	
	@Override
	public void update(Observable observable, Object data) {
		SensorEvent msgData = (SensorEvent)data;
		int type = msgData.sensor.getType();
		float[] values = (float[])msgData.values;
		
		addPoint(values, type, msgData.timestamp);
	}
	
	private void newTimeWindow(long time) {
		TimeWindow tw = new TimeWindow(time, mTimeWindowLength);
		addObserver(tw);
		mLastTimeWindow = tw;
		Log.i("TimeWindowMaker", "Creating time window: " + time);
	}
	
	private void addPoint(float[] values, int type, long time) {
		if (mLastTimeWindow == null || boundaryReached(time)) {
			newTimeWindow(time);
		}
		this.setChanged();
		notifyObservers(new DataPoint(values, type, time));
			
		// mCurrentTimeWindow.addDataPoint(values, type, time);
		// Log.v("TimeWindowMaker", "Data point added: " + type + " " + time);
	}
	
	// checks if we need to create a new time window
	private boolean boundaryReached(long time) {
		long interval = time - mLastTimeWindow.getStartTime();
		return interval >= mTimeWindowLength*mTimeWindowOverlap;
	}
	
	// called by a time window when it is full
	public void onTimeWindowFinished(TimeWindow timeWindow) {
		deleteObserver(timeWindow);
		Log.i("TimeWindowMaker", "Deleting time window: " + timeWindow.getStartTime());
		// extract features
		// write to csv
	}
}