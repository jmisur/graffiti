package org.graffiti.grafroid.sensor;

import com.google.gson.annotations.SerializedName;

import java.util.Locale;

public class SensorPoint {
    @SerializedName("timestamp")
    public long   mTimeStamp;
    @SerializedName("value")
    public double mValue;
    
    public SensorPoint(final long timeStamp, final double value) {
        this.mTimeStamp = timeStamp;
        this.mValue = value;
    }
    
    @Override
    public String toString() {
        return String.format(Locale.US, "(val: %1$.0f, time: %2$d)", mValue, mTimeStamp);
    }
    
}
