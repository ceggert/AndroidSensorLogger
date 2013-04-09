package com.guseggert.sensorlogger;

import java.util.Observable;
import java.util.Observer;

import android.hardware.Sensor;
import android.os.Message;
import android.util.Log;

public class TimeWindowMaker implements Observer {
	private HashMap<>
	
	
	public TimeWindowMaker() {
		
	}
	
	@Override
	public void update(Observable observable, Object data) {
		Log.i("TimeWindowMaker", Integer.toString(((Message)data).arg1));
	}

}
