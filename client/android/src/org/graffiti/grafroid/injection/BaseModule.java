package org.graffiti.grafroid.injection;

import android.graphics.Bitmap;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class BaseModule extends AbstractModule {

    @Override
    protected void configure() {
        final Bitmap drawingBitmap = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888);  //FIXME temp stuff

        bind(Bitmap.class).annotatedWith(Names.named("DrawingBitmap")).toInstance(drawingBitmap);
    }

}
