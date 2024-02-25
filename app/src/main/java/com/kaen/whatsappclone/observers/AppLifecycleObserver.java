package com.kaen.whatsappclone.observers;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.kaen.whatsappclone.singletons.MyApp;
import com.kaen.whatsappclone.singletons.UserStatusManager;

public class AppLifecycleObserver implements LifecycleObserver {


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onEnterForeground() {
        MyApp.getInstance().setAppInForeground(true);
        UserStatusManager.getInstance().setOnline(true);

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onEnterBackground() {
        MyApp.getInstance().setAppInForeground(false);
        UserStatusManager.getInstance().setOnline(false);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onAppDestroyed() {
        UserStatusManager.getInstance().setOnline(false);
    }
}
