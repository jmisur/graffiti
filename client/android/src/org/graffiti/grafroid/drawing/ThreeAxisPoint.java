package org.graffiti.grafroid.drawing;

import com.google.common.base.Optional;
import org.graffiti.grafroid.AccelerationMotionEventListener;

public class ThreeAxisPoint {
    final AccelerationMotionEventListener.Point pX;
    final AccelerationMotionEventListener.Point pY;
    final AccelerationMotionEventListener.Point pZ;

    public ThreeAxisPoint(final AccelerationMotionEventListener.Point pX, final AccelerationMotionEventListener.Point pY, final AccelerationMotionEventListener.Point pZ) {
        this.pX = pX;
        this.pY = pY;
        this.pZ = pZ;
    }

    public static ThreeAxisPoint fromThreePoint(final ThreeAxisPoint threeAxisPoint, final Optional<AccelerationMotionEventListener.Point> pX, final Optional<AccelerationMotionEventListener.Point> pY, final Optional<AccelerationMotionEventListener.Point> pZ) {
        final AccelerationMotionEventListener.Point x = pX.isPresent() ? pX.get() : threeAxisPoint.pX;
        final AccelerationMotionEventListener.Point y = pY.isPresent() ? pY.get() : threeAxisPoint.pY;
        final AccelerationMotionEventListener.Point z = pZ.isPresent() ? pZ.get() : threeAxisPoint.pZ;
        return new ThreeAxisPoint(x, y, z);
    }
}