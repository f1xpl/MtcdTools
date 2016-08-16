package com.f1x.mtcdtools;

import com.f1x.mtcdtools.input.KeyInput;
import com.f1x.mtcdtools.input.KeyInputType;
import com.f1x.mtcdtools.storage.KeyInputsStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by COMPUTER on 2016-08-01.
 */
public class KeyInputsStorageTest {
    public KeyInputsStorageTest() throws JSONException {
        mMockFileReader = new MockFileReader();
        mMockFileWriter = new MockFileWriter();
        mKeyInputsStorage = new KeyInputsStorage(mMockFileReader, mMockFileWriter);

        mSampleKeyInputs = new HashMap<>();
        for(int i = 1; i < 5; ++i) {
            KeyInput input = new KeyInput(i, KeyInputType.values()[i], "com.test.command" + i);
            mSampleKeyInputs.put(i, input);
        }
    }

    @Test
    public void testEmptyFileInput() throws IOException, JSONException {
        mMockFileReader.setInput("");
        mKeyInputsStorage.read();
        assertTrue(mKeyInputsStorage.getInputs().isEmpty());
    }

    @Test
    public void testEmptyList() throws IOException, JSONException {
        mSampleKeyInputs.clear();
        JSONObject testJsonObject = createTestJsonObject(mSampleKeyInputs);

        mMockFileReader.setInput(testJsonObject.toString());
        mKeyInputsStorage.read();
        assertTrue(mKeyInputsStorage.getInputs().isEmpty());
    }

    @Test
    public void parseJsonString() throws JSONException, IOException {
        JSONObject testJsonObject = createTestJsonObject(mSampleKeyInputs);

        mMockFileReader.setInput(testJsonObject.toString());
        mKeyInputsStorage.read();

        assertEquals(mSampleKeyInputs, mKeyInputsStorage.getInputs());
    }

    @Test
    public void insertElement() throws JSONException, IOException {
        mKeyInputsStorage.read();

        Map<Integer, KeyInput> testKeyInputs = new HashMap<>();

        for(Map.Entry<Integer, KeyInput> testInput : mSampleKeyInputs.entrySet()) {
            mKeyInputsStorage.insert(testInput.getValue());
            testKeyInputs.put(testInput.getKey(), testInput.getValue());
            assertEquals(testKeyInputs, mKeyInputsStorage.getInputs());

            JSONObject testJsonObject = createTestJsonObject(testKeyInputs);
            assertEquals(testJsonObject.toString(), mMockFileWriter.getOutput());
        }
    }

    @Test
    public void removeElement() throws JSONException, IOException {
        mKeyInputsStorage.read();

        for (Map.Entry<Integer, KeyInput> testInput : mSampleKeyInputs.entrySet()) {
            mKeyInputsStorage.insert(testInput.getValue());
        }

        Iterator iter = mSampleKeyInputs.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry inputEntry = (Map.Entry)iter.next();

            mKeyInputsStorage.remove((KeyInput)inputEntry.getValue());
            iter.remove();
            assertEquals(mSampleKeyInputs, mKeyInputsStorage.getInputs());

            JSONObject testJsonObject = createTestJsonObject(mSampleKeyInputs);
            assertEquals(testJsonObject.toString(), mMockFileWriter.getOutput());
        }
    }

    private static JSONObject createTestJsonObject(Map<Integer, KeyInput> sampleKeyInputs) throws JSONException {
        JSONArray inputsArray = new JSONArray();
        for (Map.Entry<Integer, KeyInput> testInput : sampleKeyInputs.entrySet()) {
            inputsArray.put(testInput.getValue().toJson());
        }

        JSONObject testJson = new JSONObject();
        testJson.put("inputs", inputsArray);

        return testJson;
    }

    private final MockFileReader mMockFileReader;
    private final MockFileWriter mMockFileWriter;
    private final KeyInputsStorage mKeyInputsStorage;
    private final Map<Integer, KeyInput> mSampleKeyInputs;
}
