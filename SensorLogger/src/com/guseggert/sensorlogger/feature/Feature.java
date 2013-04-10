package com.guseggert.sensorlogger.feature;

import java.util.ArrayList;

import com.guseggert.sensorlogger.data.DataPoint;

public abstract class Feature {
	@SuppressWarnings("unused")
	ArrayList<DataPoint> mDataPoints;
	
	public Feature(ArrayList<DataPoint> dataPoints) {
		mDataPoints = dataPoints;
	}
	
	public abstract void compute();
}
