package com.microntek.f1x.mtcdtools.service.input;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.microntek.f1x.mtcdtools.service.configuration.Configuration;

/**
 * Created by COMPUTER on 2017-06-19.
 */

public class PX3PressedKeysSequenceManager extends PressedKeysSequenceManager  {
    public PX3PressedKeysSequenceManager(Configuration configuration, Context context) {
        super(configuration);
        mContext = context;
    }

    @Override
    public void init() {
        mContext.registerReceiver(mPressedKeysReceived, new IntentFilter(KEY_DOWN_ACTION_NAME));
    }

    @Override
    public void destroy() {
        mContext.unregisterReceiver(mPressedKeysReceived);
        super.destroy();
    }

    private final BroadcastReceiver mPressedKeysReceived = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(KEY_DOWN_ACTION_NAME)) {
                int keyCode = intent.getIntExtra(KEYCODE_PARAM_NAME, DEFAULT_KEY_CODE);

                if(keyCode != DEFAULT_KEY_CODE) {
                    insertKeyCode(keyCode);
                }
            }
        }
    };

    private final Context mContext;

    private static final int DEFAULT_KEY_CODE = -1;
    private static final String KEYCODE_PARAM_NAME = "keyCode";
    private static final String KEY_DOWN_ACTION_NAME = "com.microntek.irkeyDown";
}
