package com.f1x.mtcdtools;

import com.f1x.mtcdtools.evaluation.KeyPressEvaluatorInterface;

/**
 * Created by COMPUTER on 2016-08-04.
 */
public class MockKeyPressEvaluator implements KeyPressEvaluatorInterface {
    public MockKeyPressEvaluator() {
        mLaunchInputEvaluated = false;
        mModeInputEvaluated = false;
    }

    @Override
    public void evaluateLaunchInput(String packageName) {
        mLaunchInputEvaluated = true;
        mLaunchPackageName = packageName;
    }

    @Override
    public void evaluateModeInput() {
        mModeInputEvaluated = true;
    }

    @Override
    public void evaluateMediaInput(int androidKeyCode) {
        mAndroidKeyCode = androidKeyCode;
    }

    public boolean wasLaunchInputEvaluated() {
        return mLaunchInputEvaluated;
    }

    public boolean wasModeInputEvaluated() {
        return mModeInputEvaluated;
    }

    public String getLaunchPackageName() {
        return mLaunchPackageName;
    }

    public Integer getAndroidKeyCode() {
        return mAndroidKeyCode;
    }

    private boolean mLaunchInputEvaluated;
    private boolean mModeInputEvaluated;
    private String mLaunchPackageName;

    Integer mAndroidKeyCode;
}
