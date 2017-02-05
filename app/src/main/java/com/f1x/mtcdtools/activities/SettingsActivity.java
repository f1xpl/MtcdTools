package com.f1x.mtcdtools.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TabHost;
import android.widget.TextView;

import com.f1x.mtcdtools.R;

import java.util.Locale;

public class SettingsActivity extends ServiceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        TabHost tabHost = (TabHost)findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec miscTab = tabHost.newTabSpec(this.getString(R.string.Misc));
        miscTab.setContent(R.id.tabMiscSettings);
        miscTab.setIndicator(this.getString(R.string.Misc));
        tabHost.addTab(miscTab);

        TabHost.TabSpec voiceCommandsTab = tabHost.newTabSpec(this.getString(R.string.VoiceCommands));
        voiceCommandsTab.setContent(R.id.tabVoiceCommandsSettings);
        voiceCommandsTab.setIndicator(this.getString(R.string.VoiceCommands));
        tabHost.addTab(voiceCommandsTab);

        TabHost.TabSpec storeSettingsTab = tabHost.newTabSpec(this.getString(R.string.StoreSettings));
        storeSettingsTab.setContent(R.id.tabStoreSettings);
        storeSettingsTab.setIndicator(this.getString(R.string.StoreSettings));
        tabHost.addTab(storeSettingsTab);

        mActionExecutionDelaySeekBar = (SeekBar)this.findViewById(R.id.seekBarActionExecutionDelay);
        mActionExecutionDelayValueTextView = (TextView)this.findViewById(R.id.textViewActionExecutionDelayValue);
        mActionExecutionDelaySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                mActionExecutionDelayValueTextView.setText(String.format(Locale.getDefault(), "%d", progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mKeyPressSpeedSeekBar = (SeekBar)this.findViewById(R.id.seekBarKeyPressSpeed);
        mKeyPressSpeedValueTextView = (TextView)this.findViewById(R.id.textViewKeyPressSpeedValue);

        mKeyPressSpeedSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                mKeyPressSpeedValueTextView.setText(String.format(Locale.getDefault(), "%d", progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mActionSequenceDelaySeekBar = (SeekBar)this.findViewById(R.id.seekBarActionSequenceDelay);
        mActionSequenceDelayValueTextView = (TextView)this.findViewById(R.id.textViewActionsSequenceDelayValue);

        mActionSequenceDelaySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                mActionSequenceDelayValueTextView.setText(String.format(Locale.getDefault(), "%d", progress));
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
                mServiceBinder.getConfiguration().setLaunchVoiceCommandText(mLaunchVoiceCommandEditText.getText().toString());
                mServiceBinder.getConfiguration().setCallVoiceCommandText(mCallVoiceCommandEditText.getText().toString());
                mServiceBinder.getConfiguration().setActionsSequenceDelay(mActionSequenceDelaySeekBar.getProgress());
                SettingsActivity.this.finish();
            }
        });

        mLaunchVoiceCommandEditText = (EditText)this.findViewById(R.id.editTextLaunchVoiceCommand);
        mCallVoiceCommandEditText = (EditText)this.findViewById(R.id.editTextCallVoiceCommand);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(mServiceBinder != null) {
            mActionExecutionDelaySeekBar.setProgress(mServiceBinder.getConfiguration().getActionExecutionDelay() / 1000);
            mActionExecutionDelayValueTextView.setText(String.format(Locale.getDefault(), "%d", mActionExecutionDelaySeekBar.getProgress()));

            mKeyPressSpeedSeekBar.setProgress(mServiceBinder.getConfiguration().getKeyPressSpeed());
            mKeyPressSpeedValueTextView.setText(String.format(Locale.getDefault(), "%d", mKeyPressSpeedSeekBar.getProgress()));

            mActionSequenceDelaySeekBar.setProgress(mServiceBinder.getConfiguration().getActionsSequenceDelay());
            mActionSequenceDelayValueTextView.setText(String.format(Locale.getDefault(), "%d", mActionSequenceDelaySeekBar.getProgress()));
        }
    }

    @Override
    protected void onServiceConnected() {
        mActionExecutionDelaySeekBar.setProgress(mServiceBinder.getConfiguration().getActionExecutionDelay() / 1000);
        mActionExecutionDelayValueTextView.setText(String.format(Locale.getDefault(), "%d", mActionExecutionDelaySeekBar.getProgress()));

        mKeyPressSpeedSeekBar.setProgress(mServiceBinder.getConfiguration().getKeyPressSpeed());
        mKeyPressSpeedValueTextView.setText(String.format(Locale.getDefault(), "%d", mKeyPressSpeedSeekBar.getProgress()));

        mActionSequenceDelaySeekBar.setProgress(mServiceBinder.getConfiguration().getActionsSequenceDelay());
        mActionSequenceDelayValueTextView.setText(String.format(Locale.getDefault(), "%d", mActionSequenceDelaySeekBar.getProgress()));

        mLaunchVoiceCommandEditText.setText(mServiceBinder.getConfiguration().getLaunchVoiceCommandText());
        mCallVoiceCommandEditText.setText(mServiceBinder.getConfiguration().getCallVoiceCommandText());
    }

    private SeekBar mActionExecutionDelaySeekBar;
    private TextView mActionExecutionDelayValueTextView;

    private SeekBar mKeyPressSpeedSeekBar;
    private TextView mKeyPressSpeedValueTextView;

    private SeekBar mActionSequenceDelaySeekBar;
    private TextView mActionSequenceDelayValueTextView;

    private EditText mLaunchVoiceCommandEditText;
    private EditText mCallVoiceCommandEditText;
}
