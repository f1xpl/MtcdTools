package com.f1x.mtcdtools.storage;

import com.f1x.mtcdtools.storage.exceptions.DuplicatedEntryException;
import com.f1x.mtcdtools.storage.exceptions.EntryCreationFailed;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by COMPUTER on 2017-02-26.
 */

public abstract class FileStorage {
    public FileStorage(FileReader reader, FileWriter writer) {
        mReader = reader;
        mWriter = writer;
    }

    protected final void write(String fileName, String arrayName, JSONArray array) throws IOException, JSONException {
        JSONObject inputsJson = new JSONObject();
        inputsJson.put(arrayName, array);
        mWriter.write(inputsJson.toString(), fileName, CHARSET);
    }

    protected final JSONArray read(String fileName, String arrayName) throws IOException, JSONException {
        String inputString = mReader.read(fileName, CHARSET);
        if(!inputString.isEmpty()) {
            JSONObject inputsJson = new JSONObject(inputString);
            return inputsJson.getJSONArray(arrayName);
        }

        return new JSONArray();
    }

    public abstract void read() throws JSONException, IOException, DuplicatedEntryException, EntryCreationFailed;
    public abstract void write() throws JSONException, IOException;

    private final FileReader mReader;
    private final FileWriter mWriter;
    private static final String CHARSET = "UTF-8";
}
