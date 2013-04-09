package com.guseggert.sensorlogger;

import java.util.HashMap;
import java.util.Observable;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class SensorLogger extends Observable implements SensorEventListener, Runnable {
	public static enum Command { STOP, START };
	
	private SensorManager mSensorManager;
	private HashMap<Integer, Sensor> mSensors = new HashMap<Integer, Sensor>();
	private Thread mThread;
	private final int mDelay = SensorManager.SENSOR_DELAY_UI;
	private TimeWindowMaker mTimeWindowMaker;
	Handler mUIHandler = null;
	private static SensorLogger singleton = null;
	
	public static SensorLogger getInstance(SensorManager sensorManager, Handler uiHandler) {
		return singleton==null ? new SensorLogger(sensorManager, uiHandler) : singleton;
	}
	
	private SensorLogger(SensorManager sensorManager, Handler uiHandler) {
		mSensorManager = sensorManager;
		mUIHandler = uiHandler;
		mTimeWindowMaker = new TimeWindowMaker(mSensors);
	}
	
	// This is run by the thread:
	@Override
	public void run() {
		initSensors();
		addObserver(mTimeWindowMaker);
	}
	
	public void start() {
		mThread = new Thread(this);
		mThread.start();
	}
	
	public void stop() {
		mSensorManager.unregisterListener(this);
		mThread.interrupt();
	}

	// Updates on UI thread when new sensor values arrive
	public void onSensorChanged(final SensorEvent event) {
		int type = event.sensor.getType();
		
		if (mSensors.containsKey(type)) { // if the sensor is in the sensor list
			Message msg = Message.obtain(mUIHandler, 0, type, 0, event.values);
			msg.sendToTarget();
			this.setChanged();
			notifyObservers(msg);
		}
		return;
	}
	
	@Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
	
	private void initSensors() {
		int[] sensors = {Sensor.TYPE_ACCELEROMETER, 
						 Sensor.TYPE_GRAVITY,
						 Sensor.TYPE_LINEAR_ACCELERATION,
						 Sensor.TYPE_ROTATION_VECTOR,
						 Sensor.TYPE_GYROSCOPE};
		initSensor(sensors);
	}
	
	private void initSensor(int type) {
		mSensors.put(type, mSensorManager.getDefaultSensor(type));
		mSensorManager.registerListener(this, mSensors.get(type), mDelay);
	}
	
	private void initSensor(int[] types) {
		for (int type : types) {
			initSensor(type);
		}
	}	
}
