package org.graffiti.grafroid.drawing;

import android.app.Activity;
import android.os.Bundle;
import org.graffiti.grafroid.R;
import org.graffiti.grafroid.processing.SensorDataManager;

/**
 * Activity showing the graffiti drawing.
 */
public class DrawActivity extends Activity {
	private SensorDataManager mSensorData;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mSensorData = new SensorDataManager(this);        
        //@guil call mSensorData.startRecording(listener) and mSensorData.stopRecording()  


    }
}
