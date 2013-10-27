package org.graffiti.grafroid.drawing;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.graffiti.grafroid.R;
import org.graffiti.grafroid.sensor.SensorDataManager;
import org.graffiti.grafroid.sensor.SensorDataManager.DebugDataListener;
import org.graffiti.grafroid.sensor.SensorPoint;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContextSingleton;
import roboguice.inject.InjectView;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
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
    
    public static final String GRAFITTI_PNG = "grafitti.png";

    private final static String        LOG_TAG = DrawActivity.class.getSimpleName();
    
    @InjectView(R.id.drawingImage)
    private ImageView                  mDrawingImage;
    
    @InjectView(R.id.upload_button)
    private ImageView                  mSaveButton;
    
    @InjectView(R.id.spray_button)
    private ImageView mSprayButton;

    
    @Inject
    private DrawingControlViewListener mDrawingListener;
    
    private boolean                    mRecording;
    
    private BroadcastReceiver          mBroadcastReceiver;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawing);
        
        mBroadcastReceiver = new BroadcastReceiver() {
            
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(ImageUploadService.ACTION_UPLOAD_COMPLETED)){
                    mSaveButton.setVisibility(View.VISIBLE);
                    mDrawingListener.clearImage();
                }
            }
            
        };
        registerReceiver(mBroadcastReceiver, new IntentFilter(ImageUploadService.ACTION_UPLOAD_COMPLETED));
        
        mSprayButton.setOnTouchListener(new OnTouchListener() {
            
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        mDrawingListener.startRecording();
                        break;

                    case MotionEvent.ACTION_UP:
                        mDrawingListener.stopRecording();
                        break;
                }
            return true;
            }
        });
        
     
        
        mSaveButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                //start service to extract language file
                Intent serviceIntent = new Intent(DrawActivity.this, ImageUploadService.class);
                Bitmap bitmap = ((BitmapDrawable)mDrawingImage.getDrawable()).getBitmap();
                FileOutputStream openFileInput;
                try {
                    openFileInput = openFileOutput(GRAFITTI_PNG,Context.MODE_PRIVATE);
                    DataOutputStream printout = new DataOutputStream(openFileInput);
                    bitmap.compress(CompressFormat.PNG, 75, printout);
                    printout.flush();
                    printout.close();
                    openFileInput.close();                    
                    DrawActivity.this.startService(serviceIntent);
                    mSaveButton.setVisibility(View.INVISIBLE);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }
    
//    public boolean onKeyUp(int keyCode, KeyEvent event) {
//        switch (keyCode) {
//            case KeyEvent.KEYCODE_VOLUME_UP:
//            case KeyEvent.KEYCODE_VOLUME_DOWN:
//                mDrawingListener.stopRecording();
//                mRecording = false;
//                break;
//        }
//        
//        return true;
//        
//    };
//    
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        switch (keyCode) {
//            case KeyEvent.KEYCODE_VOLUME_UP:
//            case KeyEvent.KEYCODE_VOLUME_DOWN:
//                if (!mRecording) {
//                    mRecording = true;
//                    mDrawingListener.startRecording();
//                }
//                break;
//        }
//        
//        return true;
//    }
    
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
            //mPath.clear();
            mSensorDataManager.startRecording(mDrawingEventHandler);
            mSensorDataManager.setDebugDataListener(this);
            
        }
        
        public void stopRecording() {
            mSensorDataManager.stopRecording();
            drawCurrentPath();
            mDrawingImage.invalidate();
        }
        public void clearImage(){
            mPath.clear();
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
