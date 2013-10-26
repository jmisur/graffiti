package org.graffiti.grafroid;

import android.app.Application;
import com.google.inject.Inject;
import org.graffiti.grafroid.drawing.DrawingEventHandler;
import org.graffiti.grafroid.sensor.SensorDataManager;
import roboguice.RoboGuice;
import roboguice.inject.RoboInjector;

public class GrafroidApplication extends Application {

    @Inject
    private DrawingEventHandler mDrawingEventHandler;
    @Inject
    private SensorDataManager mSensorDataManager;

    private void injectItself() {
        final RoboInjector injector = RoboGuice.getInjector(this);
        injector.injectMembersWithoutViews(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        injectItself();

        init();
    }

    @Override
    public void onTerminate() {
        cleanUp();
    }

    private void init() {
        mSensorDataManager.startRecording(mDrawingEventHandler);
    }

    private void cleanUp() {
        mSensorDataManager.stopRecording();
    }

}
