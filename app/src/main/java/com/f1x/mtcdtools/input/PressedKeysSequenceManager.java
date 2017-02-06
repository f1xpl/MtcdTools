package com.f1x.mtcdtools.input;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.CountDownTimer;

import com.f1x.mtcdtools.configuration.Configuration;
import com.f1x.mtcdtools.configuration.ConfigurationChangeListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by f1x on 2017-01-09.
 */

public class PressedKeysSequenceManager extends BroadcastReceiver implements ConfigurationChangeListener {
    public PressedKeysSequenceManager(Configuration configuration) {
        mConfiguration = configuration;
        mListeners = new ArrayList<>();
        mPressedKeysSequence = new ArrayList<>();

        mKeysCollectingTimer = createKeyCollectingTimer(mConfiguration.getKeyPressSpeed());
        configuration.addChangeListener(this);
    }

    public void destroy() {
        mListeners.clear();
        mKeysCollectingTimer.cancel();
        mConfiguration.removeChangeListener(this);
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

    private void onTimerFinish() {
        if(!mListeners.isEmpty()) {
            mListeners.get(mListeners.size() - 1).handleKeysSequence(mPressedKeysSequence);
            mPressedKeysSequence.clear();
        }
    }

    @Override
    public void onParameterChanged(String parameterName, Configuration configuration) {
        if(parameterName.equals(Configuration.KEY_PRESS_SPEED_PROPERTY_NAME)) {
            mKeysCollectingTimer.cancel();
            mKeysCollectingTimer = createKeyCollectingTimer(configuration.getKeyPressSpeed());
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

    private final Configuration mConfiguration;
    private CountDownTimer mKeysCollectingTimer;

    private final List<KeysSequenceListener> mListeners;
    private final List<Integer> mPressedKeysSequence;

    private static final int DEFAULT_KEY_CODE = -1;
    private static final String KEYCODE_PARAM_NAME = "keyCode";
    private static final String KEY_DOWN_ACTION_NAME = "com.microntek.irkeyDown";
}
