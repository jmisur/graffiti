package org.graffiti.grafroid.sensor;

import java.util.ArrayList;
import java.util.List;

import org.graffiti.grafroid.AccelerationMotionEventListener;
import org.graffiti.grafroid.sensor.SensorDataManager.DebugDataListener;

import android.util.Log;

import com.google.common.base.Optional;

class SensorDataProcessor {
    private static final int                SENSOR_WINDOW_WIDTH = 30;
    
    private final static String             LOG_TAG             = SensorDataProcessor.class.getSimpleName();
    
    private List<SensorPoint>               mXPoints            = new ArrayList<SensorPoint>();
    private List<SensorPoint>               mYPoints            = new ArrayList<SensorPoint>();
    private List<SensorPoint>               mZPoints            = new ArrayList<SensorPoint>();
    
    SensorWindow                            mXWindow            = new SensorWindow(SENSOR_WINDOW_WIDTH);
    SensorWindow                            mYWindow            = new SensorWindow(SENSOR_WINDOW_WIDTH);
    SensorWindow                            mZWindow            = new SensorWindow(SENSOR_WINDOW_WIDTH);
    
    private AccelerationMotionEventListener mListener;
    private Optional<DebugDataListener>     mDebugListener      = Optional.absent();
    
    public SensorDataProcessor(AccelerationMotionEventListener listener) {
        this.mListener = listener;
    }
    
    public void setDebugDataListener(DebugDataListener debugListener) {
        this.mDebugListener = Optional.of(debugListener);
    }
    
    private boolean[] mIsMoving = new boolean[3];

    private boolean mIsTotalStop;
    
    public void process(float[] data, long time) {
        mXWindow.addData(data[0], time);
        mYWindow.addData(data[1], time);
        mZWindow.addData(data[2], time);
        
        boolean[] moving = { mXWindow.isMoving(), mYWindow.isMoving(), mZWindow.isMoving() };
        // for (int i = 0; i < 3; i++) {
        // Log.i(LOG_TAG, i + " = " + moving[i]);
        // }
        for (int i = 0; i < 3; i++) {
            
            if (mIsMoving[i] != moving[i]) {
                if (mIsMoving[i]) {
                    Log.i(LOG_TAG, "stopped " + i);
                    // motion stopped
                    mIsMoving[i] = false;
                    //                    switch (i) {
                    //                        case 0:
                    //                            findPeaks(mXPoints, i);
                    //                            mXPoints.clear();
                    //                            break;
                    //                        case 1:
                    //                            findPeaks(mYPoints, i);
                    //                            mYPoints.clear();
                    //                            break;
                    //                        case 2:
                    //                            findPeaks(mZPoints, i);
                    //                            mZPoints.clear();
                    //                            break;
                    //                    
                    //                    }
                    
                } else if (!mIsMoving[i]) {
                    Log.i(LOG_TAG, "started " + i);
                    // motion started
                    mIsMoving[i] = true;
                    mIsTotalStop = false;
                    // add inital points
                    switch (i) {
                        case 0:
                            mXPoints.addAll(mXWindow.createList());
                            break;
                        case 1:
                            mYPoints.addAll(mYWindow.createList());
                            break;
                        case 2:
                            mZPoints.addAll(mZWindow.createList());
                            break;
                    }
                    
                }
            }
            if (mIsMoving[i]) {
                switch (i) {
                    case 0:
                        mXPoints.add(new SensorPoint(time, data[0]));
                        break;
                    case 1:
                        mYPoints.add(new SensorPoint(time, data[1]));
                        break;
                    case 2:
                        mZPoints.add(new SensorPoint(time, data[2]));
                        break;
                
                }
            }
        }
        
        if (!mIsTotalStop && (!mIsMoving[0] && !mIsMoving[1] && !mIsMoving[2])) {
            mIsTotalStop = true;
            findPeaks(mXPoints, 0);
            mXPoints.clear();
            findPeaks(mYPoints, 1);
            mYPoints.clear();
            findPeaks(mZPoints, 2);
            mZPoints.clear();
            mListener.onMotionTotalStop();
        }
    }
    
    public void stop() {
        //        mXPoints.clear();
        //        mXWindow.reset();
        //        mYPoints.clear();
        //        mYWindow.reset();
        //        mZPoints.clear();
        //        mZWindow.reset();
        
        for (int i = 0; i < 3; i++) {
            
            if (mIsMoving[i] = true) {
                Log.i(LOG_TAG, "stopped " + i);
                // motion stopped
                switch (i) {
                    case 0:
                        findPeaks(mXPoints, i);
                        mXPoints.clear();
                        mXWindow.reset();
                        break;
                    case 1:
                        findPeaks(mYPoints, i);
                        mYPoints.clear();
                        mYWindow.reset();
                        break;
                    case 2:
                        findPeaks(mZPoints, i);
                        mZPoints.clear();
                        mZWindow.reset();
                        break;
                
                }
            }
        }
        mListener.onMotionTotalStop();

    }
    
    private void findPeaks(List<SensorPoint> points, int index) {
        if (points.isEmpty()) {
            return;
        }
        long lastTime = points.get(points.size() - 1).mTimeStamp;
        for (int i = 0; i < 10; i++) {
            points.add(new SensorPoint(lastTime, 0));
            lastTime += 15;
        }
        ExtremaFinder finder = new ExtremaFinder(points);
        List<SensorPoint> e = finder.getExtrema();
        List<SensorPoint> extrema = new ArrayList<SensorPoint>();
        extrema.add(points.get(0));
        extrema.addAll(e);
        extrema.get(extrema.size() - 1).mValue = 0;
        
        String log = "";
        if (mDebugListener.isPresent()) {
            mDebugListener.get().onDebugData(points, extrema.subList(0, extrema.size() - 1), index);
        }
        
        switch (index) {
            case 0:
                log = "X";
                break;
            case 1:
                log = "Y";
                break;
            case 2:
                log = "Z";
                break;
        }
        if (extrema.size() > 1) {
            // Log.i(LOG_TAG, "FOUND " + extrema.size() + " PEAKS FOR " + log);
            
            for (int i = 0; i < extrema.size() - 1; i++) {
                switch (index) {
                    case 0:
                        log = "X";
                        mListener.onMotionDownX(extrema.get(i));
                        break;
                    case 1:
                        log = "Y";
                        mListener.onMotionDownY(extrema.get(i));
                        break;
                    case 2:
                        log = "Z";
                        mListener.onMotionDownZ(extrema.get(i));
                        break;
                }
                Log.i(LOG_TAG, "sent " + log + " :  " + extrema.get(i));
                
            }
            switch (index) {
                case 0:
                    mListener.onMotionStopX(lastTime);
                    break;
                case 1:
                    mListener.onMotionStopY(lastTime);
                    break;
                case 2:
                    mListener.onMotionStopZ(lastTime);
                    break;
            }
        }
        
    }
    
}
