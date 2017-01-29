package com.f1x.mtcdtools.activities.actions;

import android.widget.Spinner;
import android.widget.Toast;

import com.f1x.mtcdtools.R;
import com.f1x.mtcdtools.actions.Action;
import com.f1x.mtcdtools.actions.KeyAction;
import com.f1x.mtcdtools.adapters.KeyCodesArrayAdapter;

/**
 * Created by COMPUTER on 2017-01-25.
 */

public class KeyActionActivity extends ActionActivity {
    public KeyActionActivity() {
        super(R.layout.activity_key_action_details);
    }

    @Override
    protected void initControls() {
        super.initControls();

        mKeyCodesSpinner = (Spinner)this.findViewById(R.id.spinnerKeyCodes);
        mKeyCodesArrayAdapter = new KeyCodesArrayAdapter(this);
        mKeyCodesSpinner.setAdapter(mKeyCodesArrayAdapter);
    }

    @Override
    protected Action createAction(String actionName) {
        String keyCodeName = (String)mKeyCodesSpinner.getSelectedItem();
        return new KeyAction(actionName, mKeyCodesArrayAdapter.getKeyCode(keyCodeName));
    }

    @Override
    protected void fillControls(Action action) {
        super.fillControls(action);

        KeyAction keyAction = (KeyAction)action;

        if(keyAction == null) {
            Toast.makeText(this, this.getText(R.string.UnknownObjectType), Toast.LENGTH_LONG).show();
            finish();
        } else {
            int keyCodePosition = mKeyCodesArrayAdapter.getPosition(keyAction.getKeyCode());
            mKeyCodesSpinner.setSelection(keyCodePosition);
        }
    }

    protected KeyCodesArrayAdapter mKeyCodesArrayAdapter;
    protected Spinner mKeyCodesSpinner;
}
