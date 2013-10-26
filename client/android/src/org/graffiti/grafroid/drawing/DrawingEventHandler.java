package org.graffiti.grafroid.drawing;

import android.graphics.Canvas;
import com.google.inject.Inject;
import org.graffiti.grafroid.AccelerationMotionEventListener;
import org.graffiti.grafroid.sensor.Point;

import javax.inject.Named;

/**
 * Handles drawing events as they happen.
 */
public class DrawingEventHandler implements AccelerationMotionEventListener {

    @Named("DrawingCanvas")
    private final Canvas mDrawingCanvas;

    @Inject
    public DrawingEventHandler(final Canvas drawingCanvas) {
        this.mDrawingCanvas = drawingCanvas;
    }

    @Override
    public void onMotionDownX(final Point p) {

    }

    @Override
    public void onMotionDownY(final Point p) {

    }

    @Override
    public void onMotionDownZ(final Point p) {
        
    }
}
