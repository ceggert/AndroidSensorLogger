package com.guseggert.sensorlogger.feature;

import java.util.Collections;

import com.guseggert.sensorlogger.data.DataPoint;
import com.guseggert.sensorlogger.data.TimeSeries;

public class StatisticalFeatureExtractor extends FeatureExtractor {
	private double mMean = 0.0;
	private double mSumSquared = 0.0;
	private double mSumMad = 0.0;

	
	public StatisticalFeatureExtractor(TimeSeries timeSeries) {
		super(timeSeries);
	}

	public void computeFeatures() {
	}

	public double getMean() {
		prepareFeatures();
		return mMean;
	}
	
	/** This method is used to perform pre-computation calculations */
	private void prepareFeatures() {
		for (DataPoint dp : mTimeSeries) {
			mMean += dp.getValue();
			mSumSquared += Math.pow(dp.getValue(), 2);
		}
		mMean = mMean/mTimeSeries.size();
		for (DataPoint dp : mTimeSeries) {
			mSumMad += Math.abs(dp.getValue()-mMean);
		}
	}
	
	/** Standard Deviation */
	public double computeSTDV() {
		return Math.sqrt(computeVariance());
	}
	
	/** This method uses a Fourier Transformation to calculate the
	 *    energy of the inherited TimeSeries */
	public double computeEnergy() {
		Complex[] fourian = new Complex[mTimeSeries.size()];
		int i = 0;
		// Put each point in the series into an of Complex values
		for (DataPoint dp : mTimeSeries) {
			fourian[i++] = new Complex(dp.getValue(), 0);
		}
		// 
		fourian = powTwo(fourian);
		fourian = FFT(fourian);
		int halfSize = fourian.length/2;
		double[] energyList = new double[halfSize];
		for (i = 0; i < halfSize; i++) {
			energyList[i] = fourian[i].abs()/mTimeSeries.size();
		}
		
		double energySum = 0.0;
		for (double d : energyList) {
			energySum += d*d;
		}
		return (energySum/halfSize);
	}
	
	private Complex[] powTwo (Complex[] list) {
		double power = (Math.log10(list.length)/Math.log10(2));
		if (power % 1.0 == 0.0) {
			return list;
		}
		Complex[] newList = new Complex[(int)(Math.pow(2, (int)(power)+1))];
		for (int i = 0; i < newList.length; i++) {
			if (i < list.length) {
				newList[i] = list[i];
			}
			else {
				newList[i] = new Complex(0, 0);
			}
		}
		return newList;
	}
	
	/** This method performs a Fast Fourian Transformation on a given
	 *    set of complex numbers. */
	private Complex[] FFT(Complex[] x) {
        int N = x.length;

        if (N == 0) return new Complex[] {} ;
        // base case
        if (N == 1) return new Complex[] { x[0] };

        // radix 2 Cooley-Tukey FFT
        if (N % 2 != 0) { 
        	throw new RuntimeException("N is not a power of 2");
        }

        // fft of even terms
        Complex[] even = new Complex[N/2];
        for (int k = 0; k < N/2; k++) {
            even[k] = x[2*k];
        }
        Complex[] q = FFT(even);

        // fft of odd terms
        Complex[] odd  = even;  // reuse the array
        for (int k = 0; k < N/2; k++) {
            odd[k] = x[2*k + 1];
        }
        Complex[] r = FFT(odd);

        // combine
        Complex[] y = new Complex[N];
        for (int k = 0; k < N/2; k++) {
            double kth = -2 * k * Math.PI / N;
            Complex wk = new Complex(Math.cos(kth), Math.sin(kth));
            y[k]       = q[k].plus(wk.times(r[k]));
            y[k + N/2] = q[k].minus(wk.times(r[k]));
        }
        return y;
    }
	
	/** Root Mean Square */
	public double computeRMS() {
		return Math.sqrt(mSumSquared/mTimeSeries.size());
	}
	
	/** Inter-Quartile Range */
	public double computeIQR() {
		double[] quartiles = Quartiles();
		return quartiles[2] - quartiles[0];
	}
	
	/** This method calculates the quartiles of the inherited TimeSeries 
	 *  It isn't designed to handle lists with fewer than 3 elements.*/
	private double[] Quartiles() {
	    if (mTimeSeries.size() < 3)
	    	return new double[] {0.0, 0.0, 0.0};
	 
	    double median = Median(mTimeSeries);
	    double first = 0.0;
	    double third = 0.0;
	 
	    TimeSeries lowerHalf = GetValuesLessThan(median);
	    TimeSeries upperHalf = GetValuesGreaterThan(median);
	    
	    if (lowerHalf.size() == 0) {
	    	first = median;
	    }
	    else {
	    	first = Median(lowerHalf);
	    }
	    if (upperHalf.size() == 0) {
	    	third = median;
	    }
	    else {
	    	third = Median(upperHalf);
	    }
	    
	    return new double[] {first, median, third};
	}
	
	/** This method is used to calculate the Third Quartile */
	private TimeSeries GetValuesGreaterThan(double limit) {
	    TimeSeries modValues = new TimeSeries();
	 
	    for (DataPoint dp : mTimeSeries)
	        if (dp.getValue() > limit)
	            modValues.addPoints(new DataPoint[] {dp});
	 
	    return modValues;
	}
	
	/** This method is used to calculate the First Quartile */
	private TimeSeries GetValuesLessThan(double limit) {
	    TimeSeries modValues = new TimeSeries();
	 
	    for (DataPoint dp : mTimeSeries)
	        if (dp.getValue() < limit)
	            modValues.addPoints(new DataPoint[] {dp});
	 
	    return modValues;
	}
	
	/** This method computes the Median of a given TimeSeries */
	private double Median(TimeSeries series) {
	    Collections.sort(series);
	 
	    if (series.size() % 2 == 1)
	    	return series.get((series.size()+1)/2-1).getValue();
	    else
	    {
			double lower = series.get(series.size()/2-1).getValue();
			double upper = series.get(series.size()/2).getValue();
		 
			return (lower + upper) / 2.0;
	    }	
	}
	
	/** Mean Absolute Deviation */
	public double computeMAD() {		
		return (mSumMad/mTimeSeries.size());
	}

	/** Two-Pass Variance */
	public double computeVariance() {
		double sumVar = 0.0;
		for (DataPoint dp : mTimeSeries) {
			sumVar += Math.pow(dp.getValue()-mMean, 2);
		}
		return sumVar/(mTimeSeries.size() - 1);
	}
}

