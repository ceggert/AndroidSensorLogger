package com.guseggert.sensorlogger.feature;

import java.util.HashMap;
import java.util.Map;

import android.util.Log;

import com.guseggert.sensorlogger.SensorID;
import com.guseggert.sensorlogger.data.TimeSeries;
import com.guseggert.sensorlogger.data.TimeWindow;

public class FeatureSet extends HashMap<SensorID, HashMap<FeatureID, Float>> {
	private static final long serialVersionUID = 2504830754416754646L;
	private TimeWindow mTimeWindow;
	
	// calculates feature set from a time window
	public FeatureSet(TimeWindow timeWindow) {
		mTimeWindow = timeWindow;
	}
	
	public void computeFeatures() {
		// for each time series, compute the features
		for (Map.Entry<SensorID, TimeSeries> entry : mTimeWindow.entrySet()) {
			SensorID sensorID = entry.getKey();
			TimeSeries timeSeries = entry.getValue();
			this.put(sensorID, new HashMap<FeatureID, Float>());

			computeStatisticalFeatures(timeSeries, sensorID);
		}
	}
	
	private void computeStatisticalFeatures(TimeSeries timeSeries, SensorID sensorID) {
		StatisticalFeatureExtractor sfe = new StatisticalFeatureExtractor(timeSeries);
		this.get(sensorID).put(FeatureID.MEAN, (float)sfe.getMean());
	}
	
	private void computeStructuralFeatures() {
		
	}
	
	public TimeWindow getTimeWindow() {
		return mTimeWindow;
	}
	
	public void logContents() {
		for(Map.Entry<SensorID, HashMap<FeatureID, Float>> entry : this.entrySet()) {
			SensorID sensorID = entry.getKey();
			HashMap<FeatureID, Float> hm = entry.getValue();
			for (Map.Entry<FeatureID, Float> entry1 : hm.entrySet()) {
				FeatureID featureID = entry1.getKey();
				float value = entry1.getValue();
				Log.v("TimeWindowMaker", featureID + ": " + value + " Sensor: " + 
						sensorID + " Time: " + this.getTimeWindow().getStartTime());
			}
		}
	}
}
