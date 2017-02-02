package com.f1x.mtcdtools.input;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.CountDownTimer;

import com.f1x.mtcdtools.activities.MainActivity;
import com.f1x.mtcdtools.activities.SettingsActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by COMPUTER on 2017-01-09.
 */

public class PressedKeysSequenceManager extends BroadcastReceiver {
    public PressedKeysSequenceManager(SharedPreferences sharedPreferences) {
        mSharedPreferences = sharedPreferences;
        mListeners = new ArrayList<>();
        mPressedKeysSequence = new ArrayList<>();

        int keyPressSpeed = mSharedPreferences.getInt(SettingsActivity.KEY_PRESS_SPEED_PROPERTY_NAME, SettingsActivity.KEY_PRESS_SPEED_DEFAULT_VALUE_MS);
        mKeysCollectingTimer = createKeyCollectingTimer(keyPressSpeed);

        mSharedPreferences.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String prefixName) {
                if(prefixName.equals(SettingsActivity.KEY_PRESS_SPEED_PROPERTY_NAME)) {
                    int keyPressSpeed = sharedPreferences.getInt(SettingsActivity.KEY_PRESS_SPEED_PROPERTY_NAME, SettingsActivity.KEY_PRESS_SPEED_DEFAULT_VALUE_MS);
                    mKeysCollectingTimer = createKeyCollectingTimer(keyPressSpeed);
                }
            }
        });
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

    private CountDownTimer createKeyCollectingTimer(int delayMs) {
        return new CountDownTimer(delayMs, delayMs) {
            @Override
            public void onTick(long l) {}

            @Override
            public void onFinish() {
                onTimerFinish();
            }
        };
    }

    private final SharedPreferences mSharedPreferences;
    private CountDownTimer mKeysCollectingTimer;

    private final List<KeysSequenceListener> mListeners;
    private final List<Integer> mPressedKeysSequence;

    private static final int DEFAULT_KEY_CODE = -1;
    private static final String KEYCODE_PARAM_NAME = "keyCode";
    private static final String KEY_DOWN_ACTION_NAME = "com.microntek.irkeyDown";
}
