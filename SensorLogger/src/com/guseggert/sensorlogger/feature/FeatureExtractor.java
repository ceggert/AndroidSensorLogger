package com.guseggert.sensorlogger.feature;

import java.util.ArrayList;

import android.util.Log;
import android.util.SparseArray;

import com.guseggert.sensorlogger.data.DataPoint;

public class FeatureExtractor {
	
	private SparseArrayIterable<Feature> mFeatures;
	private SparseArrayIterable<ArrayList<DataPoint>> mData;
	private ArrayList<Short> mFeaturesToCalculate;
	
	public FeatureExtractor(ArrayList<Short> featuresToCalc, SparseArray<ArrayList<DataPoint>> sensorData) {
		mFeatures = new SparseArrayIterable<Feature>();
		mFeaturesToCalculate = featuresToCalc;
		mData = new SparseArrayIterable<ArrayList<DataPoint>>(sensorData);
	}
	
	public void computeFeatures() {
		prepareFeatures();
		for (Feature feature : mFeatures) {
			feature.compute();
			Mean mean = new Mean(mData);
			mean.compute();
			mFeatures.append(FeatureID.MEAN, mean);
			
//			for (ArrayList<DataPoint> dp : mData) {
//				Mean mean = new Mean();
//				mean.compute();
//				mFeatures.append(FeatureID.MEAN, new Mean(dp));
//			}
		}
	}
	
	private void prepareFeatures() {
		for (short i : mFeaturesToCalculate) {
			switch(i) {
			case FeatureID.MEAN:
				mFeatures.append(i, new Mean(mData));
				break;
			case FeatureID.STDEV:
				//mFeatures.append(i, new StandardDeviation(mData.get(i)));
				break;
			default:
				break;
			}
		}
	}
}
