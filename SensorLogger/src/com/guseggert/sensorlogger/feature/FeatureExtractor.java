package com.guseggert.sensorlogger.feature;

import com.guseggert.sensorlogger.data.TimeSeries;

public abstract class FeatureExtractor {
	protected TimeSeries mTimeSeries;
	protected FeatureSet mFeatureSet;

	public FeatureExtractor(TimeSeries timeSeries) {
		mTimeSeries = timeSeries;
	}
}
	