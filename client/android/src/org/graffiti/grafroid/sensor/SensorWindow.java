package org.graffiti.grafroid.sensor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.util.Log;

class SensorWindow {
	private static final String LOG_TAG = SensorWindow.class.getSimpleName();
	private static final double START_THRESHOLD = 4;
	private static final double STOP_THRESHOLD = 2;

	private final static boolean sDebug = false;

	private final int mStackSize;
	private LinkedList<SensorPoint> mValues;

	private boolean mFull = false;

	private boolean mMoving = false;

	public SensorWindow(int stackSize) {
		mStackSize = stackSize;
		mValues = new LinkedList<SensorPoint>();
	}

	public List<SensorPoint> createList() {
		List<SensorPoint> result = new ArrayList<SensorPoint>(mValues);
		return result;
	}

	public void addData(float value, long timestamp) {
		mValues.add(new SensorPoint(timestamp, value));
		if (mValues.size() > mStackSize) {
			mValues.removeFirst();
			mFull = true;
		}
		if (mFull) {
			double stds = getStdDeviation(mValues);
			if (sDebug) {
				String log = String.format("%1$.2f", stds);
				Log.i(LOG_TAG, log);
			}
			if (stds > START_THRESHOLD) {
				mMoving = true;
			} else if (stds < STOP_THRESHOLD) {
				mMoving = false;
			}

		}
	}

	private double getStdDeviation(LinkedList<SensorPoint> values) {
		// Get mean
		double sum = 0;

		// int max = 0;
		for (int i = 0; i < mStackSize; i++) {
			sum += values.get(i).mValue;
		}

		double mean = sum / (mStackSize);

		double sumdev = 0.0f;
		double deviation = 0;
		for (int i = 0; i < mStackSize; i++) {
			deviation = mValues.get(i).mValue - mean;
			sumdev += (deviation * deviation);
		}
		double variance = (sumdev / (mStackSize - 1));
		double result = Math.sqrt(variance);
		return result;
	}

	public boolean isMoving() {
		return mMoving;
	}

	public void reset() {
		mMoving = false;
		mValues.clear();
	}

}
