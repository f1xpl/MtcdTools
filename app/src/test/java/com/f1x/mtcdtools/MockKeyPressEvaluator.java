package com.f1x.mtcdtools;

import com.f1x.mtcdtools.keys.evaluation.KeyPressEvaluatorInterface;

/**
 * Created by COMPUTER on 2016-08-04.
 */
public class MockKeyPressEvaluator implements KeyPressEvaluatorInterface {
    public MockKeyPressEvaluator() {
        mLaunchInputEvaluated = false;
    }

    @Override
    public void evaluateLaunchInput(String packageName) {
        mLaunchInputEvaluated = true;
        mLaunchPackageName = packageName;
    }

    @Override
    public void evaluateMediaInput(int actionType, int androidKeyCode) {
        mActionType = actionType;
        mAndroidKeyCode = androidKeyCode;
    }

    public boolean wasLaunchInputEvaluated() {
        return mLaunchInputEvaluated;
    }

    public String getLaunchPackageName() {
        return mLaunchPackageName;
    }

    public Integer getActionType() {
        return mActionType;
    }

    public Integer getAndroidKeyCode() {
        return mAndroidKeyCode;
    }

    private boolean mLaunchInputEvaluated;
    private String mLaunchPackageName;

    Integer mActionType;
    Integer mAndroidKeyCode;
}
