package com.f1x.mtcdtools;

import com.f1x.mtcdtools.input.KeysSequenceConverter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by COMPUTER on 2017-01-16.
 */

public class ActionsList {
    public ActionsList(JSONObject json) throws JSONException {
        mName = json.getString(NAME_PROPERTY);

        mActionNames = new ArrayList<>();
        JSONArray actionsArray = json.getJSONArray(ACTIONS_PROPERTY);
        for (int i = 0; i < actionsArray.length(); ++i) {
            mActionNames.add(actionsArray.getString(i));
        }

        mKeysSequenceUp = KeysSequenceConverter.fromJson(json.getJSONArray(KEYS_SEQUENCE_UP_PROPERTY));
        mKeysSequenceDown = KeysSequenceConverter.fromJson(json.getJSONArray(KEYS_SEQUENCE_DOWN_PROPERTY));
    }

    List<Integer> getKeysSequenceUp() {
        return new ArrayList<>(mKeysSequenceUp);
    }

    List<Integer> getKeysSequenceDown() {
        return new ArrayList<>(mKeysSequenceDown);
    }

    public List<String> getActionNames() {
        return new ArrayList<>(mActionNames);
    }

    public JSONObject toJson() throws JSONException {
        JSONObject json = new JSONObject();

        json.put(NAME_PROPERTY, mName);

        JSONArray actionsArray = new JSONArray();
        for (String action : mActionNames) {
            actionsArray.put(action);
        }
        json.put(ACTIONS_PROPERTY, actionsArray);
        json.put(KEYS_SEQUENCE_UP_PROPERTY, KeysSequenceConverter.toJsonArray(mKeysSequenceUp));
        json.put(KEYS_SEQUENCE_DOWN_PROPERTY, KeysSequenceConverter.toJsonArray(mKeysSequenceDown));

        return json;
    }

    public String getName() {
        return mName;
    }

    String mName;
    List<Integer> mKeysSequenceUp;
    List<Integer> mKeysSequenceDown;
    List<String> mActionNames;

    static public final String NAME_PROPERTY = "name";
    static public final String ACTIONS_PROPERTY = "actions";
    static public final String KEYS_SEQUENCE_UP_PROPERTY = "keysSequenceUp";
    static public final String KEYS_SEQUENCE_DOWN_PROPERTY = "keysSequenceDown";
}
