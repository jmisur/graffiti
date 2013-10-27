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
        final Paint pencil = getPencil();
        final Paint outerPencil = getOuterPencil(pencil);
        drawingCanvas.drawPath(drawPath, outerPencil);
        drawingCanvas.drawPath(drawPath, pencil);
    }

    
    private void draw(final Bitmap bitmap, final ImmutableList<ThreeAxisPoint> pathPoints) {
        Preconditions.checkNotNull(bitmap);
        final Path drawPath = new Path();
        
        drawPath.moveTo(bitmap.getWidth() / 2, bitmap.getHeight() / 2);
        double left = Double.MAX_VALUE;
        double right = Double.MIN_VALUE;
        double top = Double.MAX_VALUE;
        double bottom = Double.MIN_VALUE;
        
        for (ThreeAxisPoint p : pathPoints) {
            left = Math.min(p.getXPoint().mValue, left);
            right = Math.max(p.getXPoint().mValue, right);
            
            top = Math.min(p.getYPoint().mValue, top);
            bottom = Math.max(p.getYPoint().mValue, bottom);
            
        }
        
        double realWidth = right - left;
        double realHeight = bottom - top;
        
        int dwidth = (int) realWidth;
        int dheight = (int) realHeight;
        int vwidth = bitmap.getWidth();
        int vheight = bitmap.getHeight();
        float scale, dx, dy;
        
        if (dwidth <= vwidth && dheight <= vheight) {
            scale = 1.0f;
        } else {
            scale = Math.min((float) vwidth / (float) dwidth, (float) vheight / (float) dheight);
        }
        dx = (vwidth - dwidth * scale) * 0.5f;
        dy = (vheight - dheight * scale) * 0.5f;
        Matrix scaler = new Matrix();
        scaler.setScale(scale, scale);
        scaler.postTranslate(dx, dy);
        
        //scaler.preTranslate(translateX, translateY);
        float[] mappedPoints = new float[2];
        for (final ThreeAxisPoint threeAxisPoint : pathPoints) {
            mappedPoints[0] = (float) threeAxisPoint.getXPoint().mValue;
            mappedPoints[1] = (float) threeAxisPoint.getYPoint().mValue;
            //scaler.mapPoints(mappedPoints);

            drawPath.rLineTo(mappedPoints[0], mappedPoints[1]);
        }
        
        draw(bitmap, drawPath);
    }
    
    public void render(final List<ThreeAxisPoint> pathPoints, final ImageView view) {
        Preconditions.checkNotNull(view);
        
        final Bitmap drawingBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
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
    
    private Paint getPencil() {
        final Paint pencil = new Paint();
        
        pencil.setAntiAlias(true);
        pencil.setColor(Color.argb(255, 248, 119, 4));
        pencil.setStyle(Paint.Style.STROKE);
        pencil.setStrokeJoin(Paint.Join.ROUND);
        pencil.setStrokeWidth(8f);
        
        return pencil;
    }
    
}
