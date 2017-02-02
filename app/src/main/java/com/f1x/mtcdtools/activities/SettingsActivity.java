package com.f1x.mtcdtools.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.f1x.mtcdtools.R;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mSharedPreferences = getSharedPreferences(MainActivity.APP_NAME, Context.MODE_PRIVATE);

        final SeekBar actionExecutionDelaySeekBar = (SeekBar)this.findViewById(R.id.seekBarActionExecutionDelay);
        actionExecutionDelaySeekBar.setProgress(mSharedPreferences.getInt(ACTION_EXECUTION_DELAY_PROPERTY_NAME, ACTION_EXECUTION_DELAY_DEFAULT_VALUE_MS) / 1000);
        final TextView actionExecutionDelayValue = (TextView)this.findViewById(R.id.textViewActionExecutionDelayValue);
        actionExecutionDelayValue.setText(String.format(Locale.getDefault(), "%d", actionExecutionDelaySeekBar.getProgress()));
        actionExecutionDelaySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                actionExecutionDelayValue.setText(String.format(Locale.getDefault(), "%d", progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final SeekBar keyPressSpeedSeekBar = (SeekBar)this.findViewById(R.id.seekBarKeyPressSpeed);
        keyPressSpeedSeekBar.setProgress(mSharedPreferences.getInt(KEY_PRESS_SPEED_PROPERTY_NAME, KEY_PRESS_SPEED_DEFAULT_VALUE_MS));
        final TextView keyPressSpeedValue = (TextView)this.findViewById(R.id.textViewKeyPressSpeedValue);
        keyPressSpeedValue.setText(String.format(Locale.getDefault(), "%d", keyPressSpeedSeekBar.getProgress()));
        keyPressSpeedSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                keyPressSpeedValue.setText(String.format(Locale.getDefault(), "%d", progress));
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
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putInt(ACTION_EXECUTION_DELAY_PROPERTY_NAME, actionExecutionDelaySeekBar.getProgress() * 1000);
                editor.putInt(KEY_PRESS_SPEED_PROPERTY_NAME, keyPressSpeedSeekBar.getProgress());

                if(!editor.commit()) {
                    Toast.makeText(SettingsActivity.this, SettingsActivity.this.getText(R.string.SaveSettingsFailed), Toast.LENGTH_LONG).show();
                } else {
                    SettingsActivity.this.finish();
                }
            }
        });
    }

     SharedPreferences mSharedPreferences;

    public static String ACTION_EXECUTION_DELAY_PROPERTY_NAME = "ActionExecutionDelay";
    public static int ACTION_EXECUTION_DELAY_DEFAULT_VALUE_MS = 3000;
    public static int ACTION_EXECUTION_DELAY_MIN_VALUE_MS = 1000;

    public static String KEY_PRESS_SPEED_PROPERTY_NAME = "KeySpeedPropertyName";
    public static int KEY_PRESS_SPEED_DEFAULT_VALUE_MS = 200;
}
