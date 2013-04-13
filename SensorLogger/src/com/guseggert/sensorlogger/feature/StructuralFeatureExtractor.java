package com.guseggert.sensorlogger.feature;

import com.guseggert.sensorlogger.data.TimeSeries;

public class StructuralFeatureExtractor extends FeatureExtractor {
	private short[] mDegreeList = {1,2,3};
	
	public StructuralFeatureExtractor(TimeSeries timeSeries) {
		super(timeSeries);
	}
	
	/** This method computes the coefficients of the polynomial that best fits the time series
	 *  for all relevant degrees*/
	public FeatureSet computeFeatures() {
		for (short degree : mDegreeList) {
			double[] features = computeLeastSquares((degree+1));
			addToFeatureSet(features);
		}
		return mFeatureSet;
	}
	
	// add the computed features to the feature set
	private void addToFeatureSet(double[] features) {
	}
	
	/** This method computes the coefficients of the polynomial of the given degree
	 *    that best fits the time series.
	 *    
	 *    degreePoly represents the number of desired co-efficients; if desired degree
	 *    is x^2, degreePoly should be 3. (x^0, x^1, x^2)*/
	public double[] computeLeastSquares(int degreePoly) {
		
		int numOfPoints = mTimeSeries.size();
		
		if (degreePoly > numOfPoints) {
			double[] bigEquation = new double[degreePoly];
			double[] smallEquation = computeLeastSquares(degreePoly-1);
			for (int i = 0; i < degreePoly; i++) {
				if (i+1 == degreePoly) {
					bigEquation[i] = 0;
				} else {
					bigEquation[i] = smallEquation[i];
				}
			}
			return bigEquation;
		}
		
		Jama.Matrix mat = new Jama.Matrix(numOfPoints, degreePoly);
		Jama.Matrix bmat;
		
		double startTime = mTimeSeries.get(0).getTimestamp();		
		for (int j = 0; j < degreePoly; j++) {
			for (int i = 0; i < numOfPoints; i++) {
				mat.set(i, j, Math.pow((mTimeSeries.get(i).getTimestamp()-startTime), j));
			}
		}

		// Set B Matrix
		bmat = new Jama.Matrix(numOfPoints, 1);
		for (int i = 0; i < numOfPoints; i++) {
			bmat.set(i, 0, (mTimeSeries.get(i).getValue()));
		}
		try {
			return mat.solve(bmat).getRowPackedCopy();
		} catch(Exception e){
			System.out.println("Singular");
		}
		return null;
	}


}
