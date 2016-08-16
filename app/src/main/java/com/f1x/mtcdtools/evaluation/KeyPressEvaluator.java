package com.f1x.mtcdtools.evaluation;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.view.KeyEvent;

/**
 * Created by COMPUTER on 2016-08-03.
 */
public class KeyPressEvaluator implements KeyPressEvaluatorInterface {
    public KeyPressEvaluator(Context context, ModePackagesRotator modePackagesRotator) {
        mContext = context;
        mAudioManager = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE);
        mModePackagesRotator = modePackagesRotator;
    }

    @Override
    public void evaluateLaunchInput(String packageName) {
        Intent intent = mContext.getPackageManager().getLaunchIntentForPackage(packageName);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    @Override
    public void evaluateMediaInput(int androidKeyCode) {
        KeyEvent keyEventDown = new KeyEvent(KeyEvent.ACTION_DOWN, androidKeyCode);
        mAudioManager.dispatchMediaKeyEvent(keyEventDown);

        KeyEvent keyEventUp = new KeyEvent(KeyEvent.ACTION_UP, androidKeyCode);
        mAudioManager.dispatchMediaKeyEvent(keyEventUp);
    }

    @Override
    public void evaluateModeInput() {
        String packageName = mModePackagesRotator.getNextPackage();

        if(packageName != "") {
            evaluateLaunchInput(packageName);
        }
    }

    private final Context mContext;
    private final AudioManager mAudioManager;
    private final ModePackagesRotator mModePackagesRotator;
}
