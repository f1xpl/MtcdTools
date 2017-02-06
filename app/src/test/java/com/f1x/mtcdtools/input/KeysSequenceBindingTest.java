package com.f1x.mtcdtools.input;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * Created by f1x on 2017-01-16.
 */

public class KeysSequenceBindingTest {
    @Before
    public void init() throws JSONException {
        mKeysSequenceBindingJson = new JSONObject();

        mKeysSequenceArray = new JSONArray();
        mKeysSequenceArray.put(1);
        mKeysSequenceArray.put(100);
        mKeysSequenceArray.put(1000);
        mKeysSequenceBindingJson.put(KeysSequenceBinding.KEYS_SEQUENCE_PROPERTY, mKeysSequenceArray);

        mKeysSequenceBindingJson.put(KeysSequenceBinding.TARGET_NAME_PROPERTY, "testSequence");
    }

    @Test
    public void test_Construct() throws JSONException {
        KeysSequenceBinding keysSequenceBinding = new KeysSequenceBinding(mKeysSequenceBindingJson);

        List<Integer> keysSequence = keysSequenceBinding.getKeysSequence();
        assertEquals(mKeysSequenceArray.length(), keysSequence.size());
        for(int i = 0; i < mKeysSequenceArray.length(); ++i) {
            assertEquals(mKeysSequenceArray.get(i), keysSequence.get(i));
        }

        assertEquals(mKeysSequenceBindingJson.get(KeysSequenceBinding.TARGET_NAME_PROPERTY), keysSequenceBinding.getTargetName());
    }

    @Test
    public void test_toJSON() throws JSONException {
        KeysSequenceBinding keysSequenceBinding = new KeysSequenceBinding(mKeysSequenceBindingJson);
        assertEquals(mKeysSequenceBindingJson.toString(), keysSequenceBinding.toJson().toString());
    }

    JSONObject mKeysSequenceBindingJson;
    JSONArray mKeysSequenceArray;
}