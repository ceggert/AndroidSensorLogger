package com.guseggert.sensorlogger.data;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class CSVWriter {
	final String FILENAME = Long.toString(System.currentTimeMillis()) + ".csv";
	boolean mFirstLine = true;
	String mFeat
	
	public CSVWriter() {
	}
	
	public void writeToCSV(TimeWindow timeWindow) {
		if (firstLine()) writeFirstLine(timeWindow);
		else write(buildLine(timeWindow));
	}
	
	private String buildLine(TimeWindow timeWindow) {
		
	}
	
	private void writeFirstLine(TimeWindow timeWindow) {
		
	}
	
	private boolean firstLine() {
		mFirstLine = !mFirstLine;
		return !mFirstLine;
	}
	
	private void write(String string) {
		try { 
			FileWriter fileWriter = new FileWriter(FILENAME, true); 
			BufferedWriter writer = new BufferedWriter(fileWriter);
			writer.write(string);
			writer.close();
		} catch (Exception e) { e.printStackTrace(); }
	}
}
