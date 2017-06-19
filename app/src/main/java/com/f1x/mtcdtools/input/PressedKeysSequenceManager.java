package com.f1x.mtcdtools.input;

import android.os.CountDownTimer;

import com.f1x.mtcdtools.configuration.Configuration;
import com.f1x.mtcdtools.configuration.ConfigurationChangeListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by f1x on 2017-01-09.
 */

public abstract class PressedKeysSequenceManager implements ConfigurationChangeListener {
    public PressedKeysSequenceManager(Configuration configuration) {
        mConfiguration = configuration;
        mListeners = new ArrayList<>();
        mPressedKeysSequence = new ArrayList<>();

        mKeysCollectingTimer = createKeyCollectingTimer(mConfiguration.getKeyPressSpeed());
        configuration.addChangeListener(this);
    }

    public abstract void init();

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

    protected void insertKeyCode(int keyCode) {
        if(!mListeners.isEmpty()) {
            mListeners.get(mListeners.size() - 1).handleSingleKey(keyCode);

            mPressedKeysSequence.add(keyCode);
            mKeysCollectingTimer.cancel();
            mKeysCollectingTimer.start();
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
}

//package com.microntek.radio;
//
//        import android.os.Bundle;
//        import android.os.Handler;
//        import android.os.Message;
//
///* renamed from: com.microntek.radio.n */
//class C0205n extends Handler {
//    final /* synthetic */ RadioService ey;
//
//    C0205n(RadioService radioService) {
//        this.ey = radioService;
//    }
//
//    public void handleMessage(Message message) {
//        super.handleMessage(message);
//        if ("Radio".equals((String) message.obj)) {
//            this.ey.di(message.getData());
//        } else if ("KeyDown".equals((String) message.obj)) {
//            Bundle data = message.getData();
//            if ("key".equals(data.getString("type"))) {
//                this.ey.dh(data.getInt("value"));
//            }
//        }
//    }
//}
//
//        this.df = new CarManager();
//                this.df.attach(new C0205n(this), "Radio,KeyDown");