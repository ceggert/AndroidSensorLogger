package com.guseggert.sensorlogger;

import java.util.HashMap;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.SparseArray;

// Each new sensor value is written to the buffer, overwriting its previous value.
// The buffer is read from approx. every 50 ms, and the values are written to CSV.
// This ensures consistent time intervals in the dataset.
public class SensorLogger implements SensorEventListener, Runnable {
	private SensorManager mSensorManager;
	private SparseArray<Sensor> mSensors = new SparseArray<Sensor>();
	private Thread mThread;
	private DataWriter mWriter;
	private final int mDelay = SensorManager.SENSOR_DELAY_FASTEST;
	private Handler mUIHandler = null;
	private static SensorLogger singleton = null;
	private HashMap<SensorID, Float> mBuffer = new HashMap<SensorID, Float>();
	private long mLastBufferWrite = 0;
	private long mBufferWriteInterval = 50000000L; // in nanoseconds
	private String mActivity;
		
	public static SensorLogger getInstance(SensorManager sensorManager, Handler uiHandler) {
		return singleton==null ? new SensorLogger(sensorManager, uiHandler) : singleton;
	}
	
	private SensorLogger(final SensorManager sensorManager, final Handler uiHandler) {
		mSensorManager = sensorManager;
		mUIHandler = uiHandler;
	}
	
	// This is run by the thread:
	@Override
	public void run() {
		initSensors();
		initDataWriter();
		initBuffer();
	}
	
	public void start() {
		Log.v("SensorLogger", "Starting sensor logger...");
		mThread = new Thread(this);
		mThread.start();
	}
	
	public void stop() {
		mSensorManager.unregisterListener(this);
		mThread.interrupt();
	}

	public void onSensorChanged(final SensorEvent event) {		
		if (mSensors.get(event.sensor.getType()) != null) { // if the sensor is in the sensor array
			writeBuffer(event);
			updateBuffer(event);
		}
		if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) 
			Log.v("SensorLogger", "Time: " + event.timestamp);
	}
	
	// updates the values in the buffer on the new sensor event
	private void updateBuffer(SensorEvent event) {
		int i = 0;
		for (SensorID sensorID : SensorID.getSensorIDs(event.sensor.getType())) {
			mBuffer.put(sensorID, event.values[i]);
			i++;
		}
	}
	
	
	private void writeBuffer(SensorEvent event) {
		if (mLastBufferWrite == 0) {
			mLastBufferWrite = event.timestamp;
		}
		else if (event.timestamp - mLastBufferWrite >= mBufferWriteInterval) {
			mLastBufferWrite += mBufferWriteInterval;
			mWriter.writeLine(mBuffer, mLastBufferWrite, mActivity);
		}
		updateUI(event);
	}
	
	private void updateUI(SensorEvent event) {
		Message msg = Message.obtain(mUIHandler, MainActivity.MSG_SENSOR_UPDATE, event.sensor.getType(), 0, event.values);
		msg.sendToTarget();
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
	
	private void initSensor(final int type) {
		Sensor sensor = mSensorManager.getDefaultSensor(type);
		if (sensor == null) Log.e("SensorLogger", "Sensor not working: " + type);
		else {
			mSensors.put(type, sensor);
			mSensorManager.registerListener(this, mSensors.get(type), mDelay);
		}
	}
	
	private void initSensor(final int[] types) {
		for (int type : types) {
			initSensor(type);
		}
	}	
	
	private void initDataWriter() {
		mWriter = new DataWriter(mSensors);
	}
	
	// initialize buffer to all zeros
	private void initBuffer() {
		for (SensorID sensorID : SensorID.getSensorIDs(mSensors))
			mBuffer.put(sensorID, 0f);
	}
	
	public void updateActivity(String activity) {
		mActivity = activity;
	}
}
