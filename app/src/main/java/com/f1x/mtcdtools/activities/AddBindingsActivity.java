package com.f1x.mtcdtools.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.f1x.mtcdtools.R;
import com.f1x.mtcdtools.adapters.InstalledPackagesArrayAdapter;
import com.f1x.mtcdtools.adapters.KeyInputTypeArrayAdapter;
import com.f1x.mtcdtools.adapters.PackageEntry;
import com.f1x.mtcdtools.input.KeyInput;
import com.f1x.mtcdtools.input.KeyInputType;

public class AddBindingsActivity extends EditBindingsActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bindings);

        InstalledPackagesArrayAdapter installedPackagesArrayAdapter = new InstalledPackagesArrayAdapter(this);
        final Spinner packagesListSpinner = (Spinner)findViewById(R.id.spinnerPackageBind);
        packagesListSpinner.setAdapter(installedPackagesArrayAdapter);
        packagesListSpinner.setVisibility(View.INVISIBLE);
        //-------------------------------------------------------------------------------------

        mKeyInputTypeArrayAdapter = new KeyInputTypeArrayAdapter(this);
        final Spinner keyInputTypeSpinner = (Spinner)findViewById(R.id.spinnerKeyInputType);
        keyInputTypeSpinner.setAdapter(mKeyInputTypeArrayAdapter);
        keyInputTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(KeyInputType.fromString(mKeyInputTypeArrayAdapter.getItem(position)) == KeyInputType.LAUNCH) {
                    packagesListSpinner.setVisibility(View.VISIBLE);
                } else {
                    packagesListSpinner.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                packagesListSpinner.setVisibility(View.INVISIBLE);
            }
        });
        //-------------------------------------------------------------------------------------

        Button obtainKeyInputButton = (Button)findViewById(R.id.buttonObtainKeyInput);
        obtainKeyInputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mKeyObtainProgressDialog.show();
                mKeyObtainTimer.start();
            }
        });
        //-------------------------------------------------------------------------------------

        mKeyCodeTextView = (TextView)findViewById(R.id.textViewKeyCode);
        Button saveButton = (Button)findViewById(R.id.buttonSaveNewBinding);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KeyInputType keyInputType = KeyInputType.fromString((String)keyInputTypeSpinner.getSelectedItem());
                String packageName = "";

                if(keyInputType == KeyInputType.LAUNCH) {
                    PackageEntry packageEntry = (PackageEntry)packagesListSpinner.getSelectedItem();
                    packageName = packageEntry.getName();
                }

                String keyCodeText = mKeyCodeTextView.getText().toString();
                if(keyInputType == KeyInputType.LAUNCH && packageName.isEmpty()) {
                    Toast.makeText(AddBindingsActivity.this, getString(R.string.NotSelectedApplication), Toast.LENGTH_LONG).show();
                } else if(keyCodeText.equals(getString(R.string.DefaultKeyCodeLabel))) {
                    Toast.makeText(AddBindingsActivity.this, getString(R.string.NotObtainedKeyCode), Toast.LENGTH_LONG).show();
                } else {
                    KeyInput keyInput = new KeyInput(Integer.parseInt(mKeyCodeTextView.getText().toString()), keyInputType, packageName);

                    if(mServiceBinder.addKeyInput(keyInput)) {
                        finish();
                    } else {
                        Toast.makeText(AddBindingsActivity.this, getString(R.string.KeyBindingAdditionFailure), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        //-------------------------------------------------------------------------------------

        Button cancelButton = (Button)findViewById(R.id.buttonCancelNewBinding);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //-------------------------------------------------------------------------------------

        mKeyObtainProgressDialog = new ProgressDialog(this);
        mKeyObtainProgressDialog.setTitle(getString(R.string.ObtainKeyInputProgressBarTitle));
        mKeyObtainProgressDialog.setMessage(getString(R.string.ObtainKeyInputProgressBarText));
        mKeyObtainProgressDialog.setCancelable(false);
        //-------------------------------------------------------------------------------------
    }

    @Override
    protected void handleKeyInput(int keyCode) {
        if(mKeyObtainProgressDialog.isShowing()) {
            mKeyObtainTimer.cancel();
            mKeyObtainProgressDialog.dismiss();
            mKeyCodeTextView.setText(Integer.toString(keyCode));
        }
    }

    private final CountDownTimer mKeyObtainTimer = new CountDownTimer(KEY_OBTAIN_TIMER_DURATION_MS, KEY_OBTAIN_TIMER_STEP_MS) {
        @Override
        public void onTick(long l) {}

        @Override
        public void onFinish() {
            mKeyObtainProgressDialog.dismiss();
        }
    };

    private TextView mKeyCodeTextView;
    private ProgressDialog mKeyObtainProgressDialog;
    private KeyInputTypeArrayAdapter mKeyInputTypeArrayAdapter;

    private static final long KEY_OBTAIN_TIMER_DURATION_MS = 5000;
    private static final long KEY_OBTAIN_TIMER_STEP_MS = 1000;
}
