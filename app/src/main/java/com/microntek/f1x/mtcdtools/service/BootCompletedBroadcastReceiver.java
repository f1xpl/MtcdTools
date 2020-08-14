package com.microntek.f1x.mtcdtools.service;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.util.Objects;

/**
 * Created by f1x on 2016-08-03.
 */
public class BootCompletedBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.equals(intent.getAction(), Intent.ACTION_BOOT_COMPLETED) ||
                Objects.equals(intent.getAction(), Intent.ACTION_SCREEN_ON)) {

            Intent startServiceIntent = new Intent(context, MtcdService.class);
            startServiceIntent.setAction(MtcdService.ACTION_AUTORUN);
            startServiceIntent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);

            if (isMyServiceRunning(context)) {
                context.stopService(startServiceIntent);
            }
            startService(context, startServiceIntent);
        }
    }

    private boolean isMyServiceRunning(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        if (manager != null) {
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (MtcdService.class.getName().equals(service.service.getClassName())) {
                    return true;
                }
            }
        }
        return false;
    }

    private void startService(Context context, Intent intent) {
        if (Build.VERSION.SDK_INT < 26) {
            context.startService(intent);
        } else {
            context.startForegroundService(intent);
        }
    }

}