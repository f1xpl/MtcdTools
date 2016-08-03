package com.f1x.mtcdtools.keys.input;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.KeyEvent;

/**
 * Created by COMPUTER on 2016-08-03.
 */
public abstract class KeyPressReceiver extends BroadcastReceiver {
    public IntentFilter getIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(KEY_DOWN_ACTION_NAME);
        intentFilter.addAction(KEY_UP_ACTION_NAME);
        return intentFilter;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(KEY_DOWN_ACTION_NAME)) {
            int keyCode = intent.getIntExtra(KEYCODE_PARAM_NAME, DEFAULT_KEY_CODE);
            handleKeyInput(keyCode, KeyEvent.ACTION_DOWN);
        } else if(intent.getAction().equals(KEY_UP_ACTION_NAME)) {
            int keyCode = intent.getIntExtra(KEYCODE_PARAM_NAME, DEFAULT_KEY_CODE);
            handleKeyInput(keyCode, KeyEvent.ACTION_UP);
        }
    }

    public abstract void handleKeyInput(int keyCode, int actionType);

    private static final int DEFAULT_KEY_CODE = -1;
    private static final String KEYCODE_PARAM_NAME = "keyCode";
    private static final String KEY_DOWN_ACTION_NAME = "com.microntek.irkeyDown";
    private static final String KEY_UP_ACTION_NAME = "com.microntek.irkeyUp";
}
