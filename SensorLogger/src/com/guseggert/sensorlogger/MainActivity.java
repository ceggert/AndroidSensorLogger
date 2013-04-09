package com.guseggert.sensorlogger;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity {
	public final static int MSG_SENSOR_UPDATE = 0;
	private SensorManager mSensorManager;
	private SensorLogger mSensorLogger;
	private UIUpdater mUIUpdater;
		
	// Handles messages from the logger to update the UI
	// suppresses the leak warnings since there are no long delayed messages
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@SuppressLint("HandlerLeak")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_SENSOR_UPDATE:
				mUIUpdater.updateValues((float[])msg.obj, msg.arg1);
				break;
			default:
				Log.e("MainActivity", "handleMessage received an invalid msg.what");
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void init() {
		initSensorLogger();
		mUIUpdater = new UIUpdater(this);
	}
	
	private void initSensorLogger() {
		mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
		mSensorLogger = SensorLogger.getInstance(mSensorManager, mHandler);
	}
	
	public SensorLogger getSensorLogger() {
		return mSensorLogger;
	}
}
