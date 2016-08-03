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
    public void evaluateMediaInput(int actionType, int androidKeyCode, String permissions) {
        mActionType = actionType;
        mAndroidKeyCode = androidKeyCode;
        mMediaKeyPermission = permissions;
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

    public String getMediaKeyPermission() {
        return mMediaKeyPermission;
    }

    private boolean mLaunchInputEvaluated;
    private String mLaunchPackageName;

    private Integer mActionType;
    private Integer mAndroidKeyCode;
    private String mMediaKeyPermission;
}
