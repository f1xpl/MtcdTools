package com.f1x.mtcdtools.activities;

import android.os.Bundle;
import android.widget.RadioButton;

import com.f1x.mtcdtools.R;

public class CreateBindingActivity extends ServiceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binding_details);

        mActionRadioButton = (RadioButton)this.findViewById(R.id.radioButtonAction);
        mActionsListRadioButton = (RadioButton)this.findViewById(R.id.radioButtonActionsList);
    }

    @Override
    protected void onServiceConnected() {
        if(mActionsListRadioButton.isChecked()) {

        }
    }

    private RadioButton mActionRadioButton;
    private RadioButton mActionsListRadioButton;
}
