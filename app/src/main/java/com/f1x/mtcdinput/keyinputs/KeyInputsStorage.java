package com.f1x.mtcdinput.keyinputs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by COMPUTER on 2016-08-01.
 */
public class KeyInputsStorage {
    public static final String CHARSET = "UTF-8";
    public static final String STORAGE_FILE_NAME = "keyInputs.json";
    public static final String INPUTS_ARRAY_NAME = "inputs";

    public KeyInputsStorage(KeyInputsReaderInterface reader, KeyInputsWriterInterface writer) throws IOException, JSONException {
        mReader = reader;
        mWriter = writer;
        mInputs = new ArrayList<>();
        read();
    }

    public void insert(KeyInput input) throws IOException, JSONException {
        mInputs.add(input);
        write();
    }

    public void remove(KeyInput input) throws IOException, JSONException {
        mInputs.remove(input);
        write();
    }

    public List<KeyInput> getInputs() {
        return new ArrayList<>(mInputs);
    }

    private void read() throws IOException, JSONException {
        String inputString = mReader.read();
        JSONObject inputsJson = new JSONObject(inputString);
        JSONArray inputsArray = inputsJson.getJSONArray(INPUTS_ARRAY_NAME);

        for(int i = 0; i < inputsArray.length(); ++i) {
            KeyInput input = new KeyInput(inputsArray.getJSONObject(i));
            mInputs.add(input);
        }
    }

    private void write() throws IOException, JSONException {
        JSONArray inputsArray = new JSONArray();
        for(KeyInput input : mInputs) {
            inputsArray.put(input.toJson());
        }

        JSONObject inputsJson = new JSONObject();
        inputsJson.put(INPUTS_ARRAY_NAME, inputsArray);
        mWriter.write(inputsJson.toString());
    }

    private final KeyInputsReaderInterface mReader;
    private final KeyInputsWriterInterface mWriter;
    private final List<KeyInput> mInputs;
}
