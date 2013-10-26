package org.graffiti.grafroid.drawing;

import java.util.List;

import org.graffiti.grafroid.R;
import org.graffiti.grafroid.sensor.SensorDataManager;
import org.graffiti.grafroid.sensor.SensorDataManager.DebugDataListener;
import org.graffiti.grafroid.sensor.SensorPoint;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContextSingleton;
import roboguice.inject.InjectView;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphViewSeries.GraphViewSeriesStyle;
import com.jjoe64.graphview.LineGraphView;

/**
 * Activity showing the graffiti drawing.
 */
public class DrawActivity extends RoboActivity {
    
    @InjectView(R.id.drawingImage)
    private ImageView                  mDrawingImage;
    
    @Inject
    private DrawingControlViewListener mDrawingListener;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawing);
        initViews();
    }
    
    private void initViews() {
        mDrawingImage.setOnTouchListener(mDrawingListener);
    }
    
    @ContextSingleton
    private static class DrawingControlViewListener implements View.OnTouchListener, DebugDataListener {
        @InjectView(R.id.drawingImage)
        private ImageView               mDrawingImage;
        
        @Inject
        private DrawingEventHandler     mDrawingEventHandler;
        @Inject
        private SensorDataManager       mSensorDataManager;
        @Inject
        private DrawingBitmapController mBitmapController;
        @Inject
        private DrawPath                mPath;
        @InjectView(R.id.debug_container_x)
        private LinearLayout            mDebugContainerX;
        @InjectView(R.id.debug_container_y)
        private LinearLayout            mDebugContainerY;
        
        @Override
        public boolean onTouch(final View v, final MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mSensorDataManager.startRecording(mDrawingEventHandler);
                    mSensorDataManager.setDebugDataListener(this);
                    
                    return true;
                case MotionEvent.ACTION_UP:
                    mSensorDataManager.stopRecording();
                    drawCurrentPath();
                    mDrawingImage.invalidate();
                    return true;
            }
            
            return false;
        }
        
        private void drawCurrentPath() {
            final ImmutableList<ThreeAxisPoint> currentPath = mPath.getInterpolatedPoints();
            mBitmapController.draw(currentPath); // XXX do that on the
                                                 // background
            mBitmapController.render(mDrawingImage);
        }
        
        @Override
        public void onDebugData(List<SensorPoint> points, List<SensorPoint> extrema, int index) {
            if (index == 1) {
                createGraph(points, extrema, Color.RED, Color.GREEN, "X-Axis", mDebugContainerX);
            } else if (index == 0) {
                createGraph(points, extrema, Color.RED, Color.GREEN, "Y-Axis", mDebugContainerY);
            }
        }
        
        private void createGraph(List<SensorPoint> points, List<SensorPoint> extrema, int pointColor, int extremaColor, String label, ViewGroup container) {
            GraphViewData[] pointArray = DebugActivity.pointListToGraphArray(points);
            GraphViewData[] extremaArray = DebugActivity.pointListToGraphArray(extrema);
            
            GraphViewSeries graphViewSeries = new GraphViewSeries("points", new GraphViewSeriesStyle(pointColor, 2), pointArray);
            GraphViewSeries graphViewSeries2 = new GraphViewSeries("extreme", new GraphViewSeriesStyle(extremaColor, 2), extremaArray);
            GraphView graphView = new LineGraphView(mDrawingImage.getContext(), label);
            graphView.addSeries(graphViewSeries);
            graphView.addSeries(graphViewSeries2);
            graphView.setManualYAxisBounds(25, -25);
            container.removeAllViews();
            container.addView(graphView);
        }
    }
}
