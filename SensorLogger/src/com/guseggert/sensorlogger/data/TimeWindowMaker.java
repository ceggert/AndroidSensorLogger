package com.guseggert.sensorlogger.data;

import java.util.Observable;
import java.util.Observer;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.util.Log;

import com.guseggert.sensorlogger.SensorID;
import com.guseggert.sensorlogger.feature.FeatureSet;

// Each time window observes TimeWindowMaker for new data points.
// When a time window is full, it calls onTimeWindowFinished(), which
// removes the time window as an observer and then processes the data.
public class TimeWindowMaker extends Observable implements Observer {
	private long mTimeWindowLength = 5000000000l; // nanoseconds
	private float mTimeWindowOverlap = 0.5f; // overlap of time windows
	private TimeWindow mLastTimeWindow;
	
	public TimeWindowMaker() {
	}
	
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
	
	// Notifies observers of a new point.  Some incoming data will have multiple
	// axes, and this will create a data point for each axis of the value set.
	private void addPoint(float[] values, int type, long time) {
		if (mLastTimeWindow == null || boundaryReached(time)) {
			newTimeWindow(time);
		}

		switch (type) {
		case Sensor.TYPE_ACCELEROMETER:
			this.setChanged();
			notifyObservers(new DataPoint(values[0], SensorID.ACC_X, time));
			notifyObservers(new DataPoint(values[1], SensorID.ACC_Y, time));
			notifyObservers(new DataPoint(values[2], SensorID.ACC_Z, time));
			break;
		case Sensor.TYPE_GYROSCOPE:
			this.setChanged();
			notifyObservers(new DataPoint(values[0], SensorID.GYRO_X, time));
			notifyObservers(new DataPoint(values[1], SensorID.GYRO_Y, time));
			notifyObservers(new DataPoint(values[2], SensorID.GYRO_Z, time));
			break;
		case Sensor.TYPE_GRAVITY:
			this.setChanged();
			notifyObservers(new DataPoint(values[0], SensorID.GRAV_X, time));
			notifyObservers(new DataPoint(values[1], SensorID.GRAV_X, time));
			notifyObservers(new DataPoint(values[2], SensorID.GRAV_X, time));
			break;
		case Sensor.TYPE_LINEAR_ACCELERATION:
			this.setChanged();
			notifyObservers(new DataPoint(values[0], SensorID.LINACC_X, time));
			notifyObservers(new DataPoint(values[1], SensorID.LINACC_X, time));
			notifyObservers(new DataPoint(values[2], SensorID.LINACC_X, time));
			break;
		case Sensor.TYPE_ROTATION_VECTOR:
			this.setChanged();
			notifyObservers(new DataPoint(values[0], SensorID.ROTVEC_X, time));
			notifyObservers(new DataPoint(values[1], SensorID.ROTVEC_X, time));
			notifyObservers(new DataPoint(values[2], SensorID.ROTVEC_X, time));
			break;
		default:
			throw new IllegalArgumentException("addPoint received an invalid sensor type");
		}
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
		FeatureSet fs = new FeatureSet(timeWindow);
		fs.computeFeatures();
		// extract features
		// write to csv
	}
}
