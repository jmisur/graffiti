package org.graffiti.grafroid.sensor;

import java.util.ArrayList;
import java.util.List;

import org.graffiti.grafroid.AccelerationMotionEventListener;

import android.util.Log;

public class SensorDataProcessor {
	private final static String LOG_TAG = SensorDataProcessor.class
			.getSimpleName();

	private List<Point> mXPoints = new ArrayList<Point>();
	private List<Point> mYPoints = new ArrayList<Point>();
	private List<Point> mZPoints = new ArrayList<Point>();

	SensorWindow mWindow = new SensorWindow(10);

	private AccelerationMotionEventListener mListener;

	public SensorDataProcessor(AccelerationMotionEventListener listener) {
		this.mListener = listener;
	}

	private boolean[] mIsMoving = new boolean[3];

	public void process(float[] data, long time) {
		mWindow.addData(data);
		boolean[] moving = mWindow.isMoving();
		for (int i = 0; i < 3; i++) {

			if (mIsMoving[i] != moving[i]) {
				if (mIsMoving[i]) {
					// motion stopped
					mIsMoving[i] = false;
					switch (i) {
					case 0:
						findPeaks(mXPoints, i);
						mXPoints.clear();
						break;
					case 1:
						findPeaks(mYPoints, i);
						mXPoints.clear();
						break;
					case 2:
						findPeaks(mZPoints, i);
						mXPoints.clear();
						break;

					}

				} else if (!mIsMoving[i]) {
					// motion started
					mIsMoving[i] = true;
				}
			}
			if (mIsMoving[i]) {
				mXPoints.add(new Point(time, data[0]));
				mYPoints.add(new Point(time, data[0]));
				mZPoints.add(new Point(time, data[0]));
			}
		}
	}

	private void findPeaks(List<Point> points, int i) {
		ExtremaFinder finder = new ExtremaFinder(points);
		List<Point> extrema = finder.getExtrema();
		if (extrema.size() > 0) {
			Log.i(LOG_TAG, i + " = " + extrema.size());
		}

	}
}
