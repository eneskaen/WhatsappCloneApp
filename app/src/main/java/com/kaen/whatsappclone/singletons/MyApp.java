package com.kaen.whatsappclone.singletons;

import android.app.Application;

import androidx.lifecycle.ProcessLifecycleOwner;

import com.kaen.whatsappclone.observers.AppLifecycleObserver;

public class MyApp extends Application {

    private static MyApp instance;
    private boolean isAppInForeground = false;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        AppLifecycleObserver appLifecycleObserver = new AppLifecycleObserver();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(appLifecycleObserver);
    }

    public static synchronized MyApp getInstance() {
        return instance;
    }

    public void setAppInForeground(boolean isForeground) {
        isAppInForeground = isForeground;
    }

    public boolean isAppInForeground() {
        return isAppInForeground;
    }
}
