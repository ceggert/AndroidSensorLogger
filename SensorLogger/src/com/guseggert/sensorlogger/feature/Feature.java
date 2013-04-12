package com.guseggert.sensorlogger.feature;

import java.util.ArrayList;

import com.guseggert.sensorlogger.data.DataPoint;

public abstract class Feature {
	SparseArrayIterable<ArrayList<DataPoint>> mDataPoints;
	
	public Feature(SparseArrayIterable<ArrayList<DataPoint>> dataPoints) {
		mDataPoints = dataPoints;
	}
	
	public abstract void compute();
}
