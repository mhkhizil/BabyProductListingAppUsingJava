package com.example.assignment; // Replace with your actual package name

import android.app.Application;
import android.database.CursorWindow;
import java.lang.reflect.Field;

public class MyApp extends Application {
    @Override
    public void onCreate() {// Called when the application is first created.
        super.onCreate();// Call the parent's onCreate() for proper initialization.

        try { // Attempt to increase the cursor window size using reflection.
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");// Get private field reference.
            field.setAccessible(true); // Allow modification of private field.
            field.set(null, 100 * 1024 * 1024); // Set new cursor window size (100MB).
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
        }
    }
}
