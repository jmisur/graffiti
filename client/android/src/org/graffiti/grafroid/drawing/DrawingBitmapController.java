package org.graffiti.grafroid.drawing;

import java.util.List;

import org.graffiti.grafroid.sensor.SensorPoint;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
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
        final Paint pencil = getPencil(0);
        final Paint outerPencil = getOuterPencil(pencil);
        drawingCanvas.drawPath(drawPath, outerPencil);
        drawingCanvas.drawPath(drawPath, pencil);
    }

    private void draw(final Bitmap bitmap, final float startX, final float startY, final ImmutableList<ThreeAxisPoint> pathPoints) {
        Preconditions.checkNotNull(bitmap);
        
        final Path drawPath = new Path();
        drawPath.moveTo(startX, startY);

        ThreeAxisPoint lastPoint = null;
        float lastYMovement = 0;
        float lastXMovement = 0;
        for (final ThreeAxisPoint threeAxisPoint : pathPoints) {
            final long timeDiff = (lastPoint == null) ? 0 : threeAxisPoint.getTimeStamp() - lastPoint.getTimeStamp();
            final double xValue = threeAxisPoint.getXPoint().mValue;
            final double yValue = threeAxisPoint.getYPoint().mValue;
            final float xMovement = (float)((double)timeDiff * xValue) / 10;
            final float yMovement = (float)((double)timeDiff * yValue) / 10;
            
            if (lastYMovement!=0){
                final float x2 = (xMovement + lastXMovement) / 2;
                final float y2 = (yMovement + lastYMovement) / 2;
                //drawPath.rQuadTo(xMovement, yMovement, x2, y2);                
            }
            lastYMovement = yMovement;
            lastXMovement = xMovement;
            drawPath.rLineTo(xMovement, yMovement);
            lastPoint = threeAxisPoint;
        }
        
        draw(bitmap, drawPath);
    }

    public void render(final List<ThreeAxisPoint> pathPoints, final float startX, final float startY, final Bitmap bitmap, final ImageView view) {
        Preconditions.checkNotNull(view);

        final ImmutableList<ThreeAxisPoint> adjustedPoints = adjustForDeviceOrientation(pathPoints);
        draw(bitmap, startX, startY, adjustedPoints);
        view.setImageBitmap(bitmap);
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
        return new Paint() {
            {
                setAntiAlias(true);
                setColor(inner.getColor());
                setAlpha(128);
                setStrokeWidth((float) (inner.getStrokeWidth() * 1.5));
                setStyle(Paint.Style.STROKE);
                setStrokeJoin(Paint.Join.ROUND);
                
            }
        };
    }
    
    private Paint getPencil(final int iteration) {
        final Paint pencil = new Paint();
        
        pencil.setAntiAlias(true);
        pencil.setColor(Color.argb(255, 248, 119, 4));
        pencil.setStyle(Paint.Style.STROKE);
        pencil.setStrokeJoin(Paint.Join.ROUND);
        pencil.setStrokeWidth(12f);
        
        return pencil;
    }
    
}
