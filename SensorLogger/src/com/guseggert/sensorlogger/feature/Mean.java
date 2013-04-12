package com.guseggert.sensorlogger.feature;

import java.util.ArrayList;

import com.guseggert.sensorlogger.data.DataPoint;

public class Mean extends Feature {
	private SparseArrayIterable<Float[]> mMeans = new SparseArrayIterable<Float[]>();
	
	public Mean(SparseArrayIterable<ArrayList<DataPoint>> dataPoints) {
		super(dataPoints);
	}
	
	public void compute () {
		// for each sensor
		for (int i = 0; i < mDataPoints.size(); i++) {
			int sensorType = mDataPoints.keyAt(i);
			float[] sums = new float[]{0f, 0f, 0f};
			
			// for each data point of this sensor, sum up data points
			for(DataPoint dp : mDataPoints.get(mDataPoints.keyAt(i))) {
				float values[] = dp.getValues();
				sums[0] += values[0];
				sums[1] += values[1];
				sums[2] += values[2];
			}
			float[] means = new float[]{0f, 0f, 0f};
			
			// calculate the means by dividing by size
			for (int j = 0; j < 3; j++) 
				means[j] = sums[j] / mDataPoints.get(mDataPoints.keyAt(i)).size();
			
			mMeans.append(sensorType, new Float[3]);
			
//			Log.v("FeatureExtractor", "Sensor: " + mDataPoints.keyAt(i) + 
//					  " MeanX: " + means[0] + 
//					  " MeanY: " + means[1] +
//					  " MeanZ: " + means[2]);
		}
	}
	
	public float getMeanX(int sensorType) {
		return mMeans.get(sensorType)[0];
	}
	
	public float getMeanY(int sensorType) {
		return mMeans.get(sensorType)[1];
	}
	
	public float getMeanZ(int sensorType) {
		return mMeans.get(sensorType)[2];
	}
}
