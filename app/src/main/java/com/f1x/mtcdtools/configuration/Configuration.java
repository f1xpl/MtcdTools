package com.f1x.mtcdtools.configuration;

import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by f1x on 2017-02-03.
 */

public class Configuration implements SharedPreferences.OnSharedPreferenceChangeListener {
    public Configuration(SharedPreferences sharedPreferences) {
        mSharedPreferences = sharedPreferences;

        mVoiceCommandExecutionDelay = sharedPreferences.getInt(VOICE_COMMAND_EXECUTION_DELAY_PROPERTY_NAME, VOICE_COMMAND_EXECUTION_DELAY_DEFAULT_VALUE);
        mActionsSequenceDelay = sharedPreferences.getInt(ACTIONS_SEQUENCE_DELAY_PROPERTY_NAME, ACTIONS_SEQUENCE_DELAY_DEFAULT_VALUE);
        mKeyPressSpeed = sharedPreferences.getInt(KEY_PRESS_SPEED_PROPERTY_NAME, KEY_PRESS_SPEED_DEFAULT_VALUE);
        mActionsVoiceDelimiter = sharedPreferences.getString(ACTIONS_VOICE_DELIMITER_PROPERTY_NAME, ACTIONS_VOICE_DELIMITER_DEFAULT_VALUE);
        mCallVoiceCommand = sharedPreferences.getString(CALL_VOICE_COMMAND_PROPERTY_NAME, ACTIONS_VOICE_DELIMITER_DEFAULT_VALUE);

        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);
        mConfigurationChangeListeners = new ArrayList<>();
    }

    public void addChangeListener(ConfigurationChangeListener listener) {
        mConfigurationChangeListeners.add(listener);
    }

    public void removeChangeListener(ConfigurationChangeListener listener) {
        mConfigurationChangeListeners.remove(listener);
    }

    public int getVoiceCommandExecutionDelay() {
        return mVoiceCommandExecutionDelay;
    }
    public void setVoiceCommandExecutionDelay(int value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(VOICE_COMMAND_EXECUTION_DELAY_PROPERTY_NAME, value);
        editor.apply();

        notifyListeners(VOICE_COMMAND_EXECUTION_DELAY_PROPERTY_NAME);
    }

    public int getKeyPressSpeed() {
        return mKeyPressSpeed;
    }
    public void setKeyPressSpeed(int value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(KEY_PRESS_SPEED_PROPERTY_NAME, value);
        editor.apply();

        notifyListeners(KEY_PRESS_SPEED_PROPERTY_NAME);
    }

    public String getActionsVoiceDelimiter() {
        return mActionsVoiceDelimiter;
    }
    public void getActionsVoiceDelimiter(String value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ACTIONS_VOICE_DELIMITER_PROPERTY_NAME, value);
        editor.apply();

        notifyListeners(ACTIONS_VOICE_DELIMITER_PROPERTY_NAME);
    }

    public String getCallVoiceCommand() {
        return mCallVoiceCommand;
    }
    public void setCallVoiceCommand(String value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(CALL_VOICE_COMMAND_PROPERTY_NAME, value);
        editor.apply();

        notifyListeners(CALL_VOICE_COMMAND_PROPERTY_NAME);
    }

    public int getActionsSequenceDelay() {
        return mActionsSequenceDelay;
    }

    public void setActionsSequenceDelay(int value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(ACTIONS_SEQUENCE_DELAY_PROPERTY_NAME, value);
        editor.apply();

        notifyListeners(ACTIONS_SEQUENCE_DELAY_PROPERTY_NAME);
    }

    private void notifyListeners(String parameterName) {
        for(ConfigurationChangeListener listener : mConfigurationChangeListeners) {
            listener.onParameterChanged(parameterName, this);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String propertyName) {
        switch(propertyName) {
            case VOICE_COMMAND_EXECUTION_DELAY_PROPERTY_NAME:
                mVoiceCommandExecutionDelay = sharedPreferences.getInt(VOICE_COMMAND_EXECUTION_DELAY_PROPERTY_NAME, VOICE_COMMAND_EXECUTION_DELAY_DEFAULT_VALUE);
                break;
            case KEY_PRESS_SPEED_PROPERTY_NAME:
                mKeyPressSpeed = sharedPreferences.getInt(KEY_PRESS_SPEED_PROPERTY_NAME, KEY_PRESS_SPEED_DEFAULT_VALUE);
                break;
            case ACTIONS_VOICE_DELIMITER_PROPERTY_NAME:
                mActionsVoiceDelimiter = sharedPreferences.getString(ACTIONS_VOICE_DELIMITER_PROPERTY_NAME, ACTIONS_VOICE_DELIMITER_DEFAULT_VALUE);
                break;
            case CALL_VOICE_COMMAND_PROPERTY_NAME:
                mCallVoiceCommand = sharedPreferences.getString(CALL_VOICE_COMMAND_PROPERTY_NAME, CALL_VOICE_COMMAND_DEFAULT_VALUE);
                break;
            case ACTIONS_SEQUENCE_DELAY_PROPERTY_NAME:
                mActionsSequenceDelay = sharedPreferences.getInt(ACTIONS_SEQUENCE_DELAY_PROPERTY_NAME, ACTIONS_SEQUENCE_DELAY_DEFAULT_VALUE);
            default:
        }
    }

    private final SharedPreferences mSharedPreferences;
    private int mVoiceCommandExecutionDelay;
    private int mActionsSequenceDelay;
    private int mKeyPressSpeed;
    private String mActionsVoiceDelimiter;
    private String mCallVoiceCommand;
    private final List<ConfigurationChangeListener> mConfigurationChangeListeners;

    public static final String VOICE_COMMAND_EXECUTION_DELAY_PROPERTY_NAME = "VoiceCommandExecutionDelay";
    private static final int VOICE_COMMAND_EXECUTION_DELAY_DEFAULT_VALUE = 3000;

    private static final String ACTIONS_SEQUENCE_DELAY_PROPERTY_NAME = "ActionsSequenceDelay";
    private static final int ACTIONS_SEQUENCE_DELAY_DEFAULT_VALUE = 3000;

    public static final String KEY_PRESS_SPEED_PROPERTY_NAME = "KeySpeedPropertyName";
    private static final int KEY_PRESS_SPEED_DEFAULT_VALUE = 200;

    private static final String ACTIONS_VOICE_DELIMITER_PROPERTY_NAME = "ActionsDelimiter";
    private static final String ACTIONS_VOICE_DELIMITER_DEFAULT_VALUE = "";

    private static final String CALL_VOICE_COMMAND_PROPERTY_NAME = "CallVoiceCommand";
    private static final String CALL_VOICE_COMMAND_DEFAULT_VALUE = "";
}
