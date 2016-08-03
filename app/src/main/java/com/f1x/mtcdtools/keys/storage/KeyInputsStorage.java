package com.f1x.mtcdtools.keys.storage;

import com.f1x.mtcdtools.keys.input.KeyInput;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by COMPUTER on 2016-08-01.
 */
public class KeyInputsStorage {
    public static final String CHARSET = "UTF-8";
    public static final String STORAGE_FILE_NAME = "keyInputs.json";
    public static final String INPUTS_ARRAY_NAME = "inputs";

    public KeyInputsStorage(KeyInputsReaderInterface reader, KeyInputsWriterInterface writer) {
        mReader = reader;
        mWriter = writer;
        mInputs = new HashMap<>();
    }

    public void insert(KeyInput input) throws IOException, JSONException {
        mInputs.put(input.getKeyCode(), input);
        write();
    }

    public void remove(KeyInput input) throws IOException, JSONException {
        mInputs.remove(input.getKeyCode());
        write();
    }

    public Map<Integer, KeyInput> getInputs() {
        return new HashMap<>(mInputs);
    }

    public void read() throws IOException, JSONException {
        String inputString = mReader.read();
        if(!inputString.isEmpty()) {
            JSONObject inputsJson = new JSONObject(inputString);
            JSONArray inputsArray = inputsJson.getJSONArray(INPUTS_ARRAY_NAME);

            for (int i = 0; i < inputsArray.length(); ++i) {
                KeyInput input = new KeyInput(inputsArray.getJSONObject(i));
                mInputs.put(input.getKeyCode(), input);
            }
        }
    }

    private void write() throws IOException, JSONException {
        JSONArray inputsArray = new JSONArray();
        for (Map.Entry<Integer, KeyInput> input : mInputs.entrySet()) {
            inputsArray.put(input.getValue().toJson());
        }

        JSONObject inputsJson = new JSONObject();
        inputsJson.put(INPUTS_ARRAY_NAME, inputsArray);
        mWriter.write(inputsJson.toString());
    }

    private final KeyInputsReaderInterface mReader;
    private final KeyInputsWriterInterface mWriter;
    private final Map<Integer, KeyInput> mInputs;
}
