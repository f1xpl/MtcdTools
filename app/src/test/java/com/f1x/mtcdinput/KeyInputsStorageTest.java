package com.f1x.mtcdinput;

import com.f1x.mtcdinput.keyinputs.KeyInput;
import com.f1x.mtcdinput.keyinputs.KeyInputType;
import com.f1x.mtcdinput.keyinputs.KeyInputsStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by COMPUTER on 2016-08-01.
 */
public class KeyInputsStorageTest {
    public KeyInputsStorageTest() throws JSONException {
        mMockKeyInputsReader = new MockKeyInputsReader();
        mMockKeyInputsWriter = new MockKeyInputsWriter();

        mTestKeyInputs = createTesKeyInputs();
        mTestJsonObject = createTestJsonObject(mTestKeyInputs);
    }

    @Test
    public void parseJsonString() throws JSONException, IOException {
        mMockKeyInputsReader.setInput(mTestJsonObject.toString());

        KeyInputsStorage keyInputsStorage = new KeyInputsStorage(mMockKeyInputsReader, mMockKeyInputsWriter);
        assertEquals(mTestKeyInputs, keyInputsStorage.getInputs());
    }

    @Test
    public void insertElement() throws JSONException, IOException {
        KeyInputsStorage keyInputsStorage = new KeyInputsStorage(mMockKeyInputsReader, mMockKeyInputsWriter);
        List<KeyInput> testKeyInputs = new ArrayList<>();

        for(KeyInput testInput : mTestKeyInputs) {
            keyInputsStorage.insert(testInput);
            testKeyInputs.add(testInput);
            assertEquals(testKeyInputs, keyInputsStorage.getInputs());

            JSONObject testJsonObject = createTestJsonObject(testKeyInputs);
            assertEquals(testJsonObject.toString(), mMockKeyInputsWriter.getOutput());
        }
    }

    @Test
    public void removeElement() throws JSONException, IOException {
        KeyInputsStorage keyInputsStorage = new KeyInputsStorage(mMockKeyInputsReader, mMockKeyInputsWriter);

        for(KeyInput testInput : mTestKeyInputs) {
            keyInputsStorage.insert(testInput);
        }

        Iterator<KeyInput> iter = mTestKeyInputs.iterator();
        while (iter.hasNext()) {
            keyInputsStorage.remove(iter.next());
            iter.remove();

            assertEquals(mTestKeyInputs, keyInputsStorage.getInputs());
            JSONObject testJsonObject = createTestJsonObject(mTestKeyInputs);
            assertEquals(testJsonObject.toString(), mMockKeyInputsWriter.getOutput());
        }
    }

    private final static List<KeyInput> createTesKeyInputs() {
        List<KeyInput> inputs = new ArrayList<>();

        for(int i = 1; i < 5; ++i) {
            KeyInput input = new KeyInput(i, KeyInputType.values()[i], "com.test.command" + i);
            inputs.add(input);
        }

        return inputs;
    }

    private final static JSONObject createTestJsonObject(List<KeyInput> testKeyInputs) throws JSONException {
        JSONArray inputsArray = new JSONArray();
        for(KeyInput testInput : testKeyInputs) {
            inputsArray.put(testInput.toJson());
        }

        JSONObject testJson = new JSONObject();
        testJson.put(KeyInputsStorage.INPUTS_ARRAY_NAME, inputsArray);

        return testJson;
    }

    private final MockKeyInputsReader mMockKeyInputsReader;
    private final MockKeyInputsWriter mMockKeyInputsWriter;
    private final List<KeyInput> mTestKeyInputs;
    private final JSONObject mTestJsonObject;
}
