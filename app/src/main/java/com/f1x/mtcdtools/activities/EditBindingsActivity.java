package com.f1x.mtcdtools.activities;
import com.f1x.mtcdtools.input.KeyPressReceiver;

/**
 * Created by COMPUTER on 2016-08-18.
 */
public abstract class EditBindingsActivity extends ServiceActivity {
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mKeyPressReceiver, mKeyPressReceiver.getIntentFilter());

        if(mServiceBinder != null) {
            mServiceBinder.suspendKeyInputsProcessing();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mKeyPressReceiver);

        if(mServiceBinder != null) {
            mServiceBinder.resumeKeyInputsProcessing();
        }
    }

    @Override
    protected void onServiceConnected() {
        mServiceBinder.suspendKeyInputsProcessing();
    }

    protected abstract void handleKeyInput(int keyCode);

    private final KeyPressReceiver mKeyPressReceiver = new KeyPressReceiver() {
        @Override
        public void handleKeyInput(int keyCode) {
            EditBindingsActivity.this.handleKeyInput(keyCode);
        }
    };
}
