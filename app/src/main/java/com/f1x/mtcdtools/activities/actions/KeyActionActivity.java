package com.f1x.mtcdtools.activities.actions;

import android.widget.Spinner;
import android.widget.Toast;

import com.f1x.mtcdtools.R;
import com.f1x.mtcdtools.actions.KeyAction;
import com.f1x.mtcdtools.activities.NamedObjectActivity;
import com.f1x.mtcdtools.adapters.KeyCodesArrayAdapter;
import com.f1x.mtcdtools.storage.NamedObject;

/**
 * Created by COMPUTER on 2017-01-25.
 */

public class KeyActionActivity extends NamedObjectActivity {
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
    protected NamedObject createNamedObject(String namedObjectName) {
        String keyCodeName = (String)mKeyCodesSpinner.getSelectedItem();
        return new KeyAction(namedObjectName, mKeyCodesArrayAdapter.getKeyCode(keyCodeName));
    }

    @Override
    protected void fillControls(NamedObject namedObject) throws ClassCastException {
        super.fillControls(namedObject);

        KeyAction keyAction = (KeyAction)namedObject;
        int keyCodePosition = mKeyCodesArrayAdapter.getPosition(keyAction.getKeyCode());
        mKeyCodesSpinner.setSelection(keyCodePosition);
    }

    protected KeyCodesArrayAdapter mKeyCodesArrayAdapter;
    protected Spinner mKeyCodesSpinner;
}
