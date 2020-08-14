package com.microntek.f1x.mtcdtools.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

/**
 * Created by f1x on 2016-08-16.
 */
class MtcdServiceWatchdog {
    public static void scheduleServiceRestart(Context context) {
        // WORKAROUND: Do not know why MTCD Android does not respect START_STICKY

        Intent restartServiceIntent = new Intent(context, MtcdService.class);
        restartServiceIntent.setPackage(context.getPackageName());

        PendingIntent restartServicePendingIntent = PendingIntent.getService(context, 1555, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + SERVICE_RESTART_DELAY_MS, restartServicePendingIntent);
    }

    private static final int SERVICE_RESTART_DELAY_MS = 100;
}
