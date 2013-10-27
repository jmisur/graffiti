package org.graffiti.grafroid.drawing;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.graffiti.grafroid.sensor.SensorPoint;

import javax.inject.Singleton;
import java.util.List;
import java.util.Map;

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

        final Map<Long, ThreeAxisPoint> orderedPoints = Maps.newTreeMap();
        for (ThreeAxisPoint point : pointsCopy) {
            final Long timeStamp = point.getTimeStamp();
            if (orderedPoints.containsKey(timeStamp)) {
                final ThreeAxisPoint currentPoint = orderedPoints.get(timeStamp);
                final ThreeAxisPoint newPoint = ThreeAxisPoint.fromThreePoint(
                        currentPoint,
                        Optional.fromNullable(point.getXPoint()),
                        Optional.fromNullable(point.getYPoint()),
                        Optional.fromNullable(point.getZPoint()));
                orderedPoints.put(timeStamp, newPoint);
            } else {
                orderedPoints.put(timeStamp, point);
            }
        }

        final List<Long> xUnprocessedTimeStamps = Lists.newArrayList();
        final List<Long> yUnprocessedTimeStamps = Lists.newArrayList();
        SensorPoint lastXProcessedPoint = null;
        SensorPoint lastYProcessedPoint = null;
        final List<SensorPoint> interpolatedXPoints = Lists.newArrayList();
        final List<SensorPoint> interpolatedYPoints = Lists.newArrayList();
        for (final ThreeAxisPoint originalPoint : orderedPoints.values()) {
            final SensorPoint originalXPoint = originalPoint.getXPoint();
            final SensorPoint originalYPoint = originalPoint.getYPoint();
            final long originalPointTimeStamp = originalPoint.getTimeStamp();

            if (originalXPoint != null) {
                if (lastXProcessedPoint == null) {
                    for (final long timeStamp : xUnprocessedTimeStamps) {
                        interpolatedXPoints.add(new SensorPoint(timeStamp, 0));
                    }
                    xUnprocessedTimeStamps.clear();

                    interpolatedXPoints.add(originalXPoint);
                    lastXProcessedPoint = originalXPoint;
                } else {
                    final double xDistance = originalXPoint.mValue - lastXProcessedPoint.mValue;
                    final long timeDistance = originalXPoint.mTimeStamp - lastXProcessedPoint.mTimeStamp;
                    for (final long timeStamp : xUnprocessedTimeStamps) {
                        final double timeStampDistance = ((double)(timeStamp - lastXProcessedPoint.mTimeStamp)) / timeDistance;
                        final double xDistanceAtTimeStamp = xDistance * timeStampDistance;
                        interpolatedXPoints.add(new SensorPoint(timeStamp, xDistanceAtTimeStamp));
                    }
                    xUnprocessedTimeStamps.clear();

                    interpolatedXPoints.add(originalXPoint);

                    lastXProcessedPoint = originalXPoint;
                }
            } else {
                xUnprocessedTimeStamps.add(originalPointTimeStamp);
            }

            if (originalYPoint != null) {
                if (lastYProcessedPoint == null) {
                    for (final long timeStamp : yUnprocessedTimeStamps) {
                        interpolatedYPoints.add(new SensorPoint(timeStamp, 0));
                    }
                    yUnprocessedTimeStamps.clear();

                    interpolatedYPoints.add(originalYPoint);
                    lastYProcessedPoint = originalYPoint;
                } else {
                    final double yDistance = originalYPoint.mValue - lastYProcessedPoint.mValue;
                    final long timeDistance = originalYPoint.mTimeStamp - lastYProcessedPoint.mTimeStamp;
                    for (final long timeStamp : yUnprocessedTimeStamps) {
                        final double timeStampDistance = ((double)(timeStamp - lastYProcessedPoint.mTimeStamp)) / timeDistance;
                        final double yDistanceAtTimeStamp = yDistance * timeStampDistance;
                        interpolatedYPoints.add(new SensorPoint(timeStamp, yDistanceAtTimeStamp));
                    }
                    yUnprocessedTimeStamps.clear();

                    interpolatedYPoints.add(originalYPoint);

                    lastYProcessedPoint = originalYPoint;
                }
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

