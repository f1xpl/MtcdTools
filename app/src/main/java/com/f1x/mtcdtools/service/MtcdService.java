package com.f1x.mtcdtools.service;

import android.app.Notification;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.f1x.mtcdtools.R;

/**
 * Created by COMPUTER on 2016-08-03.
 */
public class MtcdService extends android.app.Service {
    @Override
    public void onCreate() {
        super.onCreate();

        mForceRestart = true;
        mServiceInitialized = false;
        mKeyInputDispatchingActive = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(mForceRestart) {
            MtcdServiceWatchdog.scheduleServiceRestart(this);
        }

        if(mServiceInitialized) {
        }

        mServiceInitialized = false;
        mKeyInputDispatchingActive = false;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mServiceBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        if(!mServiceInitialized) {
            mServiceInitialized = true;
            mKeyInputDispatchingActive = true;
            startForeground(1555, createNotification());
        }

        return START_STICKY;
    }

    private Notification createNotification() {
        return new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.MtcdServiceDescription))
                .setSmallIcon(R.drawable.notificationicon)
                .setOngoing(true)
                .build();
    }

    private boolean mForceRestart;
    private boolean mServiceInitialized;
    private boolean mKeyInputDispatchingActive;

    private final ServiceBinder mServiceBinder = new ServiceBinder() {
    };
}
