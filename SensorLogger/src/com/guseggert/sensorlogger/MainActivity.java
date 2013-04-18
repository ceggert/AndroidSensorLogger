package com.guseggert.sensorlogger;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.Menu;
import android.view.WindowManager;

// suppresses the leak warnings since there are no long delayed messages
@SuppressLint("HandlerLeak")

public class MainActivity extends Activity {
	public final static int MSG_SENSOR_UPDATE = 0;
	private SensorManager mSensorManager;
	private SensorLogger mSensorLogger;
	private UIUpdater mUIUpdater;
	private WakeLock mWakeLock;
		
	// Handles messages from the ui updater to update the UI
	private Handler mHandler = new Handler() {
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
	
	@SuppressLint("HandlerLeak")
	private void init() {
		initSensorLogger();
		mUIUpdater = new UIUpdater(this);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // keep the screen on
	}
	
	private void initSensorLogger() {
		mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
		mSensorLogger = SensorLogger.getInstance(mSensorManager, mHandler);
	}
	
	public SensorLogger getSensorLogger() {
		return mSensorLogger;
	}
}
