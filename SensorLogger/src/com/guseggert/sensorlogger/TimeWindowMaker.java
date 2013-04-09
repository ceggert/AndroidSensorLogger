package com.guseggert.sensorlogger;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import android.hardware.Sensor;
import android.os.Message;
import android.util.Log;

public class TimeWindowMaker implements Observer {
	// each sensor is indexed by its type constant (e.g. Sensor.TYPE_*):
	private HashMap<Integer, DataPoint[]> mSensorData;
	private HashMap<Integer, Sensor> mSensors;
	
	
	public TimeWindowMaker(HashMap<Integer, Sensor> sensors) {
		mSensors = sensors;
		mSensorData = new HashMap<Integer, DataPoint[]>();
	}
	
	@Override
	public void update(Observable observable, Object data) {
		Log.i("TimeWindowMaker", Integer.toString(((Message)data).arg1));
	}

}
