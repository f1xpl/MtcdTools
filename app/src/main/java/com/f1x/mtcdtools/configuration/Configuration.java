package com.f1x.mtcdtools.configuration;

import android.content.SharedPreferences;

import com.f1x.mtcdtools.input.KeysSequenceConverter;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by COMPUTER on 2017-02-03.
 */

public class Configuration implements SharedPreferences.OnSharedPreferenceChangeListener {
    public Configuration(SharedPreferences sharedPreferences) throws JSONException {
        mSharedPreferences = sharedPreferences;

        mActionExecutionDelay = sharedPreferences.getInt(ACTION_EXECUTION_DELAY_PROPERTY_NAME, ACTION_EXECUTION_DELAY_DEFAULT_VALUE);
        mKeyPressSpeed = sharedPreferences.getInt(KEY_PRESS_SPEED_PROPERTY_NAME, KEY_PRESS_SPEED_DEFAULT_VALUE);
        mLaunchVoiceCommandText = sharedPreferences.getString(LAUNCH_VOICE_COMMAND_PROPERTY_NAME, LAUNCH_VOICE_COMMAND_DEFAULT_VALUE);
        mCallVoiceCommandText = sharedPreferences.getString(LAUNCH_VOICE_COMMAND_PROPERTY_NAME, LAUNCH_VOICE_COMMAND_DEFAULT_VALUE);
        mVoiceCommandKeysSequence = readVoiceCommandKeySequence(sharedPreferences.getString(VOICE_COMMAND_KEYS_SEQUENCE_PROPERTY_NAME, ""));

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

    public String getLaunchVoiceCommandText() {
        return mLaunchVoiceCommandText;
    }
    public void setLaunchVoiceCommandText(String value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(LAUNCH_VOICE_COMMAND_PROPERTY_NAME, value);
        editor.apply();

        notifyListeners(LAUNCH_VOICE_COMMAND_PROPERTY_NAME);
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

    public List<Integer> getVoiceCommandKeysSequence() {
        return mVoiceCommandKeysSequence;
    }
    public void setVoiceCommandKeysSequence(List<Integer> value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(VOICE_COMMAND_KEYS_SEQUENCE_PROPERTY_NAME, KeysSequenceConverter.toJsonArray(value).toString());
        editor.apply();

        notifyListeners(VOICE_COMMAND_KEYS_SEQUENCE_PROPERTY_NAME);
    }

    private List<Integer> readVoiceCommandKeySequence(String json) throws JSONException {
        return json.isEmpty() ? new ArrayList<Integer>() : KeysSequenceConverter.fromJsonArray(new JSONArray(json));
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
            case LAUNCH_VOICE_COMMAND_PROPERTY_NAME:
                mLaunchVoiceCommandText = sharedPreferences.getString(LAUNCH_VOICE_COMMAND_PROPERTY_NAME, LAUNCH_VOICE_COMMAND_DEFAULT_VALUE);
                break;
            case CALL_VOICE_COMMAND_PROPERTY_NAME:
                mCallVoiceCommandText = sharedPreferences.getString(CALL_VOICE_COMMAND_PROPERTY_NAME, CALL_VOICE_COMMAND_DEFAULT_VALUE);
                break;
            case VOICE_COMMAND_KEYS_SEQUENCE_PROPERTY_NAME:
                try {
                    mVoiceCommandKeysSequence = readVoiceCommandKeySequence(sharedPreferences.getString(VOICE_COMMAND_KEYS_SEQUENCE_PROPERTY_NAME, ""));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            default:
        }
    }

    private SharedPreferences mSharedPreferences;
    private int mActionExecutionDelay;
    private int mKeyPressSpeed;
    private String mLaunchVoiceCommandText;
    private String mCallVoiceCommandText;
    private List<Integer> mVoiceCommandKeysSequence;
    private List<ConfigurationChangeListener> mConfigurationChangeListeners;

    public static final String ACTION_EXECUTION_DELAY_PROPERTY_NAME = "ActionExecutionDelay";
    private static int ACTION_EXECUTION_DELAY_DEFAULT_VALUE = 3000;
    private static int ACTION_EXECUTION_DELAY_MIN_VALUE = 1000;

    public static final String KEY_PRESS_SPEED_PROPERTY_NAME = "KeySpeedPropertyName";
    private static int KEY_PRESS_SPEED_DEFAULT_VALUE = 200;

    public static final String LAUNCH_VOICE_COMMAND_PROPERTY_NAME = "LaunchVoiceCommand";
    private static String LAUNCH_VOICE_COMMAND_DEFAULT_VALUE = "";

    public static final String CALL_VOICE_COMMAND_PROPERTY_NAME = "CallVoiceCommand";
    private static String CALL_VOICE_COMMAND_DEFAULT_VALUE = "";

    public static final String VOICE_COMMAND_KEYS_SEQUENCE_PROPERTY_NAME = "VoiceCommandKeysSequence";
}
