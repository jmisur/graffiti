package org.graffiti.grafroid.drawing;

import com.google.common.base.Optional;
import org.graffiti.grafroid.sensor.SensorPoint;

public class ThreeAxisPoint {
    private final SensorPoint pX;
    private final SensorPoint pY;
    private final SensorPoint pZ;

    public ThreeAxisPoint(final SensorPoint pX, final SensorPoint pY, final SensorPoint pZ) {
        this.pX = pX;
        this.pY = pY;
        this.pZ = pZ;
    }

    public static ThreeAxisPoint fromThreePoint(final ThreeAxisPoint threeAxisPoint, final Optional<SensorPoint> pX, final Optional<SensorPoint> pY, final Optional<SensorPoint> pZ) {
        final SensorPoint x = pX.isPresent() ? pX.get() : threeAxisPoint.pX;
        final SensorPoint y = pY.isPresent() ? pY.get() : threeAxisPoint.pY;
        final SensorPoint z = pZ.isPresent() ? pZ.get() : threeAxisPoint.pZ;
        return new ThreeAxisPoint(x, y, z);
    }

    public SensorPoint getXPoint() {
        return pX;
    }

    public SensorPoint getYPoint() {
        return pY;
    }

    public SensorPoint getZPoint() {
        return pZ;
    }
}