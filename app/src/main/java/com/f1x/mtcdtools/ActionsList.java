package com.f1x.mtcdtools;

import com.f1x.mtcdtools.input.KeysSequenceConverter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by COMPUTER on 2017-01-16.
 */

public class ActionsList {
    public ActionsList(JSONObject json) throws JSONException {
        mName = json.getString(NAME_PROPERTY);

        mActionNames = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        JSONArray actionsArray = json.getJSONArray(ACTIONS_PROPERTY);

        for (int i = 0; i < actionsArray.length(); ++i) {
            mActionNames.add(actionsArray.getString(i));
        }

        mKeysSequenceUp = KeysSequenceConverter.fromJsonArray(json.getJSONArray(KEYS_SEQUENCE_UP_PROPERTY));
        mKeysSequenceDown = KeysSequenceConverter.fromJsonArray(json.getJSONArray(KEYS_SEQUENCE_DOWN_PROPERTY));
    }

    public ActionsList(String name, List<Integer> keysSequenceUp, List<Integer> keysSequenceDown, Set<String> actionsNames) {
        mName = name;
        mKeysSequenceUp = keysSequenceUp;
        mKeysSequenceDown = keysSequenceDown;
        mActionNames = actionsNames;
    }

    public List<Integer> getKeysSequenceUp() {
        return new ArrayList<>(mKeysSequenceUp);
    }

    public List<Integer> getKeysSequenceDown() {
        return new ArrayList<>(mKeysSequenceDown);
    }

    public Set<String> getActionNames() {
        return new TreeSet<>(mActionNames);
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

    public void removeActionName(String name) {
        mActionNames.remove(name);

    }

    public void replaceActionName(String oldName, String newName) {
        if(mActionNames.contains(oldName)) {
            mActionNames.remove(oldName);
            mActionNames.add(newName);
        }
    }

    private String mName;
    private List<Integer> mKeysSequenceUp;
    private List<Integer> mKeysSequenceDown;
    private Set<String> mActionNames;

    static public final String NAME_PROPERTY = "name";
    static public final String ACTIONS_PROPERTY = "actions";
    static public final String KEYS_SEQUENCE_UP_PROPERTY = "keysSequenceUp";
    static public final String KEYS_SEQUENCE_DOWN_PROPERTY = "keysSequenceDown";
}
