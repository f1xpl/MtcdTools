package com.f1x.mtcdtools.named.objects;

import com.f1x.mtcdtools.input.KeysSequenceConverter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by COMPUTER on 2017-01-16.
 */

public class ActionsList extends NamedObjectsContainer {
    public ActionsList(JSONObject json) throws JSONException {
        super(json);

        mKeysSequenceUp = KeysSequenceConverter.fromJsonArray(json.getJSONArray(KEYS_SEQUENCE_UP_PROPERTY));
        mKeysSequenceDown = KeysSequenceConverter.fromJsonArray(json.getJSONArray(KEYS_SEQUENCE_DOWN_PROPERTY));
    }

    public ActionsList(String name, List<Integer> keysSequenceUp, List<Integer> keysSequenceDown, List<String> actionsNames) {
        super(name, OBJECT_TYPE, actionsNames);

        mKeysSequenceUp = keysSequenceUp;
        mKeysSequenceDown = keysSequenceDown;
    }

    public List<Integer> getKeysSequenceUp() {
        return new ArrayList<>(mKeysSequenceUp);
    }

    public List<Integer> getKeysSequenceDown() {
        return new ArrayList<>(mKeysSequenceDown);
    }

    @Override
    public JSONObject toJson() throws JSONException {
        JSONObject json = super.toJson();

        json.put(KEYS_SEQUENCE_UP_PROPERTY, KeysSequenceConverter.toJsonArray(mKeysSequenceUp));
        json.put(KEYS_SEQUENCE_DOWN_PROPERTY, KeysSequenceConverter.toJsonArray(mKeysSequenceDown));

        return json;
    }

    private List<Integer> mKeysSequenceUp;
    private List<Integer> mKeysSequenceDown;

    static public final String KEYS_SEQUENCE_UP_PROPERTY = "keysSequenceUp";
    static public final String KEYS_SEQUENCE_DOWN_PROPERTY = "keysSequenceDown";
    static public final String OBJECT_TYPE = "ActionsList";
}
