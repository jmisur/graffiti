package org.graffiti.grafroid.drawing;

import java.util.List;

import javax.inject.Singleton;

import org.graffiti.grafroid.sensor.SensorPoint;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

@Singleton
/*package*/ class DrawPath {

    private final List<ThreeAxisPoint> mThreePoints = Lists.newArrayList();

    private static final float INTERPOLATION_AMPLITUDE_AMPLIFICATION_RATIO = 10;

    public void addPoint(final ThreeAxisPoint point) {
        Preconditions.checkNotNull(point);

        mThreePoints.add(point);
    }

    public ImmutableList<ThreeAxisPoint> getInterpolatedPoints() {
        final List<ThreeAxisPoint> pointsCopy = Lists.newArrayList(mThreePoints);

        //TODO real interpolation
        final List<ThreeAxisPoint> interpolatedPoints = Lists.newArrayList();
        for (final ThreeAxisPoint originalPoint : pointsCopy) {
            final long pointTimeStamp = originalPoint.getTimeStamp();
            final SensorPoint zeroPoint = new SensorPoint(pointTimeStamp, 0);

            final SensorPoint originalPointX = originalPoint.getXPoint();
            final SensorPoint interpolatedPointX = originalPointX != null ? new SensorPoint(pointTimeStamp, originalPointX.mValue * INTERPOLATION_AMPLITUDE_AMPLIFICATION_RATIO) : zeroPoint;
            final SensorPoint originalPointY = originalPoint.getYPoint();
            final SensorPoint interpolatedPointY = originalPointY != null ? new SensorPoint(pointTimeStamp, originalPointY.mValue * INTERPOLATION_AMPLITUDE_AMPLIFICATION_RATIO) : zeroPoint;
            final SensorPoint originalPointZ = originalPoint.getZPoint();
            final SensorPoint interpolatedPointZ = originalPointZ != null ? new SensorPoint(pointTimeStamp, originalPointZ.mValue * INTERPOLATION_AMPLITUDE_AMPLIFICATION_RATIO) : zeroPoint;
            final ThreeAxisPoint interpolatedPoint = new ThreeAxisPoint(interpolatedPointX, interpolatedPointY, interpolatedPointZ);

            interpolatedPoints.add(interpolatedPoint);
        }

        return ImmutableList.copyOf(interpolatedPoints);
    }
}

