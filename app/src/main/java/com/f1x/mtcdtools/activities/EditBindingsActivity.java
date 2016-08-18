package com.f1x.mtcdtools.activities;

import android.os.Message;
import android.os.RemoteException;

import com.f1x.mtcdtools.Messaging;
import com.f1x.mtcdtools.input.KeyInput;
import com.f1x.mtcdtools.input.KeyPressReceiver;

/**
 * Created by COMPUTER on 2016-08-18.
 */
public abstract class EditBindingsActivity extends ServiceActivity {
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mKeyPressReceiver, mKeyPressReceiver.getIntentFilter());

        if(mServiceMessenger != null) {
            sendMessage(Messaging.MessageIds.SUSPEND_KEY_INPUT_DISPATCHING);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mKeyPressReceiver);

        if(mServiceMessenger != null) {
            sendMessage(Messaging.MessageIds.RESUME_KEY_INPUT_DISPATCHING);
        }
    }

    @Override
    protected void onServiceConnected() {
        sendMessage(Messaging.MessageIds.SUSPEND_KEY_INPUT_DISPATCHING);
    }

    protected void sendKeyInputEditRequest(int editType, KeyInput keyInput) {
        Message message = new Message();
        message.what = Messaging.MessageIds.EDIT_KEY_INPUTS_REQUEST;
        message.arg1 = editType;
        message.obj = keyInput;
        message.replyTo = mMessenger;

        try {
            mServiceMessenger.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    protected abstract void handleKeyInput(int keyCode);

    private final KeyPressReceiver mKeyPressReceiver = new KeyPressReceiver() {
        @Override
        public void handleKeyInput(int keyCode) {
            EditBindingsActivity.this.handleKeyInput(keyCode);
        }
    };
}
