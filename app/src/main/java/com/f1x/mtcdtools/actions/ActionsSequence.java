package com.f1x.mtcdtools.actions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by COMPUTER on 2017-01-16.
 */

public class ActionsSequence {
    public ActionsSequence(JSONObject json) throws JSONException {
        mActionNames = new ArrayList<>();
        JSONArray actionsArray = json.getJSONArray(ACTIONS_PROPERTY);
        for (int i = 0; i < actionsArray.length(); ++i) {
            mActionNames.add(actionsArray.getString(i));
        }

        mKeysSequenceUp = new ArrayList<>();
        JSONArray keysSequenceUpArray = json.getJSONArray(KEYS_SEQUENCE_UP_PROPERTY);
        for (int i = 0; i < keysSequenceUpArray.length(); ++i) {
            mKeysSequenceUp.add(keysSequenceUpArray.getInt(i));
        }

        mKeysSequenceDown = new ArrayList<>();
        JSONArray keysSequenceDownArray = json.getJSONArray(KEYS_SEQUENCE_DOWN_PROPERTY);
        for (int i = 0; i < keysSequenceDownArray.length(); ++i) {
            mKeysSequenceDown.add(keysSequenceDownArray.getInt(i));
        }
    }

    List<Integer> getKeysSequenceUp() {
        return new ArrayList<>(mKeysSequenceUp);
    }

    void setKeysSequenceUp(List<Integer> keysSequenceUp) {
        mKeysSequenceUp = keysSequenceUp;
    }

    List<Integer> getKeysSequenceDown() {
        return new ArrayList<>(mKeysSequenceDown);
    }

    void setKeysSequenceDown(List<Integer> keysSequenceDown) {
        mKeysSequenceDown = keysSequenceDown;
    }

    List<String> getActionNames() {
        return new ArrayList<>(mActionNames);
    }

    void setActionNames(List<String> actionNames) {
        mActionNames = actionNames;
    }

    JSONObject toJson() throws JSONException {
        JSONObject json = new JSONObject();

        JSONArray actionsArray = new JSONArray();
        for (String action : mActionNames) {
            actionsArray.put(action);
        }
        json.put(ACTIONS_PROPERTY, actionsArray);

        JSONArray keysSequenceUpArray = new JSONArray();
        for (int key : mKeysSequenceUp) {
            keysSequenceUpArray.put(key);
        }
        json.put(KEYS_SEQUENCE_UP_PROPERTY, keysSequenceUpArray);

        JSONArray keysSequenceDownArray = new JSONArray();
        for (int key : mKeysSequenceDown) {
            keysSequenceDownArray.put(key);
        }
        json.put(KEYS_SEQUENCE_DOWN_PROPERTY, keysSequenceDownArray);

        return json;
    }

    List<Integer> mKeysSequenceUp;
    List<Integer> mKeysSequenceDown;
    List<String> mActionNames;

    static public final String ACTIONS_PROPERTY = "actions";
    static public final String KEYS_SEQUENCE_UP_PROPERTY = "keysSequenceUp";
    static public final String KEYS_SEQUENCE_DOWN_PROPERTY = "keysSequenceDown";
}
