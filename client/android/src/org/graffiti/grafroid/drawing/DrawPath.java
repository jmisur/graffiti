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

    /*package*/ void clear() {
        mThreePoints.clear();
    }

    public ImmutableList<ThreeAxisPoint> getInterpolatedPoints() {
        final List<ThreeAxisPoint> pointsCopy = Lists.newArrayList(mThreePoints);

        final List<Long> xUnprocessedTimeStamps = Lists.newArrayList();
        final List<Long> yUnprocessedTimeStamps = Lists.newArrayList();
        final long lastXProcessedTimeStamp = 0;
        final long lastYProcessedTimeStamp = 0;
        final List<SensorPoint> interpolatedXPoints = Lists.newArrayList();
        final List<SensorPoint> interpolatedYPoints = Lists.newArrayList();
        for (final ThreeAxisPoint originalPoint : pointsCopy) {
            final SensorPoint originalXPoint = originalPoint.getXPoint();
            final SensorPoint originalYPoint = originalPoint.getYPoint();
            final long originalPointTimeStamp = originalPoint.getTimeStamp();

            if (originalXPoint != null) {
                final double xDistance = originalXPoint.mValue;
                final long timeDistance = originalXPoint.mTimeStamp - lastXProcessedTimeStamp;
                for (final long timeStamp : xUnprocessedTimeStamps) {
                    final double timeStampDistance = ((double)(timeStamp - lastXProcessedTimeStamp)) / timeDistance;
                    final double xDistanceAtTimeStamp = xDistance * timeStampDistance;
                    interpolatedXPoints.add(new SensorPoint(timeStamp, xDistanceAtTimeStamp));
                }
                xUnprocessedTimeStamps.clear();

                interpolatedXPoints.add(originalXPoint);
            } else {
                xUnprocessedTimeStamps.add(originalPointTimeStamp);
            }

            if (originalYPoint != null) {
                final double yDistance = originalYPoint.mValue;
                final long timeDistance = originalYPoint.mTimeStamp - lastYProcessedTimeStamp;
                for (final long timeStamp : yUnprocessedTimeStamps) {
                    final double timeStampDistance = ((double)(timeStamp - lastYProcessedTimeStamp)) / timeDistance;
                    final double yDistanceAtTimeStamp = yDistance * timeStampDistance;
                    interpolatedYPoints.add(new SensorPoint(timeStamp, yDistanceAtTimeStamp));
                }
                yUnprocessedTimeStamps.clear();

                interpolatedYPoints.add(originalYPoint);
            } else {
                yUnprocessedTimeStamps.add(originalPointTimeStamp);
            }
        }

        //process what remains
        for (final long timeStamp : xUnprocessedTimeStamps) {
            interpolatedXPoints.add(new SensorPoint(timeStamp, 0));
        }
        for (final long timeStamp : yUnprocessedTimeStamps) {
            interpolatedYPoints.add(new SensorPoint(timeStamp, 0));
        }

        Preconditions.checkState(interpolatedXPoints.size() == interpolatedYPoints.size());

        final List<ThreeAxisPoint> interpolatedPoints = Lists.newArrayList();
        for (int i = 0; i < interpolatedXPoints.size(); ++i) {
            final SensorPoint interpolatedXPoint = interpolatedXPoints.get(i);
            final SensorPoint interpolatedYPoint = interpolatedYPoints.get(i);
            final ThreeAxisPoint point = new ThreeAxisPoint(
                    new SensorPoint(interpolatedXPoint.mTimeStamp, interpolatedXPoint.mValue * INTERPOLATION_AMPLITUDE_AMPLIFICATION_RATIO),
                    new SensorPoint(interpolatedYPoint.mTimeStamp, interpolatedYPoint.mValue * INTERPOLATION_AMPLITUDE_AMPLIFICATION_RATIO),
                    new SensorPoint(interpolatedXPoint.mTimeStamp, 0));
            interpolatedPoints.add(point);
        }

        return ImmutableList.copyOf(interpolatedPoints);
    }
}

