package org.graffiti.grafroid.drawing;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import com.google.inject.Inject;
import org.graffiti.grafroid.R;
import org.graffiti.grafroid.sensor.SensorDataManager;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContextSingleton;
import roboguice.inject.InjectView;

/**
 * Activity showing the graffiti drawing.
 */
public class DrawActivity extends RoboActivity {

    @InjectView(R.id.drawingImage)
    private ImageView mDrawingImage;

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
    private static class DrawingControlViewListener implements View.OnTouchListener {
        @Inject
        private DrawingEventHandler mDrawingEventHandler;
        @Inject
        private SensorDataManager mSensorDataManager;

        @Override
        public boolean onTouch(final View v, final MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mSensorDataManager.startRecording(mDrawingEventHandler);
                    return true;
                case MotionEvent.ACTION_UP:
                    mSensorDataManager.stopRecording();
                    return true;
            }

            return false;
        }
    }
}
