package com.f1x.mtcdtools.activities.actions.key;

import android.os.Bundle;
import android.widget.Toast;

import com.f1x.mtcdtools.R;
import com.f1x.mtcdtools.actions.KeyAction;

import org.json.JSONException;

import java.io.IOException;

/**
 * Created by COMPUTER on 2017-01-25.
 */

public class EditKeyActionActivity extends KeyActionActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEditActionName = this.getIntent().getStringExtra(ACTION_NAME_PARAMETER);

        if(mEditActionName == null) {
            Toast.makeText(this, this.getText(R.string.InvalidActionName), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();

        KeyAction keyAction = (KeyAction)mServiceBinder.getActionsStorage().getAction(mEditActionName);
        if(keyAction == null) {
            Toast.makeText(this, this.getText(R.string.UnknownActionType), Toast.LENGTH_LONG).show();
            return;
        }

        int keyCodePosition = mKeyCodesArrayAdapter.getPosition(keyAction.getKeyCode());
        mKeyCodesSpinner.setSelection(keyCodePosition);
        mActionNameEditText.setText(keyAction.getName());
    }

    @Override
    protected void onSaveAction(String actionName) {
        try {
            mServiceBinder.getActionsStorage().remove(mEditActionName);
            super.onSaveAction(actionName);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    String mEditActionName;
    public static final String ACTION_NAME_PARAMETER = "actionName";
}
