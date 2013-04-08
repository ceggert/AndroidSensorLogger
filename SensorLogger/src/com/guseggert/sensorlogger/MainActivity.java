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

	private TextView mTextAccX;
	private TextView mTextAccY;
	private TextView mTextAccZ;
	private TextView mTextGyroX;
	private TextView mTextGyroY;
	private TextView mTextGyroZ;
	private TextView mTextGravX;
	private TextView mTextGravY;
	private TextView mTextGravZ;
	private TextView mTextLinAccX;
	private TextView mTextLinAccY;
	private TextView mTextLinAccZ;
	private TextView mTextRotVecX;
	private TextView mTextRotVecY;
	private TextView mTextRotVecZ;
	private Button mButtonStart;
	private SensorManager mSensorManager;
	private SensorLogger mSensorLogger;
	
	public final static int MSG_SENSOR_UPDATE = 0;
	
	// Handles messages from the logger to update the UI
	// suppresses the leak warnings since there are no long delayed messages
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@SuppressLint("HandlerLeak")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_SENSOR_UPDATE:
				updateValues((float[])msg.obj, msg.arg1);
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
		initUIObjs();
		initSensorLogger();
		initActivitySpinner();
		initStartButton();
	}
	
	private void initSensorLogger() {
		mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
		mSensorLogger = SensorLogger.getInstance(mSensorManager, mHandler);
	}
	
	private void initUIObjs() {
		mTextAccX = (TextView)findViewById(R.id.acc_x_value);
		mTextAccY = (TextView)findViewById(R.id.acc_y_value);
		mTextAccZ = (TextView)findViewById(R.id.acc_z_value);
		mTextGyroX = (TextView)findViewById(R.id.gyro_x_value);
		mTextGyroY = (TextView)findViewById(R.id.gyro_y_value);
		mTextGyroZ = (TextView)findViewById(R.id.gyro_z_value);
		mTextGravX = (TextView)findViewById(R.id.grav_x_value);
		mTextGravY = (TextView)findViewById(R.id.grav_y_value);
		mTextGravZ = (TextView)findViewById(R.id.grav_z_value);
		mTextLinAccX = (TextView)findViewById(R.id.linacc_x_value);
		mTextLinAccY = (TextView)findViewById(R.id.linacc_y_value);
		mTextLinAccZ = (TextView)findViewById(R.id.linacc_z_value);
		mTextRotVecX = (TextView)findViewById(R.id.rotvec_x_value);
		mTextRotVecY = (TextView)findViewById(R.id.rotvec_y_value);
		mTextRotVecZ = (TextView)findViewById(R.id.rotvec_z_value);
	}
	
	private void initActivitySpinner() {
		Spinner spinner = (Spinner) findViewById(R.id.spn_activity);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.activities_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
	}
	
	private void initStartButton() {
		mButtonStart = (Button)findViewById(R.id.btn_start);
	}
	
	public void onStartButtonClick(View view) {
		if (mButtonStart.getText().equals("Start")) {
			mButtonStart.setText("Stop");
			mSensorLogger.start();
		}
		else {
			mButtonStart.setText("Start");
			mSensorLogger.stop();
		}
	}
	
	private void updateValues(float[] values, int type) {
		switch (type) {
		case Sensor.TYPE_ACCELEROMETER:
			mTextAccX.setText(Float.toString(values[0]));
			mTextAccY.setText(Float.toString(values[1]));
			mTextAccZ.setText(Float.toString(values[2]));
			break;
		case Sensor.TYPE_GRAVITY:
			mTextGravX.setText(Float.toString(values[0]));
			mTextGravY.setText(Float.toString(values[1]));
			mTextGravZ.setText(Float.toString(values[2]));
			break;
		case Sensor.TYPE_GYROSCOPE:
			mTextGyroX.setText(Float.toString(values[0]));
			mTextGyroY.setText(Float.toString(values[1]));
			mTextGyroZ.setText(Float.toString(values[2]));
			break;
		case Sensor.TYPE_LINEAR_ACCELERATION:
			mTextLinAccX.setText(Float.toString(values[0]));
			mTextLinAccY.setText(Float.toString(values[1]));
			mTextLinAccZ.setText(Float.toString(values[2]));
			break;
		case Sensor.TYPE_ROTATION_VECTOR:
			mTextRotVecX.setText(Float.toString(values[0]));
			mTextRotVecY.setText(Float.toString(values[1]));
			mTextRotVecZ.setText(Float.toString(values[2]));
			break;
		default:
			Log.e("MainActivity", "MainActivity.updateValues() received an invalid sensor type");
			break;
		}
	}
}
