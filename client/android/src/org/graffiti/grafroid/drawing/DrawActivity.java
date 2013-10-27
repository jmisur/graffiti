package org.graffiti.grafroid.drawing;

import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
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
import org.graffiti.grafroid.R;
import org.graffiti.grafroid.sensor.SensorDataManager;
import org.graffiti.grafroid.sensor.SensorDataManager.DebugDataListener;
import org.graffiti.grafroid.sensor.SensorPoint;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContextSingleton;
import roboguice.inject.InjectView;

import java.util.List;

/**
 * Activity showing the graffiti drawing.
 */
public class DrawActivity extends RoboActivity {
    
    private final static String        LOG_TAG = DrawActivity.class.getSimpleName();
    
    @InjectView(R.id.drawingImage)
    private ImageView                  mDrawingImage;
    
    @Inject
    private DrawingControlViewListener mDrawingListener;
    
    private boolean                    mRecording;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawing);
    }
    
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                mDrawingListener.stopRecording();
                mRecording = false;                
                break;
        }
        
        return true;
        
    };
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (!mRecording) {
                    mRecording = true;
                    mDrawingListener.startRecording();
                }
                break;
        }
        
        return true;
    }
    
    @ContextSingleton
    private static class DrawingControlViewListener implements DebugDataListener {
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
        
        void startRecording() {
            mPath.clear();
            mSensorDataManager.startRecording(mDrawingEventHandler);
            mSensorDataManager.setDebugDataListener(this);
            
        }
        
        public void stopRecording() {
            mSensorDataManager.stopRecording();
            drawCurrentPath();
            mDrawingImage.invalidate();
        }
        
        private void drawCurrentPath() {
            final ImmutableList<ThreeAxisPoint> currentPath = mPath.getInterpolatedPoints();
            mBitmapController.render(currentPath, mDrawingImage);
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
