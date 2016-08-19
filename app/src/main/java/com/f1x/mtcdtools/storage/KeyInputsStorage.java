package com.f1x.mtcdtools.storage;

import com.f1x.mtcdtools.input.KeyInput;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by COMPUTER on 2016-08-01.
 */
public class KeyInputsStorage extends Storage {
    public KeyInputsStorage(FileReaderInterface reader, FileWriterInterface writer) {
        super(reader, writer);

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
        JSONArray inputsArray = read(STORAGE_FILE_NAME, ROOT_ARRAY_NAME);

        for (int i = 0; i < inputsArray.length(); ++i) {
            KeyInput input = new KeyInput(inputsArray.getJSONObject(i));
            mInputs.put(input.getKeyCode(), input);
        }
    }

    private void write() throws IOException, JSONException {
        JSONArray inputsArray = new JSONArray();
        for (Map.Entry<Integer, KeyInput> input : mInputs.entrySet()) {
            inputsArray.put(input.getValue().toJson());
        }

        write(STORAGE_FILE_NAME, ROOT_ARRAY_NAME, inputsArray);
    }

    private final Map<Integer, KeyInput> mInputs;

    private static final String STORAGE_FILE_NAME = "keyInputs.json";
    private static final String ROOT_ARRAY_NAME = "inputs";
}
