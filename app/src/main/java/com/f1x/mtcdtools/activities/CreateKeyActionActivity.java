package com.f1x.mtcdtools.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Spinner;

import com.f1x.mtcdtools.R;
import com.f1x.mtcdtools.adapters.KeyCodesArrayAdapter;

public class CreateKeyActionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_action_details);

        mKeyCodesArrayAdapter = new KeyCodesArrayAdapter(this);

        Spinner keyCodesSpinner = (Spinner)this.findViewById(R.id.spinnerKeyCodes);
        keyCodesSpinner.setAdapter(mKeyCodesArrayAdapter);
    }

    private KeyCodesArrayAdapter mKeyCodesArrayAdapter;
}
