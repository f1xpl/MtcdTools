package com.f1x.mtcdtools.input;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * Created by f1x on 2017-01-24.
 */

public class KeysSequenceConverterTest {
    @Before
    public void init() throws JSONException {
        mKeysSequence = new ArrayList<>(Arrays.asList(1, 100, 1000));

        mKeysSequenceArray = new JSONArray();
        for(Integer keyCode : mKeysSequence) {
            mKeysSequenceArray.put(keyCode);
        }
    }

    @Test
    public void test_conversion() throws JSONException {
        List<Integer> keysSequence = KeysSequenceConverter.fromJsonArray(mKeysSequenceArray);
        assertEquals(mKeysSequence, keysSequence);

        JSONArray keysSequenceJsonArray = KeysSequenceConverter.toJsonArray(mKeysSequence);
        assertEquals(mKeysSequenceArray.toString(), keysSequenceJsonArray.toString());
    }

    @Test
    public void test_conversion_from_array() {
        int array[] = { 1, 100, 1000};
        List<Integer> keysSequence = KeysSequenceConverter.fromArray(array);

        assertEquals(mKeysSequence, keysSequence);
    }

    private List<Integer> mKeysSequence;
    private JSONArray mKeysSequenceArray;
}
