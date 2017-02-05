package com.f1x.mtcdtools;

import com.f1x.mtcdtools.input.KeysSequenceConverter;
import com.f1x.mtcdtools.storage.NamedObject;

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

public class ActionsList extends NamedObject {
    public ActionsList(JSONObject json) throws JSONException {
        super(json);

        mActionNames = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        JSONArray actionsArray = json.getJSONArray(ACTIONS_PROPERTY);

        for (int i = 0; i < actionsArray.length(); ++i) {
            mActionNames.add(actionsArray.getString(i));
        }

        mKeysSequenceUp = KeysSequenceConverter.fromJsonArray(json.getJSONArray(KEYS_SEQUENCE_UP_PROPERTY));
        mKeysSequenceDown = KeysSequenceConverter.fromJsonArray(json.getJSONArray(KEYS_SEQUENCE_DOWN_PROPERTY));
    }

    public ActionsList(String name, List<Integer> keysSequenceUp, List<Integer> keysSequenceDown, Set<String> actionsNames) {
        super(name, OBJECT_TYPE);

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

    @Override
    public JSONObject toJson() throws JSONException {
        JSONObject json = super.toJson();

        JSONArray actionsArray = new JSONArray();
        for (String action : mActionNames) {
            actionsArray.put(action);
        }

        json.put(ACTIONS_PROPERTY, actionsArray);
        json.put(KEYS_SEQUENCE_UP_PROPERTY, KeysSequenceConverter.toJsonArray(mKeysSequenceUp));
        json.put(KEYS_SEQUENCE_DOWN_PROPERTY, KeysSequenceConverter.toJsonArray(mKeysSequenceDown));

        return json;
    }

    @Override
    public void removeDependency(String dependencyName) {
        mActionNames.remove(dependencyName);
    }

    @Override
    public void replaceDependency(String oldDependencyName, String newDependencyName) {
        if(mActionNames.contains(oldDependencyName)) {
            mActionNames.remove(oldDependencyName);
            mActionNames.add(newDependencyName);
        }
    }

    private List<Integer> mKeysSequenceUp;
    private List<Integer> mKeysSequenceDown;
    private Set<String> mActionNames;

    static public final String ACTIONS_PROPERTY = "actions";
    static public final String KEYS_SEQUENCE_UP_PROPERTY = "keysSequenceUp";
    static public final String KEYS_SEQUENCE_DOWN_PROPERTY = "keysSequenceDown";
    static public final String OBJECT_TYPE = "ActionsList";
}
