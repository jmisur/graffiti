package org.graffiti.grafroid.drawing;

import com.google.inject.Inject;
import org.graffiti.grafroid.AccelerationMotionEventListener;
import org.graffiti.grafroid.sensor.Point;

/**
 * Handles drawing events as they happen.
 */
public class DrawingEventHandler implements AccelerationMotionEventListener {

    @Inject
    private DrawPath mDrawPath;

    @Override
    public void onMotionDownX(final Point p) {
        final ThreeAxisPoint xThreePoint = new ThreeAxisPoint(p, null, null);
        mDrawPath.addPoint(xThreePoint);
    }

    @Override
    public void onMotionDownY(final Point p) {
        final ThreeAxisPoint yThreePoint = new ThreeAxisPoint(null, p, null);
        mDrawPath.addPoint(yThreePoint);
    }

    @Override
    public void onMotionDownZ(final Point p) {
        final ThreeAxisPoint zThreePoint = new ThreeAxisPoint(null, null, p);
        mDrawPath.addPoint(zThreePoint);
    }

}
