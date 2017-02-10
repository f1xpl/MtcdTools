package com.f1x.mtcdtools.adapters.entries;

/**
 * Created by COMPUTER on 2017-02-10.
 */

public class ActionInSequenceEntry {
    public ActionInSequenceEntry(String actionName, int delay) {
        mActionName = actionName;
        mDelay = delay;
    }

    public String getActionName() {
        return mActionName;
    }

    public void setDelay(int delay) {
        mDelay = delay;
    }
    public int getDelay() {
        return mDelay;
    }

    private final String mActionName;
    private int mDelay;
}
