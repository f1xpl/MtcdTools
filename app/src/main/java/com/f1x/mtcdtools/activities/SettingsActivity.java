package com.f1x.mtcdtools.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.f1x.mtcdtools.R;

import java.util.Locale;

public class SettingsActivity extends ServiceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mActionExecutionDelaySeekBar = (SeekBar)this.findViewById(R.id.seekBarActionExecutionDelay);
        mActionExecutionDelayValue = (TextView)this.findViewById(R.id.textViewActionExecutionDelayValue);
        mActionExecutionDelaySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                mActionExecutionDelayValue.setText(String.format(Locale.getDefault(), "%d", progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mKeyPressSpeedSeekBar = (SeekBar)this.findViewById(R.id.seekBarKeyPressSpeed);
        mKeyPressSpeedValue = (TextView)this.findViewById(R.id.textViewKeyPressSpeedValue);

        mKeyPressSpeedSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                mKeyPressSpeedValue.setText(String.format(Locale.getDefault(), "%d", progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Button cancelButton = (Button)this.findViewById(R.id.buttonCancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingsActivity.this.finish();
            }
        });

        Button saveButton = (Button)this.findViewById(R.id.buttonSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mServiceBinder.getConfiguration().setActionExecutionDelay(mActionExecutionDelaySeekBar.getProgress() * 1000);
                mServiceBinder.getConfiguration().setKeyPressSpeed(mKeyPressSpeedSeekBar.getProgress());
                SettingsActivity.this.finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(mServiceBinder != null) {
            mActionExecutionDelaySeekBar.setProgress(mServiceBinder.getConfiguration().getActionExecutionDelay() / 1000);
            mActionExecutionDelayValue.setText(String.format(Locale.getDefault(), "%d", mActionExecutionDelaySeekBar.getProgress()));

            mKeyPressSpeedSeekBar.setProgress(mServiceBinder.getConfiguration().getKeyPressSpeed());
            mKeyPressSpeedValue.setText(String.format(Locale.getDefault(), "%d", mKeyPressSpeedSeekBar.getProgress()));
        }
    }

    @Override
    protected void onServiceConnected() {
        mActionExecutionDelaySeekBar.setProgress(mServiceBinder.getConfiguration().getActionExecutionDelay() / 1000);
        mActionExecutionDelayValue.setText(String.format(Locale.getDefault(), "%d", mActionExecutionDelaySeekBar.getProgress()));

        mKeyPressSpeedSeekBar.setProgress(mServiceBinder.getConfiguration().getKeyPressSpeed());
        mKeyPressSpeedValue.setText(String.format(Locale.getDefault(), "%d", mKeyPressSpeedSeekBar.getProgress()));
    }

    private SeekBar mActionExecutionDelaySeekBar;
    TextView mActionExecutionDelayValue;

    private SeekBar mKeyPressSpeedSeekBar;
    TextView mKeyPressSpeedValue;
}
