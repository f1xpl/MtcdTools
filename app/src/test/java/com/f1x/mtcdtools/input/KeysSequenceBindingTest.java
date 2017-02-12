package com.f1x.mtcdtools.input;

import com.f1x.mtcdtools.named.objects.NamedObjectId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * Created by f1x on 2017-01-16.
 */

public class KeysSequenceBindingTest {
    @Before
    public void init() throws JSONException {
        mKeysSequenceBindingJson = new JSONObject();

        mKeysSequence = new ArrayList<>(Arrays.asList(1, 100, 1000));
        mKeysSequenceArray = new JSONArray();

        for(Integer keyCode : mKeysSequence) {
            mKeysSequenceArray.put(keyCode);
        }

        mKeysSequenceBindingJson.put(KeysSequenceBinding.KEYS_SEQUENCE_PROPERTY, mKeysSequenceArray);
        mTargetId = new NamedObjectId("testSequence");
        mKeysSequenceBindingJson.put(KeysSequenceBinding.TARGET_NAME_PROPERTY, mTargetId.toString());
    }

    @Test
    public void test_construct() throws JSONException {
        KeysSequenceBinding keysSequenceBinding = new KeysSequenceBinding(mKeysSequenceBindingJson);

        assertEquals(mKeysSequence, keysSequenceBinding.getKeysSequence());
        assertEquals(mTargetId, keysSequenceBinding.getTargetId());
    }

    @Test
    public void test_construct_from_parameters() {
        KeysSequenceBinding keysSequenceBinding = new KeysSequenceBinding(mKeysSequence, mTargetId);
        assertEquals(mKeysSequence, keysSequenceBinding.getKeysSequence());
        assertEquals(mTargetId, keysSequenceBinding.getTargetId());
    }

    @Test
    public void test_toJSON() throws JSONException {
        KeysSequenceBinding keysSequenceBinding = new KeysSequenceBinding(mKeysSequenceBindingJson);
        assertEquals(mKeysSequenceBindingJson.toString(), keysSequenceBinding.toJson().toString());
    }

    private NamedObjectId mTargetId;
    private List<Integer> mKeysSequence;

    private JSONObject mKeysSequenceBindingJson;
    private JSONArray mKeysSequenceArray;
}