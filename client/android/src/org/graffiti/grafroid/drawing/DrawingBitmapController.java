package org.graffiti.grafroid.drawing;

import android.graphics.*;
import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;

import javax.annotation.concurrent.Immutable;
import javax.inject.Named;
import java.util.List;

public class DrawingBitmapController {

    private Bitmap mDrawingBitmap;
    private final Canvas mDrawingCanvas;

    @Inject
    public DrawingBitmapController(@Named("DrawingBitmap") final Bitmap drawingBitmap) {
        this.mDrawingBitmap = drawingBitmap;
        this.mDrawingCanvas = new Canvas(mDrawingBitmap);
    }

    public void draw(final Path drawPath) {
        //FIXME temp stuff:
        final Paint pencil = getPencil();

        mDrawingCanvas.drawPath(drawPath, pencil);
    }

    public void draw(final ImmutableList<ThreeAxisPoint> pathPoints) {
        final Path drawPath = new Path();
        //FIXME temp stuff:
        drawPath.moveTo(100, 100);

        for (final ThreeAxisPoint threeAxisPoint : pathPoints) {
            drawPath.rLineTo((float) threeAxisPoint.pX.getValue(), (float) threeAxisPoint.pY.getValue());
        }

        draw(drawPath);
    }

    private Paint getPencil() {
        final Paint pencil = new Paint();

        pencil.setAntiAlias(true);
        pencil.setColor(Color.BLACK);
        pencil.setStyle(Paint.Style.STROKE);
        pencil.setStrokeJoin(Paint.Join.ROUND);
        pencil.setStrokeWidth(5f);

        return pencil;
    }

}
