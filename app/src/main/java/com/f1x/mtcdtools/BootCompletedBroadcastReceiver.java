package com.f1x.mtcdtools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by COMPUTER on 2016-08-03.
 */
public class BootCompletedBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Intent startServiceIntent = new Intent(context, MtcdService.class);
            context.startService(startServiceIntent);
        }
    }
}