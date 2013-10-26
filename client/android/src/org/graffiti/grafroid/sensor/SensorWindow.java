package org.graffiti.grafroid.sensor;

import android.util.Log;

class SensorWindow {
	private static final String LOG_TAG = SensorWindow.class.getSimpleName();
	private static final double START_THRESHOLD = 5;
	private static final double STOP_THRESHOLD = 2;

	private final static boolean sDebug = false;

	private final int mStackSize;
	private int mIndex = 0;
	private final float[][] mValues;

	private boolean mFull = false;

	private boolean[] mMoving = new boolean[3];

	public SensorWindow(int stackSize) {
		mStackSize = stackSize;
		mValues = new float[mStackSize][3];
	}

	public void addData(float[] values) {
		System.arraycopy(values, 0, mValues[mIndex++], 0, 3);
		if (mIndex >= mStackSize) {
			mIndex = 0;
			mFull = true;
		}
		if (mFull) {
			double[] stds = getStdDeviation(mValues);
			if (sDebug) {
				String log = String.format("%1$.2f, %2$.2f, %3.2f", stds[0],
						stds[1], stds[2]);
				Log.i(LOG_TAG, log);
			}
			for (int i = 0; i < 3; i++) {
				if (stds[i] > START_THRESHOLD) {
					mMoving[i] = true;
				} else if (stds[i]<STOP_THRESHOLD){
					mMoving[i] = false;
				}
			}

		}
	}

	private double[] getStdDeviation(float[][] values) {
		// Get mean
		double sumX = 0;
		double sumY = 0;
		double sumZ = 0;

		// int max = 0;
		for (int i = 0; i < mStackSize; i++) {
			sumX += values[i][0];
			sumY += values[i][1];
			sumZ += values[i][2];
		}

		double meanX = sumX / (mStackSize);
		double meanY = sumY / (mStackSize);
		double meanZ = sumZ / (mStackSize);

		double sumdevX = 0.0f;
		double sumdevY = 0.0f;
		double sumdevZ = 0.0f;
		double deviationX = 0;
		double deviationY = 0;
		double deviationZ = 0;
		for (int i = 0; i < mStackSize; i++) {
			deviationX = mValues[i][0] - meanX;
			deviationY = mValues[i][1] - meanY;
			deviationZ = mValues[i][2] - meanZ;
			sumdevX += (deviationX * deviationX);
			sumdevY += (deviationY * deviationY);
			sumdevZ += (deviationZ * deviationZ);
		}
		double varianceX = (sumdevX / (mStackSize - 1));
		double resultX = Math.sqrt(varianceX);

		double varianceY = (sumdevY / (mStackSize - 1));
		double resultY = Math.sqrt(varianceY);

		double varianceZ = (sumdevZ / (mStackSize - 1));
		double resultZ = Math.sqrt(varianceZ);

		return new double[] { resultX, resultY, resultZ };
	}

	public boolean[] isMoving() {
		return mMoving;
	}

	public void reset() {
		for (int i = 0; i < 3; i++) {
			mMoving[i] = false;
		}
		for (int i = 0; i < mValues.length; i++) {
			for (int j = 0; j < 3; j++) {
				mValues[i][j] = 0;
			}
		}
	}

}
