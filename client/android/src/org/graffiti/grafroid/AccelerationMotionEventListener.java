package org.graffiti.grafroid;

import org.graffiti.grafroid.sensor.SensorPoint;

public interface AccelerationMotionEventListener {

    void onMotionStopX(long timestamp);
    void onMotionStopY(long timestamp);
    void onMotionStopZ(long timestamp);
    
	void onMotionDownX(SensorPoint p);

	void onMotionDownY(SensorPoint p);

	void onMotionDownZ(SensorPoint p);
    void onMotionTotalStop();

}
