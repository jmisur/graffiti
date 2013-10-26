package org.graffiti.grafroid.drawing;

import com.google.common.base.Objects;
import org.graffiti.grafroid.sensor.SensorPoint;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

public class ThreeAxisPoint {
    private final SensorPoint pX;
    private final SensorPoint pY;
    private final SensorPoint pZ;

    public ThreeAxisPoint(final SensorPoint pX, final SensorPoint pY, final SensorPoint pZ) {
        Preconditions.checkArgument(pX != null || pY != null || pZ != null);
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

    public long getTimeStamp() {
        final long timeStamp = pX != null ? pX.mTimeStamp : pY != null ? pY.mTimeStamp : pZ != null ? pZ.mTimeStamp : -1;
        if (timeStamp == -1 ||
                (pX != null && pX.mTimeStamp != timeStamp) ||
                (pY != null && pY.mTimeStamp != timeStamp) ||
                (pZ != null && pZ.mTimeStamp != timeStamp)) {
            throw new IllegalStateException("All three axis points should have the same timestamp");
        }

        return timeStamp;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("X", getXPoint())
                .add("Y", getYPoint())
                .add("Z", getZPoint())
                .toString();
    }
}