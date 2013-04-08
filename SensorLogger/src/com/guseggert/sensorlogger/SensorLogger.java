package com.guseggert.sensorlogger;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;

public class SensorLogger implements SensorEventListener, Runnable {
	public static enum Command { STOP, START };
	
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	private Sensor mGyroscope;
	private Sensor mGravity;
	private Sensor mLinAccel;
	private Sensor mRotVec;

	Handler mUIHandler = null;
	
//	Handler mHandler = new Handler() {
//		@Override
//		public void handleMessage(Message msg) {
//			switch((Command)msg.obj) {
//			case STOP:
//				mSensorManager.unregisterListener();
//				break;
//			case START:
//				break;
//			default:
//				break;
//			}
//		}
//	};
	
	private static SensorLogger singleton = null;
	
	public static SensorLogger getInstance(SensorManager sensorManager, Handler uiHandler) {
		return singleton==null ? new SensorLogger(sensorManager, uiHandler) : singleton;
	}
	
	private SensorLogger(SensorManager sensorManager, Handler uiHandler) {
		mSensorManager = sensorManager;
		mUIHandler = uiHandler;
		Thread thread = new Thread(this);
		thread.start();
	}
	
	@Override
	public void run() {
		initSensors();
	}
	
	public void stop() {
		mSensorManager.unregisterListener(this);
	}

	// Updates on UI thread when new sensor values arrive
	public void onSensorChanged(final SensorEvent event) {
		int type = event.sensor.getType();
		if (type == Sensor.TYPE_ACCELEROMETER ||
				type == Sensor.TYPE_GYROSCOPE ||
				type == Sensor.TYPE_LINEAR_ACCELERATION ||
				type == Sensor.TYPE_GRAVITY ||
				type == Sensor.TYPE_ROTATION_VECTOR) {
			Message msg = Message.obtain(mUIHandler, 0, type, 0, event.values);
			msg.sendToTarget();
		}
		return;
	}
	
	@Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
	
	private void initSensors() {
		initAccelerometer();
		initGyroscope();		
        initGravity();
        initLinearAccel();
        initRotationVector();
	}
	
	private void initAccelerometer() {
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);
	}
	
	private void initGyroscope() {
		mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
		mSensorManager.registerListener(this,  mGyroscope, SensorManager.SENSOR_DELAY_FASTEST);
	}
	
	private void initGravity() {
		mGravity = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
		mSensorManager.registerListener(this, mGravity, SensorManager.SENSOR_DELAY_FASTEST);
	}
	
	private void initLinearAccel() {
		mLinAccel = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
		mSensorManager.registerListener(this, mLinAccel, SensorManager.SENSOR_DELAY_FASTEST);
	}
	
	private void initRotationVector() {
		mRotVec = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
		mSensorManager.registerListener(this, mRotVec, SensorManager.SENSOR_DELAY_FASTEST);
	}
	
	
	
}
