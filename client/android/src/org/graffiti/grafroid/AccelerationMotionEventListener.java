package org.graffiti.grafroid;

public interface AccelerationMotionEventListener {
    
    public static class Point {
        public final long mTimeStamp;
        public final double mValue;

        public Point(final long timeStamp, final double value) {
            this.mTimeStamp = timeStamp;
            this.mValue = value;
        }
    }
    
    void onMotionDownX(Point p);
    void onMotionDownY(Point p);
    void onMotionDownZ(Point p);
    
}
