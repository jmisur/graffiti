package org.graffiti.grafroid.drawing;

import java.util.List;

import org.graffiti.grafroid.AccelerationMotionEventListener;
import org.graffiti.grafroid.R;
import org.graffiti.grafroid.sensor.SensorDataManager;
import org.graffiti.grafroid.sensor.SensorDataManager.DebugDataListener;
import org.graffiti.grafroid.sensor.SensorPoint;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.content.Context;
import android.graphics.Color;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphViewSeries.GraphViewSeriesStyle;
import com.jjoe64.graphview.LineGraphView;

/**
 * Activity showing debug output
 */
public class DebugActivity extends RoboActivity implements View.OnTouchListener, AccelerationMotionEventListener, DebugDataListener {
    
    @InjectView(R.id.drawingImage)
    private ImageView         mDrawingImage;
    
    @InjectView(R.id.container)
    private ViewGroup         mDebugContainer;
    
    private SensorDataManager mSensorDataManager;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.debugging);
        SensorManager manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorDataManager = new SensorDataManager(manager);
        mDrawingImage.setOnTouchListener(this);
    }
    
    @Override
    public boolean onTouch(final View v, final MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mSensorDataManager.startRecording(this);
                mSensorDataManager.setDebugDataListener(this);
                return true;
            case MotionEvent.ACTION_UP:
                mSensorDataManager.stopRecording();
                return true;
        }
        
        return false;
    }
    
    @Override
    public void onMotionDownX(SensorPoint p) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void onMotionDownY(SensorPoint p) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void onMotionDownZ(SensorPoint p) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void onDebugData(List<SensorPoint> points, List<SensorPoint> extrema, int index) {
        
        GraphViewData[] pointArray = pointListToGraphArray(points);
        GraphViewData[] extremaArray = pointListToGraphArray(extrema);
        
        GraphViewSeries pointsSeries = new GraphViewSeries("points", new GraphViewSeriesStyle(Color.RED, 2), pointArray);
        GraphViewSeries extremeSeries = new GraphViewSeries("extreme", new GraphViewSeriesStyle(Color.GREEN, 2), extremaArray);
        
        GraphView graphView = new LineGraphView(this, "LinearAccelerometer");
        graphView.addSeries(pointsSeries);
        graphView.addSeries(extremeSeries);
        graphView.setManualYAxisBounds(20, -20);
        mDebugContainer.removeAllViews();
        mDebugContainer.addView(graphView);
    }
    
    public static GraphViewData[] pointListToGraphArray(List<SensorPoint> points) {
        GraphViewData[] pointArray = new GraphViewData[points.size()];
        for (int i = 0; i < points.size(); i++) {
            SensorPoint p = points.get(i);
            GraphViewData data = new GraphViewData(p.mTimeStamp, p.mValue);
            pointArray[i] = data;
        }
        return pointArray;
    }

    @Override
    public void onMotionStopX(long timestamp) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onMotionStopY(long timestamp) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onMotionStopZ(long timestamp) {
        // TODO Auto-generated method stub
        
    }


}
