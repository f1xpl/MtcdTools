package com.f1x.mtcdtools.input;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.CountDownTimer;

import com.f1x.mtcdtools.input.KeysSequenceListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by COMPUTER on 2017-01-09.
 */

public class PressedKeysSequenceManager extends BroadcastReceiver {
    public PressedKeysSequenceManager() {
        mListeners = new ArrayList<>();
        mPressedKeysSequence = new ArrayList<>();
    }

    public void pushListener(KeysSequenceListener listener) {
        mListeners.add(listener);
    }

    public void popListener(KeysSequenceListener listener) {
        mListeners.remove(listener);
    }

    public IntentFilter getIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(KEY_DOWN_ACTION_NAME);
        return intentFilter;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(KEY_DOWN_ACTION_NAME)) {
            int keyCode = intent.getIntExtra(KEYCODE_PARAM_NAME, DEFAULT_KEY_CODE);

            if(keyCode != DEFAULT_KEY_CODE) {
                if(!mListeners.isEmpty()) {
                    mListeners.get(mListeners.size() - 1).handleSingleKey(keyCode);

                    mPressedKeysSequence.add(keyCode);
                    mKeysCollectingTimer.cancel();
                    mKeysCollectingTimer.start();
                }
            }
        }
    }

    public void onTimerFinish() {
        if(!mListeners.isEmpty()) {
            mListeners.get(mListeners.size() - 1).handleKeysSequence(mPressedKeysSequence);
            mPressedKeysSequence.clear();
        }
    }

    private final CountDownTimer mKeysCollectingTimer = new CountDownTimer(WAIT_PRESS_DURATION_MS, WAIT_PRESS_DURATION_MS) {
        @Override
        public void onTick(long l) {}

        @Override
        public void onFinish() {
            onTimerFinish();
        }
    };

    private final List<KeysSequenceListener> mListeners;
    private final List<Integer> mPressedKeysSequence;

    private static final int DEFAULT_KEY_CODE = -1;
    private static final String KEYCODE_PARAM_NAME = "keyCode";
    private static final String KEY_DOWN_ACTION_NAME = "com.microntek.irkeyDown";
    private static final int WAIT_PRESS_DURATION_MS = 300;
}
