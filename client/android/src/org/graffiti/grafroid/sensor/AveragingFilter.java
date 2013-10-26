package org.graffiti.grafroid.sensor;

public class AveragingFilter {
	// number of values which are averaged
	private final int mStackSize;
	private int mIndex = 0;
	private final float[][] mValues;
	private final float[] mFilteredValues = new float[3];

	public AveragingFilter(int stackSize) {
		mStackSize = stackSize;
		mValues = new float[mStackSize][3];
	}

	public void addData(float[] values) {
		System.arraycopy(values, 0, mValues[mIndex++], 0, 3);
		if (mIndex >= mStackSize) {
			mIndex = 0;
		}
		average(mValues, mFilteredValues);
	}

	public float[] getFilteredValues() {
		return mFilteredValues;
	}

	private void average(float[][] values, float[] outAverage) {
		outAverage[0] = 0;
		outAverage[1] = 0;
		outAverage[2] = 0;
		for (int i = 0; i < values.length; i++) {
			for (int j = 0; j < 3; j++) {
				outAverage[j] += values[i][j];
			}
		}
		outAverage[0] /= values.length;
		outAverage[1] /= values.length;
		outAverage[2] /= values.length;
	}

	public void reset() {
		for (int i = 0; i < mValues.length; i++) {
			for (int j = 0; j < 3; j++) {
				mValues[i][j] = 0;
			}
		}
	}

}
