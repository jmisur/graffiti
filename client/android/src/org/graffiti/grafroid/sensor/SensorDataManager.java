package org.graffiti.grafroid.sensor;

import java.util.List;

import org.graffiti.grafroid.AccelerationMotionEventListener;

import roboguice.inject.ContextSingleton;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.google.inject.Inject;

@ContextSingleton
public class SensorDataManager implements SensorEventListener {

	private final SensorManager mSensorManager;

	private Sensor mLinearAccelerometerSensor;
	private long mStartRecordTimestamp = -1;
	private SensorDataProcessor mProcessor;

	private AveragingFilter mFilter = new AveragingFilter(5);

	@Inject
	public SensorDataManager(final SensorManager sensorManager) {
		mSensorManager = sensorManager;
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

	public interface DebugDataListener {
		void onDebugData(List<SensorPoint> points, List<SensorPoint> extrema);
	}

	public void setDebugDataListener(DebugDataListener debugListener) {
		this.mProcessor.setDebugDataListener(debugListener);
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
