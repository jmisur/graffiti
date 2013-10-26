package org.graffiti.grafroid.sensor;

import org.graffiti.grafroid.AccelerationMotionEventListener;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class SensorDataManager implements SensorEventListener {
	private SensorManager mSensorManager;
	private Sensor mLinearAccelerometerSensor;
	private long mStartRecordTimestamp = -1;
	private SensorDataProcessor mProcessor;

	private AveragingFilter mFilter = new AveragingFilter(5);

	public SensorDataManager(Context c) {
		mSensorManager = (SensorManager) c
				.getSystemService(Context.SENSOR_SERVICE);
		mLinearAccelerometerSensor = mSensorManager
				.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

	}

	public void startRecording(AccelerationMotionEventListener listener) {
		mProcessor = new SensorDataProcessor(listener);
		mSensorManager.registerListener(this, mLinearAccelerometerSensor,
				SensorManager.SENSOR_DELAY_FASTEST);

	}

	public void stopRecording() {
		mSensorManager.unregisterListener(this);
		mProcessor.stop();

	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	@Override
	public void onSensorChanged(SensorEvent event) {

		long timestamp = event.timestamp / 1000000L;

		switch (event.sensor.getType()) {
		case Sensor.TYPE_LINEAR_ACCELERATION:
			mFilter.addData(event.values);
			break;
		}

		if (mStartRecordTimestamp == -1) {
			mStartRecordTimestamp = timestamp;
		}

		final long t = (timestamp - mStartRecordTimestamp);
		mProcessor.process(event.values, t);

	}

}
