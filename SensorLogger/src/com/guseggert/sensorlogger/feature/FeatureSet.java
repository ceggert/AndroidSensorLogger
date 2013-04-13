package com.guseggert.sensorlogger.feature;

import java.util.HashMap;

import com.guseggert.sensorlogger.data.TimeWindow;

public class FeatureSet extends HashMap<FeatureID, Float> {
	private TimeWindow mTimeWindow;
	
	// calculates feature set from a time window
	public FeatureSet(TimeWindow timeWindow) {
		mTimeWindow = timeWindow;
		computeFeatures();
	}
	
	public void computeFeatures() {
		
	}
}
