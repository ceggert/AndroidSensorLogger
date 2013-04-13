package com.guseggert.sensorlogger.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class TimeSeries extends ArrayList <DataPoint> {	

	private static final long serialVersionUID = 7631932145097190500L;
	
	public TimeSeries() {
	}

	public boolean addDataPoint(DataPoint dp) {
		if (dp != null && !this.contains(dp)) {
			return super.add(dp);
		} else {
			/** Overwrite? or not overwrite?*/
			return false;
		}		
	}
	
	public void sort() {
		Collections.sort(this);
	}

	public void addPoints(Collection<DataPoint> points) {
		for (DataPoint dp : points) {
			addDataPoint(dp);
		}
		Collections.sort(this);
	}	

	public void addPoints(DataPoint[] points) {
		for (DataPoint dp : points) {
			addDataPoint(dp);
		}
		Collections.sort(this);
	}	
}