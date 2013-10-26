package org.graffiti.grafroid.sensor;

import java.util.ArrayList;
import java.util.List;

import org.graffiti.grafroid.AccelerationMotionEventListener;

public class SensorDataProcessor {

	private List<Point>  mPoints = new ArrayList<Point>();
	private AccelerationMotionEventListener mListener;

	public SensorDataProcessor(AccelerationMotionEventListener listener) {
		this.mListener = listener;
	}
	
	public void stop(){
		
	}

	public void process(float[] data, long time) {
		//TODO check for start of motion and end of motion, then find peaks
		
		

	}
}
