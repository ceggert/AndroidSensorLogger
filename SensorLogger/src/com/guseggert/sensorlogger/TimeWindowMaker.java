package com.guseggert.sensorlogger;

import java.util.Observable;
import java.util.Observer;

import android.hardware.Sensor;
import android.os.Message;
import android.util.Log;
import android.util.SparseArray;

public class TimeWindowMaker implements Observer {
	// each sensor is indexed by its type constant (e.g. Sensor.TYPE_*):
	private SparseArray<DataPoint[]> mSensorData;
	private SparseArray<Sensor> mSensors;
	
	public TimeWindowMaker(SparseArray<Sensor> sensors) {
		mSensors = sensors;
		mSensorData = new SparseArray<DataPoint[]>();
	}
	
	@Override
	public void update(Observable observable, Object data) {
		
		Log.i("TimeWindowMaker", Integer.toString(((Message)data).arg1));
	}

}
