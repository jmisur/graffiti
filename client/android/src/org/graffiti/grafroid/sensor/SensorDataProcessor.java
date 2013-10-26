package org.graffiti.grafroid.sensor;

import java.util.ArrayList;
import java.util.List;

import org.graffiti.grafroid.AccelerationMotionEventListener;

public class SensorDataProcessor {

	private List<Point>  mXPoints = new ArrayList<Point>();
	private List<Point>  mYPoints = new ArrayList<Point>();
	private List<Point>  mZPoints = new ArrayList<Point>();
	
	private AccelerationMotionEventListener mListener;

	public SensorDataProcessor(AccelerationMotionEventListener listener) {
		this.mListener = listener;
	}
	
	public void stop(){
		ExtremaFinder finder = new ExtremaFinder(mXPoints);
		List<Point> extrema = finder.getExtrema();
		//TODO feed listener with data
		mXPoints.clear();
		mYPoints.clear();
		mZPoints.clear();
	}

	public void process(float[] data, long time) {
		//TODO listen when motion starts
		mXPoints.add(new Point(time, data[0]));
		mYPoints.add(new Point(time, data[0]));
		mZPoints.add(new Point(time, data[0]));
	}
}
