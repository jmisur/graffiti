package org.graffiti.grafroid.drawing;

import java.util.List;

import org.graffiti.grafroid.sensor.SensorPoint;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.widget.ImageView;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class DrawingBitmapController {
    
    private void draw(final Bitmap bitmap, final Path drawPath) {
        Preconditions.checkNotNull(bitmap);
        final Canvas drawingCanvas = new Canvas(bitmap);
        
        //FIXME temp stuff:
        final Paint pencil = getPencil();
        final Paint outerPencil = getOuterPencil(pencil);
        drawingCanvas.drawPath(drawPath, outerPencil);
        drawingCanvas.drawPath(drawPath, pencil);
    }
    
    private void draw(final Bitmap bitmap, final ImmutableList<ThreeAxisPoint> pathPoints) {
        Preconditions.checkNotNull(bitmap);
        
        final Path drawPath = new Path();
        //FIXME temp stuff:
        drawPath.moveTo(100, 100);
        
        for (final ThreeAxisPoint threeAxisPoint : pathPoints) {
            drawPath.rLineTo((float) threeAxisPoint.getXPoint().mValue, (float) threeAxisPoint.getYPoint().mValue);
        }
        
        draw(bitmap, drawPath);
    }
    
    public void render(final ImmutableList<ThreeAxisPoint> pathPoints, final ImageView view) {
        Preconditions.checkNotNull(view);
        
        final Bitmap drawingBitmap = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888);
        final ImmutableList<ThreeAxisPoint> adjustedPoints = adjustForDeviceOrientation(pathPoints);
        draw(drawingBitmap, adjustedPoints);
        view.setImageBitmap(drawingBitmap);
    }
    
    private ImmutableList<ThreeAxisPoint> adjustForDeviceOrientation(final List<ThreeAxisPoint> sensorPoints) { //XXX allow parameterized orientation
        final List<ThreeAxisPoint> adjustedPoints = Lists.newArrayList();
        for (final ThreeAxisPoint sensorPoint : sensorPoints) {
            final SensorPoint adjustedPointX = new SensorPoint(sensorPoint.getYPoint().mTimeStamp, sensorPoint.getYPoint().mValue * -1);
            final SensorPoint adjustedPointY = new SensorPoint(sensorPoint.getXPoint().mTimeStamp, sensorPoint.getXPoint().mValue * -1);
            //Z ignore now
            adjustedPoints.add(new ThreeAxisPoint(adjustedPointX, adjustedPointY, sensorPoint.getZPoint()));
        }
        return ImmutableList.copyOf(adjustedPoints);
    }
    
    private Paint getOuterPencil(final Paint inner) {
        return new Paint(){{
            setAntiAlias(true);
            setColor(inner.getColor());
            setAlpha(128);
            setStrokeWidth((float) (inner.getStrokeWidth()*3));
            setStyle(Paint.Style.STROKE);
            setStrokeJoin(Paint.Join.ROUND);

        }};
    }
    
    private Paint getPencil() {
        final Paint pencil = new Paint();
        
        pencil.setAntiAlias(true);
        pencil.setColor(Color.RED);
        pencil.setStyle(Paint.Style.STROKE);
        pencil.setStrokeJoin(Paint.Join.ROUND);
        pencil.setStrokeWidth(5f);
        
        return pencil;
    }
    
}
