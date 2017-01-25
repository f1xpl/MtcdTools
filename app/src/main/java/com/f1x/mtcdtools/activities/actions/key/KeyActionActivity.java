package com.f1x.mtcdtools.activities.actions.key;

import android.os.Bundle;
import android.widget.Spinner;

import com.f1x.mtcdtools.R;
import com.f1x.mtcdtools.actions.Action;
import com.f1x.mtcdtools.actions.KeyAction;
import com.f1x.mtcdtools.activities.actions.StoreActionActivity;
import com.f1x.mtcdtools.adapters.KeyCodesArrayAdapter;

/**
 * Created by COMPUTER on 2017-01-25.
 */

public class KeyActionActivity extends StoreActionActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_action_details);
        initControls();

        mKeyCodesSpinner = (Spinner)this.findViewById(R.id.spinnerKeyCodes);
        mKeyCodesArrayAdapter = new KeyCodesArrayAdapter(this);
        mKeyCodesSpinner.setAdapter(mKeyCodesArrayAdapter);
    }

    @Override
    public Action createAction(String actionName) {
        String keyCodeName = (String)mKeyCodesSpinner.getSelectedItem();
        return new KeyAction(actionName, mKeyCodesArrayAdapter.getKeyCode(keyCodeName));
    }

    @Override
    protected void onServiceConnected() {}

    protected KeyCodesArrayAdapter mKeyCodesArrayAdapter;
    protected Spinner mKeyCodesSpinner;
}
