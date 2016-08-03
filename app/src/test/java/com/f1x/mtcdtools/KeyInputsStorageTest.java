package com.f1x.mtcdtools;

import com.f1x.mtcdtools.keys.input.KeyInput;
import com.f1x.mtcdtools.keys.input.KeyInputType;
import com.f1x.mtcdtools.keys.storage.KeyInputsStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by COMPUTER on 2016-08-01.
 */
public class KeyInputsStorageTest {
    public KeyInputsStorageTest() throws JSONException {
        mMockKeyInputsReader = new MockKeyInputsReader();
        mMockKeyInputsWriter = new MockKeyInputsWriter();
        mKeyInputsStorage = new KeyInputsStorage(mMockKeyInputsReader, mMockKeyInputsWriter);

        mTestKeyInputs = createTestKeyInputs();
        mTestJsonObject = createTestJsonObject(mTestKeyInputs);
    }

    @Test
    public void parseJsonString() throws JSONException, IOException {
        mMockKeyInputsReader.setInput(mTestJsonObject.toString());
        mKeyInputsStorage.read();

        assertEquals(mTestKeyInputs, mKeyInputsStorage.getInputs());
    }

    @Test
    public void insertElement() throws JSONException, IOException {
        mKeyInputsStorage.read();

        Map<Integer, KeyInput> testKeyInputs = new HashMap<>();

        for(Map.Entry<Integer, KeyInput> testInput : mTestKeyInputs.entrySet()) {
            mKeyInputsStorage.insert(testInput.getValue());
            testKeyInputs.put(testInput.getKey(), testInput.getValue());
            assertEquals(testKeyInputs, mKeyInputsStorage.getInputs());

            JSONObject testJsonObject = createTestJsonObject(testKeyInputs);
            assertEquals(testJsonObject.toString(), mMockKeyInputsWriter.getOutput());
        }
    }

    @Test
    public void removeElement() throws JSONException, IOException {
        mKeyInputsStorage.read();

        for (Map.Entry<Integer, KeyInput> testInput : mTestKeyInputs.entrySet()) {
            mKeyInputsStorage.insert(testInput.getValue());
        }

        Iterator iter = mTestKeyInputs.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry inputEntry = (Map.Entry)iter.next();

            mKeyInputsStorage.remove((KeyInput)inputEntry.getValue());
            iter.remove();
            assertEquals(mTestKeyInputs, mKeyInputsStorage.getInputs());

            JSONObject testJsonObject = createTestJsonObject(mTestKeyInputs);
            assertEquals(testJsonObject.toString(), mMockKeyInputsWriter.getOutput());
        }
    }

    private static Map<Integer, KeyInput> createTestKeyInputs() {
        Map<Integer, KeyInput> inputs = new HashMap<>();

        for(int i = 1; i < 5; ++i) {
            KeyInput input = new KeyInput(i, KeyInputType.values()[i], "com.test.command" + i);
            inputs.put(i, input);
        }

        return inputs;
    }

    private static JSONObject createTestJsonObject(Map<Integer, KeyInput> testKeyInputs) throws JSONException {
        JSONArray inputsArray = new JSONArray();
        for (Map.Entry<Integer, KeyInput> testInput : testKeyInputs.entrySet()) {
            inputsArray.put(testInput.getValue().toJson());
        }

        JSONObject testJson = new JSONObject();
        testJson.put(KeyInputsStorage.INPUTS_ARRAY_NAME, inputsArray);

        return testJson;
    }

    private final MockKeyInputsReader mMockKeyInputsReader;
    private final MockKeyInputsWriter mMockKeyInputsWriter;
    private final KeyInputsStorage mKeyInputsStorage;
    private final Map<Integer, KeyInput> mTestKeyInputs;
    private final JSONObject mTestJsonObject;
}
