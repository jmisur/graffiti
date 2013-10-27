package org.graffiti.grafroid.drawing;

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
import org.graffiti.grafroid.AccelerationMotionEventListener;
import org.graffiti.grafroid.R;
import org.graffiti.grafroid.sensor.SensorDataManager;
import org.graffiti.grafroid.sensor.SensorDataManager.DebugDataListener;
import org.graffiti.grafroid.sensor.SensorPoint;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContextSingleton;
import roboguice.inject.InjectView;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

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

    @ContextSingleton
    private static class DrawingControlViewListener implements DebugDataListener, AccelerationMotionEventListener {
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
            drawCurrentPath();
            mSensorDataManager.startRecording(this);
            mSensorDataManager.setDebugDataListener(this);
        }
        
        public void stopRecording() {
            mSensorDataManager.stopRecording();
        }

        public void clearImage(){
            mPath.clear();
            drawCurrentPath();
            mDrawingImage.invalidate();
        }
        
        private void drawCurrentPath() {
            final ImmutableList<ThreeAxisPoint> currentPath = mPath.getInterpolatedPoints();
            final Bitmap bitmap = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888);
            final float startX = bitmap.getWidth() / 2;
            final float startY = bitmap.getHeight() / 2;
            mBitmapController.render(currentPath, startX, startY, bitmap, mDrawingImage);
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

        @Override
        public void onMotionDownX(final SensorPoint p) {
            final ThreeAxisPoint xThreePoint = new ThreeAxisPoint(p, null, null);
            mPath.addPoint(xThreePoint);
        }

        @Override
        public void onMotionDownY(final SensorPoint p) {
            final ThreeAxisPoint yThreePoint = new ThreeAxisPoint(null, p, null);
            mPath.addPoint(yThreePoint);
        }

        @Override
        public void onMotionDownZ(final SensorPoint p) {
            final ThreeAxisPoint zThreePoint = new ThreeAxisPoint(null, null, p);
            mPath.addPoint(zThreePoint);
        }

        @Override
        public void onMotionStopX(long timeStamp) {
            final ThreeAxisPoint xThreePoint = new ThreeAxisPoint(new SensorPoint(timeStamp, 0), null, null);
            mPath.addPoint(xThreePoint);
        }

        @Override
        public void onMotionStopY(long timeStamp) {
            final ThreeAxisPoint yThreePoint = new ThreeAxisPoint(null, new SensorPoint(timeStamp, 0), null);
             mPath.addPoint(yThreePoint);
        }

        @Override
        public void onMotionStopZ(long timeStamp) {
            final ThreeAxisPoint zThreePoint = new ThreeAxisPoint(null, null, new SensorPoint(timeStamp, 0));
            mPath.addPoint(zThreePoint);
        }

        @Override
        public void onMotionTotalStop() {
            drawCurrentPath();
            mDrawingImage.invalidate();
        }
    }
}
