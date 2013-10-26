package org.graffiti.grafroid.drawing;

import com.google.common.base.Optional;
import org.graffiti.grafroid.sensor.Point;

public class ThreeAxisPoint {
    private final Point pX;
    private final Point pY;
    private final Point pZ;

    public ThreeAxisPoint(final Point pX, final Point pY, final Point pZ) {
        this.pX = pX;
        this.pY = pY;
        this.pZ = pZ;
    }

    public static ThreeAxisPoint fromThreePoint(final ThreeAxisPoint threeAxisPoint, final Optional<Point> pX, final Optional<Point> pY, final Optional<Point> pZ) {
        final Point x = pX.isPresent() ? pX.get() : threeAxisPoint.pX;
        final Point y = pY.isPresent() ? pY.get() : threeAxisPoint.pY;
        final Point z = pZ.isPresent() ? pZ.get() : threeAxisPoint.pZ;
        return new ThreeAxisPoint(x, y, z);
    }

    public Point getXPoint() {
        return pX;
    }

    public Point getYPoint() {
        return pY;
    }

    public Point getZPoint() {
        return pZ;
    }
}