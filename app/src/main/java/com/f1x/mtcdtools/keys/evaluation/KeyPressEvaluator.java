package com.f1x.mtcdtools.keys.evaluation;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.view.KeyEvent;

/**
 * Created by COMPUTER on 2016-08-03.
 */
public class KeyPressEvaluator implements KeyPressEvaluatorInterface {
    public KeyPressEvaluator(Context context) {
        mContext = context;
        mAudioManager = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE);
    }

    @Override
    public void evaluateLaunchInput(String packageName) {
        Intent intent = mContext.getPackageManager().getLaunchIntentForPackage(packageName);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    @Override
    public void evaluateMediaInput(int actionType, int androidKeyCode) {
        KeyEvent keyEvent = new KeyEvent(actionType, androidKeyCode);
        mAudioManager.dispatchMediaKeyEvent(keyEvent);
    }

    private final Context mContext;
    private final AudioManager mAudioManager;
}
