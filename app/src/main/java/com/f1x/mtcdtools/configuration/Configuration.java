package com.f1x.mtcdtools.configuration;

import android.content.SharedPreferences;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by COMPUTER on 2017-02-03.
 */

public class Configuration implements SharedPreferences.OnSharedPreferenceChangeListener {
    public Configuration(SharedPreferences sharedPreferences) {
        mSharedPreferences = sharedPreferences;

        mActionExecutionDelay = sharedPreferences.getInt(ACTION_EXECUTION_DELAY_PROPERTY_NAME, ACTION_EXECUTION_DELAY_DEFAULT_VALUE);
        mActionsSequenceDelay = sharedPreferences.getInt(ACTIONS_SEQUENCE_DELAY_PROPERTY_NAME, ACTIONS_SEQUENCE_DELAY_DEFAULT_VALUE);
        mKeyPressSpeed = sharedPreferences.getInt(KEY_PRESS_SPEED_PROPERTY_NAME, KEY_PRESS_SPEED_DEFAULT_VALUE);
        mActionExecutionVoiceCommandText = sharedPreferences.getString(EXECUTE_ACTION_VOICE_COMMAND_PROPERTY_NAME, EXECUTE_ACTION_VOICE_COMMAND_DEFAULT_VALUE);
        mCallVoiceCommandText = sharedPreferences.getString(CALL_VOICE_COMMAND_PROPERTY_NAME, EXECUTE_ACTION_VOICE_COMMAND_DEFAULT_VALUE);

        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);
        mConfigurationChangeListeners = new ArrayList<>();
    }

    public void addChangeListener(ConfigurationChangeListener listener) {
        mConfigurationChangeListeners.add(listener);
    }

    public void removeChangeListener(ConfigurationChangeListener listener) {
        mConfigurationChangeListeners.remove(listener);
    }

    public int getActionExecutionDelay() {
        return mActionExecutionDelay;
    }
    public void setActionExecutionDelay(int value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(ACTION_EXECUTION_DELAY_PROPERTY_NAME, value);
        editor.apply();

        notifyListeners(ACTION_EXECUTION_DELAY_PROPERTY_NAME);
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

    public String getExecuteActionVoiceCommandText() {
        return mActionExecutionVoiceCommandText;
    }
    public void setExecuteActionVoiceCommandText(String value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(EXECUTE_ACTION_VOICE_COMMAND_PROPERTY_NAME, value);
        editor.apply();

        notifyListeners(EXECUTE_ACTION_VOICE_COMMAND_PROPERTY_NAME);
    }

    public String getCallVoiceCommandText() {
        return mCallVoiceCommandText;
    }
    public void setCallVoiceCommandText(String value) {
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
            case ACTION_EXECUTION_DELAY_PROPERTY_NAME:
                mActionExecutionDelay = sharedPreferences.getInt(ACTION_EXECUTION_DELAY_PROPERTY_NAME, ACTION_EXECUTION_DELAY_DEFAULT_VALUE);
                break;
            case KEY_PRESS_SPEED_PROPERTY_NAME:
                mKeyPressSpeed = sharedPreferences.getInt(KEY_PRESS_SPEED_PROPERTY_NAME, KEY_PRESS_SPEED_DEFAULT_VALUE);
                break;
            case EXECUTE_ACTION_VOICE_COMMAND_PROPERTY_NAME:
                mActionExecutionVoiceCommandText = sharedPreferences.getString(EXECUTE_ACTION_VOICE_COMMAND_PROPERTY_NAME, EXECUTE_ACTION_VOICE_COMMAND_DEFAULT_VALUE);
                break;
            case CALL_VOICE_COMMAND_PROPERTY_NAME:
                mCallVoiceCommandText = sharedPreferences.getString(CALL_VOICE_COMMAND_PROPERTY_NAME, CALL_VOICE_COMMAND_DEFAULT_VALUE);
                break;
            case ACTIONS_SEQUENCE_DELAY_PROPERTY_NAME:
                mActionsSequenceDelay = sharedPreferences.getInt(ACTIONS_SEQUENCE_DELAY_PROPERTY_NAME, ACTIONS_SEQUENCE_DELAY_DEFAULT_VALUE);
            default:
        }
    }

    private SharedPreferences mSharedPreferences;
    private int mActionExecutionDelay;
    private int mActionsSequenceDelay;
    private int mKeyPressSpeed;
    private String mActionExecutionVoiceCommandText;
    private String mCallVoiceCommandText;
    private List<ConfigurationChangeListener> mConfigurationChangeListeners;

    public static final String ACTION_EXECUTION_DELAY_PROPERTY_NAME = "ActionExecutionDelay";
    private static int ACTION_EXECUTION_DELAY_DEFAULT_VALUE = 3000;

    public static final String ACTIONS_SEQUENCE_DELAY_PROPERTY_NAME = "ActionsSequenceDelay";
    private static int ACTIONS_SEQUENCE_DELAY_DEFAULT_VALUE = 3000;

    public static final String KEY_PRESS_SPEED_PROPERTY_NAME = "KeySpeedPropertyName";
    private static int KEY_PRESS_SPEED_DEFAULT_VALUE = 200;

    public static final String EXECUTE_ACTION_VOICE_COMMAND_PROPERTY_NAME = "LaunchVoiceCommand";
    private static String EXECUTE_ACTION_VOICE_COMMAND_DEFAULT_VALUE = "";

    public static final String CALL_VOICE_COMMAND_PROPERTY_NAME = "CallVoiceCommand";
    private static String CALL_VOICE_COMMAND_DEFAULT_VALUE = "";
}
