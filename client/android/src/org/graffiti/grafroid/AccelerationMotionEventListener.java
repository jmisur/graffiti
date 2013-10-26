package org.graffiti.grafroid;

public interface AccelerationMotionEventListener {
    
    public static class Point {
        private final long mTimeStamp;
        private final double mValue;

        public Point(final long timeStamp, final double value) {
            this.mTimeStamp = timeStamp;
            this.mValue = value;
        }

        public long getTimeStamp() {
            return mTimeStamp;
        }

        public double getValue() {
            return mValue;
        }
    }
    
    void onMotionDownX(Point p);
    void onMotionDownY(Point p);
    void onMotionDownZ(Point p);
    
}
