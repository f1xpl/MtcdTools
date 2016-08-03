package com.f1x.mtcdtools;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Created by COMPUTER on 2016-08-03.
 */

public class StaticMessageHandler extends Handler {
    public StaticMessageHandler() {
        mTargetReference = new WeakReference<>(null);
    }

    public void setTarget(MessageHandlerInterface target) {
        if(target == null) {
            mTargetReference.clear();
        } else {
            mTargetReference = new WeakReference<>(target);
        }
    }

    @Override
    public void handleMessage(Message message) {
        MessageHandlerInterface target = mTargetReference.get();

        if(target != null) {
            target.handleMessage(message);
        }

    }

    private WeakReference<MessageHandlerInterface> mTargetReference;
}
