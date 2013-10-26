package org.graffiti.grafroid.drawing;

import android.app.Activity;
import android.os.Bundle;
import org.graffiti.grafroid.R;

/**
 * Activity showing the graffiti drawing.
 */
public class DrawActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawing);
    }

}
