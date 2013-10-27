package org.graffiti.grafroid;

import org.graffiti.grafroid.sensor.SensorPoint;

public interface AccelerationMotionEventListener {

    void onMotionStopX();
    void onMotionStopY();
    void onMotionStopZ();
    
	void onMotionDownX(SensorPoint p);

	void onMotionDownY(SensorPoint p);

	void onMotionDownZ(SensorPoint p);

}
