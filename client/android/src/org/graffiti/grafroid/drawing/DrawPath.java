package org.graffiti.grafroid.drawing;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.List;

/*package*/ class DrawPath {

    private final List<ThreeAxisPoint> mThreePoints = Lists.newArrayList();

    public void addPoint(final ThreeAxisPoint point) {
        Preconditions.checkNotNull(point);

        mThreePoints.add(point);
    }

    public ImmutableList<ThreeAxisPoint> getInterpolatedPoints() {
        final List<ThreeAxisPoint> interpolatedPoints = Lists.newArrayList(mThreePoints);

        //TODO interpolation

        return ImmutableList.copyOf(interpolatedPoints);
    }
}

