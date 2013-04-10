package com.guseggert.sensorlogger.feature;

import java.util.ArrayList;

import android.util.Log;

import com.guseggert.sensorlogger.data.DataPoint;

public class Mean extends Feature {
	private float meanX = 0f, meanY = 0f, meanZ = 0f;
	
	public Mean(ArrayList<DataPoint> dataPoints) {
		super(dataPoints);
	}
	
	public void compute () {
		double sumX = 0f, sumY = 0f, sumZ = 0f;

		for(DataPoint dp : mDataPoints) {
			float values[] = dp.getValues();
			sumX += values[0];
			sumY += values[1];
			sumZ += values[2];
		}
		meanX = (float) (sumX / mDataPoints.size());
		meanY = (float) (sumY / mDataPoints.size());
		meanZ = (float) (sumZ / mDataPoints.size());
		
		Log.v("FeatureExtractor", "MeanX: " + meanX + 
				  "MeanY: " + meanY +
				  "MeanZ: " + meanZ);

	}
	
	public float getMeanX() {
		return meanX;
	}
	
	public float getMeanY() {
		return meanY;
	}
	
	public float getMeanZ() {
		return meanZ;
	}
}
