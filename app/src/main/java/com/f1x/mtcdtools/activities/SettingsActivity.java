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

        mAVoiceCommandExecutionDelaySeekBar = (SeekBar)this.findViewById(R.id.seekBarActionExecutionDelay);
        mVoiceCommandExecutionDelayValueTextView = (TextView)this.findViewById(R.id.textViewVoiceCommandExecutionDelayValue);
        mAVoiceCommandExecutionDelaySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                mVoiceCommandExecutionDelayValueTextView.setText(String.format(Locale.getDefault(), "%d", progress));
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
                mServiceBinder.getConfiguration().setVoiceCommandExecutionDelay(mAVoiceCommandExecutionDelaySeekBar.getProgress() * 1000);
                mServiceBinder.getConfiguration().setKeyPressSpeed(mKeyPressSpeedSeekBar.getProgress());
                mServiceBinder.getConfiguration().getActionsVoiceDelimiter(mActionsVoiceDelimiterEditText.getText().toString());
                mServiceBinder.getConfiguration().setCallVoiceCommand(mCallVoiceCommandEditText.getText().toString());
                mServiceBinder.getConfiguration().setActionsSequenceDelay(mActionSequenceDelaySeekBar.getProgress());
                SettingsActivity.this.finish();
            }
        });

        mActionsVoiceDelimiterEditText = (EditText)this.findViewById(R.id.editTextActionsVoiceDelimiter);
        mCallVoiceCommandEditText = (EditText)this.findViewById(R.id.editTextCallVoiceCommand);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(mServiceBinder != null) {
            mAVoiceCommandExecutionDelaySeekBar.setProgress(mServiceBinder.getConfiguration().getVoiceCommandExecutionDelay() / 1000);
            mVoiceCommandExecutionDelayValueTextView.setText(String.format(Locale.getDefault(), "%d", mAVoiceCommandExecutionDelaySeekBar.getProgress()));

            mKeyPressSpeedSeekBar.setProgress(mServiceBinder.getConfiguration().getKeyPressSpeed());
            mKeyPressSpeedValueTextView.setText(String.format(Locale.getDefault(), "%d", mKeyPressSpeedSeekBar.getProgress()));

            mActionSequenceDelaySeekBar.setProgress(mServiceBinder.getConfiguration().getActionsSequenceDelay());
            mActionSequenceDelayValueTextView.setText(String.format(Locale.getDefault(), "%d", mActionSequenceDelaySeekBar.getProgress()));
        }
    }

    @Override
    protected void onServiceConnected() {
        mAVoiceCommandExecutionDelaySeekBar.setProgress(mServiceBinder.getConfiguration().getVoiceCommandExecutionDelay() / 1000);
        mVoiceCommandExecutionDelayValueTextView.setText(String.format(Locale.getDefault(), "%d", mAVoiceCommandExecutionDelaySeekBar.getProgress()));

        mKeyPressSpeedSeekBar.setProgress(mServiceBinder.getConfiguration().getKeyPressSpeed());
        mKeyPressSpeedValueTextView.setText(String.format(Locale.getDefault(), "%d", mKeyPressSpeedSeekBar.getProgress()));

        mActionSequenceDelaySeekBar.setProgress(mServiceBinder.getConfiguration().getActionsSequenceDelay());
        mActionSequenceDelayValueTextView.setText(String.format(Locale.getDefault(), "%d", mActionSequenceDelaySeekBar.getProgress()));

        mActionsVoiceDelimiterEditText.setText(mServiceBinder.getConfiguration().getActionsVoiceDelimiter());
        mCallVoiceCommandEditText.setText(mServiceBinder.getConfiguration().getCallVoiceCommand());
    }

    private SeekBar mAVoiceCommandExecutionDelaySeekBar;
    private TextView mVoiceCommandExecutionDelayValueTextView;

    private SeekBar mKeyPressSpeedSeekBar;
    private TextView mKeyPressSpeedValueTextView;

    private SeekBar mActionSequenceDelaySeekBar;
    private TextView mActionSequenceDelayValueTextView;

    private EditText mActionsVoiceDelimiterEditText;
    private EditText mCallVoiceCommandEditText;
}
