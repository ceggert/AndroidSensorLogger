package com.guseggert.sensorlogger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.SparseArray;

public class SensorLogger implements SensorEventListener, Runnable {
	public static enum Command { STOP, START };
	
	private SensorManager mSensorManager;
	private SparseArray<Sensor> mSensors = new SparseArray<Sensor>();
	private Thread mThread;
	private DataWriter mWriter;
	private final int mDelay = SensorManager.SENSOR_DELAY_UI;
	private Handler mUIHandler = null;
	private static SensorLogger singleton = null;
	private HashMap<SensorID, Float> mBuffer = new HashMap<SensorID, Float>();
	private long mLastBufferWrite = 0;
	private long mBufferWriteInterval = 50000000L; // in nanoseconds
	private String mActivity;
	
	// Handles messages from the main activity (change in selected activity)
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			
		}
	};

	
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
	}
	
	// updates the values in the buffer on the new sensor event
	private void updateBuffer(SensorEvent event) {
		int i = 0;
		for (SensorID sensorID : getSensorIDs(event.sensor.getType())) {
			mBuffer.put(sensorID, event.values[i]);
			i++;
		}
	}
	
	public static ArrayList<SensorID> getSensorIDs(int sensorType) {
		switch (sensorType) {
		case Sensor.TYPE_ACCELEROMETER:
			return new ArrayList<SensorID>(Arrays.asList(SensorID.ACC_X, SensorID.ACC_Y, SensorID.ACC_Z));
		case Sensor.TYPE_GYROSCOPE:
			return new ArrayList<SensorID>(Arrays.asList(SensorID.GYRO_X, SensorID.GYRO_Y, SensorID.GYRO_Z));
		case Sensor.TYPE_GRAVITY:
			return new ArrayList<SensorID>(Arrays.asList(SensorID.GRAV_X, SensorID.GRAV_Y, SensorID.GRAV_Z));
		case Sensor.TYPE_LINEAR_ACCELERATION:
			return new ArrayList<SensorID>(Arrays.asList(SensorID.LINACC_X, SensorID.LINACC_Y, SensorID.LINACC_Z));
		case Sensor.TYPE_ROTATION_VECTOR:
			return new ArrayList<SensorID>(Arrays.asList(SensorID.ROTVEC_X, SensorID.ROTVEC_Y, SensorID.ROTVEC_Z));
		default:
			throw new IllegalArgumentException("Invalid sensor type in getSensorIDs()");
		}
	}
	
	public static ArrayList<SensorID> getSensorIDs(int[] sensorTypes) {
		ArrayList<SensorID> sensorIDs = new ArrayList<SensorID>();
		for (int sensorType : sensorTypes)
			sensorIDs.addAll(getSensorIDs(sensorType));
		return sensorIDs;
	}
	
	private ArrayList<SensorID> getSensorIDs(SparseArray<Sensor> sensors) {
		ArrayList<SensorID> sensorIDs = new ArrayList<SensorID>();
		for (int i = 0; i < sensors.size(); i++)
			sensorIDs.addAll(getSensorIDs(sensors.valueAt(i).getType()));
		return sensorIDs;
	}
	
	private void writeBuffer(SensorEvent event) {
		Log.v("WriteBuffer", "Writing buffer...");
		if (mLastBufferWrite == 0) {
			mLastBufferWrite = event.timestamp;
			updateUI(event);
		}
		else if (event.timestamp - mLastBufferWrite >= mBufferWriteInterval) {
			mLastBufferWrite += mBufferWriteInterval;
			mWriter.writeLine(mBuffer, mLastBufferWrite, mActivity);
			updateUI(event);
		}
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
		for (SensorID sensorID : getSensorIDs(mSensors))
			mBuffer.put(sensorID, 0f);
	}
	
	public void updateActivity(String activity) {
		mActivity = activity;
	}
}
