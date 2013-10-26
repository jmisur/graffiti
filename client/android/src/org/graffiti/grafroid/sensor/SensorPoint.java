package org.graffiti.grafroid.sensor;

public class SensorPoint {
	public final long mTimeStamp;
	public final double mValue;

	public SensorPoint(final long timeStamp, final double value) {
		this.mTimeStamp = timeStamp;
		this.mValue = value;
	}

	@Override
	public String toString() {
		return String.format("(%1$.0f, %2$d", mValue, mTimeStamp);
	}

}