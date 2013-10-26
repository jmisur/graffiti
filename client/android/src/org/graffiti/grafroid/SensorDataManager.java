package org.graffiti.grafroid;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class SensorDataManager implements SensorEventListener {
	private SensorManager mSensorManager;
	private Sensor mLinearAccelerometerSensor;
	private long mStartRecordTimestamp = -1;
	private AccelerationMotionEventListener mMotionListener;

	public SensorDataManager(Context c) {
		mSensorManager = (SensorManager) c
				.getSystemService(Context.SENSOR_SERVICE);
		mLinearAccelerometerSensor = mSensorManager
				.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

	}

	public void startRecording(AccelerationMotionEventListener listener) {
		this.mMotionListener = listener;
		mSensorManager.registerListener(this, mLinearAccelerometerSensor,
				SensorManager.SENSOR_DELAY_FASTEST);

	}

	public void stopRecording() {
		mSensorManager.unregisterListener(this);

	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (mStartRecordTimestamp == -1) {
			mStartRecordTimestamp = event.timestamp;
		}

		final long t = (event.timestamp - mStartRecordTimestamp);

		// TODO Auto-generated method stub

	}

}
