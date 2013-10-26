package org.graffiti.grafroid;

import android.app.Activity;
import android.hardware.Sensor;
import android.os.Bundle;

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
