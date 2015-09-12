package com.sri.zoomcar.app;

import android.app.Application;
import android.content.Context;

import com.sri.zoomcar.app.gson.Car;
import com.sri.zoomcar.app.imageloader.SriImageLoader;

/**
 * Created by sridhar on 12/9/15.
 */
public class ZoomCarApp extends Application {

    private static Car currentCar = null;
    private static SriImageLoader imageLoader = null;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static void setCurrentCar(Car c) {
        currentCar = c;
    }

    public static Car getCurrentCar() {
        return currentCar;
    }

    public static SriImageLoader getImageLoader(Context c) {
        if (imageLoader == null) {
            imageLoader = new SriImageLoader(c.getApplicationContext());
        }

        return imageLoader;
    }
}
