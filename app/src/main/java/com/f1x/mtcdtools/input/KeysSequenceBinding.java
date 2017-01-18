package com.f1x.mtcdtools.input;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by COMPUTER on 2017-01-16.
 */

public class KeysSequenceBinding {
    public KeysSequenceBinding(JSONObject json) throws JSONException {
        mKeysSequence = new ArrayList<>();
        JSONArray keysSequenceArray = json.getJSONArray(KEYS_SEQUENCE_PROPERTY);
        for (int i = 0; i < keysSequenceArray.length(); ++i) {
            mKeysSequence.add(keysSequenceArray.getInt(i));
        }

        mTargetType = json.getString(TARGET_TYPE_PROPERTY);
        mTargetName = json.getString(TARGET_NAME_PROPERTY);
    }

    public KeysSequenceBinding(List<Integer> keysSequence, String targetType, String targetName) {
        mKeysSequence = keysSequence;
        mTargetName = targetName;
        mTargetType = targetType;
    }

    public String getTargetType() {
        return mTargetType;
    }

    public String getTargetName() {
        return mTargetName;
    }

    public List<Integer> getKeysSequence() {
        return new ArrayList<>(mKeysSequence);
    }

    public JSONObject toJson() throws JSONException {
        JSONObject json = new JSONObject();
        JSONArray keysSequenceArray = new JSONArray();
        for (int key : mKeysSequence) {
            keysSequenceArray.put(key);
        }
        json.put(KEYS_SEQUENCE_PROPERTY, keysSequenceArray);

        json.put(TARGET_TYPE_PROPERTY, mTargetType);
        json.put(TARGET_NAME_PROPERTY, mTargetName);

        return json;
    }

    private final List<Integer> mKeysSequence;
    private final String mTargetType;
    private final String mTargetName;

    public static final String KEYS_SEQUENCE_PROPERTY = "keysSequence";
    public static final String TARGET_TYPE_PROPERTY = "targetType";
    public static final String TARGET_NAME_PROPERTY = "targetName";

    public static final String TARGET_TYPE_ACTIONS_SEQUENCE = "ActionsSequence";
    public static final String TARGET_TYPE_ACTION = "Action";
}
