package org.graffiti.grafroid.sensor;

import java.util.ArrayList;
import java.util.List;

import org.graffiti.grafroid.AccelerationMotionEventListener;

import android.util.Log;

class SensorDataProcessor {
	private final static String LOG_TAG = SensorDataProcessor.class
			.getSimpleName();

	private List<SensorPoint> mXPoints = new ArrayList<SensorPoint>();
	private List<SensorPoint> mYPoints = new ArrayList<SensorPoint>();
	private List<SensorPoint> mZPoints = new ArrayList<SensorPoint>();

	SensorWindow mWindow = new SensorWindow(30);

	private AccelerationMotionEventListener mListener;

	public SensorDataProcessor(AccelerationMotionEventListener listener) {
		this.mListener = listener;
	}

	private boolean[] mIsMoving = new boolean[3];

	public void process(float[] data, long time) {
		mWindow.addData(data);
		boolean[] moving = mWindow.isMoving();
//		for (int i = 0; i < 3; i++) {
//			Log.i(LOG_TAG, i + " = " + moving[i]);
//		}
		for (int i = 0; i < 3; i++) {

			if (mIsMoving[i] != moving[i]) {
				if (mIsMoving[i]) {
					Log.i(LOG_TAG, "stopped " + i);
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
					Log.i(LOG_TAG, "started " + i);
					// motion started
					mIsMoving[i] = true;
				}
			}
			if (mIsMoving[i]) {
				mXPoints.add(new SensorPoint(time, data[0]));
				mYPoints.add(new SensorPoint(time, data[0]));
				mZPoints.add(new SensorPoint(time, data[0]));
			}
		}
	}

	private void findPeaks(List<SensorPoint> points, int index) {
		ExtremaFinder finder = new ExtremaFinder(points);
		List<SensorPoint> extrema = finder.getExtrema();
		if (extrema.size() > 0) {
			for (int i = 0; i < extrema.size() - 1; i++) {
				String log="";
				switch (index) {
				case 0:
					log = "X";
					mListener.onMotionDownX(extrema.get(i));
					break;
				case 1:
					log = "Y";
					mListener.onMotionDownY(extrema.get(i));
					break;
				case 2:
					log = "Z";
					mListener.onMotionDownZ(extrema.get(i));
					break;
				}
				
				if (extrema.get(i).mValue < 0) {
					Log.i(LOG_TAG, "DOWN " + log);
				} else {
					Log.i(LOG_TAG, "UP " + log);
				}

			}
		}

	}
}
