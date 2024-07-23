package com.example.assignment; // Replace with your actual package name

import android.app.Application;
import android.database.CursorWindow;
import java.lang.reflect.Field;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024); // 100MB is the new size
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
        }
    }
}
