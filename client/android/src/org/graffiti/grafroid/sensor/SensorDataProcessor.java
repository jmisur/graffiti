package org.graffiti.grafroid.sensor;

import org.graffiti.grafroid.AccelerationMotionEventListener;

public class SensorDataProcessor {

	private AccelerationMotionEventListener mListener;

	public SensorDataProcessor(AccelerationMotionEventListener listener) {
		this.mListener = listener;
	}

	public void process(float[] data, long time) {

	}
}
