package com.f1x.mtcdtools.adapters.entries;

import com.f1x.mtcdtools.named.objects.NamedObjectId;

/**
 * Created by COMPUTER on 2017-02-10.
 */

public class ActionInSequenceEntry {
    public ActionInSequenceEntry(NamedObjectId actionId, int delay) {
        mActionId = actionId;
        mDelay = delay;
    }

    public NamedObjectId getActionId() {
        return mActionId;
    }

    public void setDelay(int delay) {
        mDelay = delay;
    }
    public int getDelay() {
        return mDelay;
    }

    private final NamedObjectId mActionId;
    private int mDelay;
}
