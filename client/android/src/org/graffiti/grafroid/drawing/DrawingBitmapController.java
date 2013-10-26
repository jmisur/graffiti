package org.graffiti.grafroid.drawing;

import javax.inject.Named;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.widget.ImageView;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;

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
            drawPath.rLineTo((float) threeAxisPoint.getXPoint().mValue, (float) threeAxisPoint.getYPoint().mValue);
        }

        draw(drawPath);
    }

    public void render(final ImageView view) {
        Preconditions.checkNotNull(view);

        final Bitmap copyBitmap = Bitmap.createBitmap(mDrawingBitmap);
        view.setImageBitmap(copyBitmap);
    }

    private Paint getPencil() {
        final Paint pencil = new Paint();

        pencil.setAntiAlias(true);
        pencil.setColor(Color.RED);
        pencil.setStyle(Paint.Style.STROKE);
        pencil.setStrokeJoin(Paint.Join.ROUND);
        pencil.setStrokeWidth(20f);

        return pencil;
    }

}
