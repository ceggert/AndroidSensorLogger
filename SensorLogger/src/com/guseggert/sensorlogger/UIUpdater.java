package com.guseggert.sensorlogger;

import android.hardware.Sensor;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class UIUpdater {

	private MainActivity mActivity;
	private SensorLogger mSensorLogger;
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
	
	public UIUpdater(final MainActivity act) {
		mActivity = act;
		mSensorLogger = mActivity.getSensorLogger();
		initUIObjs();
		initActivitySpinner();
		initStartButton();
	}
	
	private void initUIObjs() {
		mTextAccX = (TextView)mActivity.findViewById(R.id.acc_x_value);
		mTextAccY = (TextView)mActivity.findViewById(R.id.acc_y_value);
		mTextAccZ = (TextView)mActivity.findViewById(R.id.acc_z_value);
		mTextGyroX = (TextView)mActivity.findViewById(R.id.gyro_x_value);
		mTextGyroY = (TextView)mActivity.findViewById(R.id.gyro_y_value);
		mTextGyroZ = (TextView)mActivity.findViewById(R.id.gyro_z_value);
		mTextGravX = (TextView)mActivity.findViewById(R.id.grav_x_value);
		mTextGravY = (TextView)mActivity.findViewById(R.id.grav_y_value);
		mTextGravZ = (TextView)mActivity.findViewById(R.id.grav_z_value);
		mTextLinAccX = (TextView)mActivity.findViewById(R.id.linacc_x_value);
		mTextLinAccY = (TextView)mActivity.findViewById(R.id.linacc_y_value);
		mTextLinAccZ = (TextView)mActivity.findViewById(R.id.linacc_z_value);
		mTextRotVecX = (TextView)mActivity.findViewById(R.id.rotvec_x_value);
		mTextRotVecY = (TextView)mActivity.findViewById(R.id.rotvec_y_value);
		mTextRotVecZ = (TextView)mActivity.findViewById(R.id.rotvec_z_value);
	}
	
	private void initActivitySpinner() {
		Spinner spinner = (Spinner) mActivity.findViewById(R.id.spn_activity);
		ArrayAdapter<CharSequence> adapter = 
				ArrayAdapter.createFromResource(mActivity, R.array.activities_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
	}
	
	private void initStartButton() {
		mButtonStart = (Button)mActivity.findViewById(R.id.btn_start);
		mButtonStart.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				onStartButtonClick();
			}
		});
	}
	
	private void onStartButtonClick() {
		if (mButtonStart.getText().equals("Start")) {
			mButtonStart.setText("Stop");
			mSensorLogger.start();
		}
		else {
			mButtonStart.setText("Start");
			mSensorLogger.stop();
		}
	}
	
	public void updateValues(final float[] values, final int type) {
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
