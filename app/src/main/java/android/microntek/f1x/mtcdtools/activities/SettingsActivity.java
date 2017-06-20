package android.microntek.f1x.mtcdtools.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TabHost;
import android.widget.TextView;

import android.microntek.f1x.mtcdtools.R;
import android.microntek.f1x.mtcdtools.service.ServiceActivity;

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
                mActionExecutionDelayValueTextView.setText(String.format(Locale.getDefault(), "%d [ms]", progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mVoiceCommandExecutionDelaySeekBar = (SeekBar)this.findViewById(R.id.seekBarVoiceCommandExecutionDelay);
        mVoiceCommandExecutionDelayValueTextView = (TextView)this.findViewById(R.id.textViewVoiceCommandExecutionDelayValue);
        mVoiceCommandExecutionDelaySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                mVoiceCommandExecutionDelayValueTextView.setText(String.format(Locale.getDefault(), "%d [ms]", progress));
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
                mKeyPressSpeedValueTextView.setText(String.format(Locale.getDefault(), "%d [ms]", progress));
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
                mServiceBinder.getConfiguration().setActionExecutionDelay(mActionExecutionDelaySeekBar.getProgress());
                mServiceBinder.getConfiguration().setVoiceCommandExecutionDelay(mVoiceCommandExecutionDelaySeekBar.getProgress());
                mServiceBinder.getConfiguration().setKeyPressSpeed(mKeyPressSpeedSeekBar.getProgress());
                mServiceBinder.getConfiguration().getActionsVoiceDelimiter(mActionsVoiceDelimiterEditText.getText().toString());
                SettingsActivity.this.finish();
            }
        });

        mActionsVoiceDelimiterEditText = (EditText)this.findViewById(R.id.editTextActionsVoiceDelimiter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(mServiceBinder != null) {
            mVoiceCommandExecutionDelaySeekBar.setProgress(mServiceBinder.getConfiguration().getVoiceCommandExecutionDelay());
            mVoiceCommandExecutionDelayValueTextView.setText(String.format(Locale.getDefault(), "%d [ms]", mVoiceCommandExecutionDelaySeekBar.getProgress()));

            mKeyPressSpeedSeekBar.setProgress(mServiceBinder.getConfiguration().getKeyPressSpeed());
            mKeyPressSpeedValueTextView.setText(String.format(Locale.getDefault(), "%d [ms]", mKeyPressSpeedSeekBar.getProgress()));
        }
    }

    @Override
    protected void onServiceConnected() {
        mActionExecutionDelaySeekBar.setProgress(mServiceBinder.getConfiguration().getActionExecutionDelay());
        mActionExecutionDelayValueTextView.setText(String.format(Locale.getDefault(), "%d [ms]", mActionExecutionDelaySeekBar.getProgress()));

        mVoiceCommandExecutionDelaySeekBar.setProgress(mServiceBinder.getConfiguration().getVoiceCommandExecutionDelay());
        mVoiceCommandExecutionDelayValueTextView.setText(String.format(Locale.getDefault(), "%d [ms]", mVoiceCommandExecutionDelaySeekBar.getProgress()));

        mKeyPressSpeedSeekBar.setProgress(mServiceBinder.getConfiguration().getKeyPressSpeed());
        mKeyPressSpeedValueTextView.setText(String.format(Locale.getDefault(), "%d [ms]", mKeyPressSpeedSeekBar.getProgress()));

        mActionsVoiceDelimiterEditText.setText(mServiceBinder.getConfiguration().getActionsVoiceDelimiter());
    }

    private SeekBar mActionExecutionDelaySeekBar;
    private TextView mActionExecutionDelayValueTextView;

    private SeekBar mVoiceCommandExecutionDelaySeekBar;
    private TextView mVoiceCommandExecutionDelayValueTextView;

    private SeekBar mKeyPressSpeedSeekBar;
    private TextView mKeyPressSpeedValueTextView;

    private EditText mActionsVoiceDelimiterEditText;
}
