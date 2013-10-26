package org.graffiti.grafroid;

public interface AccelerationMotionEventListener {
    
    public static class Point {
        long mTimeStamp;
        double mValue;
        
    }
    
    void onMotionDownX(Point p);
    void onMotionDownY(Point p);
    void onMotionDownZ(Point p);
    
}
