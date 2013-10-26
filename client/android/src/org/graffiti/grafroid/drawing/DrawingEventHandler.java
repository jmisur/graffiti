package org.graffiti.grafroid.drawing;

import com.google.inject.Inject;
import org.graffiti.grafroid.AccelerationMotionEventListener;
import org.graffiti.grafroid.sensor.SensorPoint;

/**
 * Handles drawing events as they happen.
 */
public class DrawingEventHandler implements AccelerationMotionEventListener {

    @Inject
    private DrawPath mDrawPath;

    @Override
    public void onMotionDownX(final SensorPoint p) {
        final ThreeAxisPoint xThreePoint = new ThreeAxisPoint(p, null, null);
        mDrawPath.addPoint(xThreePoint);
    }

    @Override
    public void onMotionDownY(final SensorPoint p) {
        final ThreeAxisPoint yThreePoint = new ThreeAxisPoint(null, p, null);
        mDrawPath.addPoint(yThreePoint);
    }

    @Override
    public void onMotionDownZ(final SensorPoint p) {
        final ThreeAxisPoint zThreePoint = new ThreeAxisPoint(null, null, p);
        mDrawPath.addPoint(zThreePoint);
    }

}
