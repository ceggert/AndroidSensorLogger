package com.guseggert.sensorlogger;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {

	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	private Sensor mGyroscope;
	private Sensor mGravity;
	private Sensor mLinAccel;
	private Sensor mRotVec;
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		initAccelerometer();
		initGyroscope();		
        initGravity();
        initLinearAccel();
        initRotationVector();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			mTextAccX.setText(Float.toString(event.values[0]));
			mTextAccY.setText(Float.toString(event.values[1]));
			mTextAccZ.setText(Float.toString(event.values[2]));
		}
		else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
			mTextGyroX.setText(Float.toString(event.values[0]));
			mTextGyroY.setText(Float.toString(event.values[1]));
			mTextGyroZ.setText(Float.toString(event.values[2]));
		}
		else if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
			mTextLinAccX.setText(Float.toString(event.values[0]));
			mTextLinAccY.setText(Float.toString(event.values[1]));
			mTextLinAccZ.setText(Float.toString(event.values[2]));
		}
		else if (event.sensor.getType() == Sensor.TYPE_GRAVITY) {
			mTextGravX.setText(Float.toString(event.values[0]));
			mTextGravY.setText(Float.toString(event.values[1]));
			mTextGravZ.setText(Float.toString(event.values[2]));
		}
		else if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
			mTextRotVecX.setText(Float.toString(event.values[0]));
			mTextRotVecY.setText(Float.toString(event.values[1]));
			mTextRotVecZ.setText(Float.toString(event.values[2]));
		}
		return;
	}

	@Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
	
	private void initAccelerometer() {
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);
		mTextAccX = (TextView)findViewById(R.id.acc_x_value);
		mTextAccY = (TextView)findViewById(R.id.acc_y_value);
		mTextAccZ = (TextView)findViewById(R.id.acc_z_value);
	}
	
	private void initGyroscope() {
		mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
		mSensorManager.registerListener(this,  mGyroscope, SensorManager.SENSOR_DELAY_FASTEST);
		mTextGyroX = (TextView)findViewById(R.id.gyro_x_value);
		mTextGyroY = (TextView)findViewById(R.id.gyro_y_value);
		mTextGyroZ = (TextView)findViewById(R.id.gyro_z_value);
	}
	
	
	
	private void initGravity() {
		mGravity = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
		mSensorManager.registerListener(this, mGravity, SensorManager.SENSOR_DELAY_FASTEST);
		mTextGravX = (TextView)findViewById(R.id.grav_x_value);
		mTextGravY = (TextView)findViewById(R.id.grav_y_value);
		mTextGravZ = (TextView)findViewById(R.id.grav_z_value);
	}
	
	private void initLinearAccel() {
		mLinAccel = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
		mSensorManager.registerListener(this, mLinAccel, SensorManager.SENSOR_DELAY_FASTEST);
		mTextLinAccX = (TextView)findViewById(R.id.linacc_x_value);
		mTextLinAccY = (TextView)findViewById(R.id.linacc_y_value);
		mTextLinAccZ = (TextView)findViewById(R.id.linacc_z_value);
	}
	
	private void initRotationVector() {
		mRotVec = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
		mSensorManager.registerListener(this, mRotVec, SensorManager.SENSOR_DELAY_FASTEST);
		mTextRotVecX = (TextView)findViewById(R.id.rotvec_x_value);
		mTextRotVecY = (TextView)findViewById(R.id.rotvec_y_value);
		mTextRotVecZ = (TextView)findViewById(R.id.rotvec_z_value);
	}
	
}
