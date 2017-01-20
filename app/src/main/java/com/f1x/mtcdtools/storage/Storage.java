package com.f1x.mtcdtools.storage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by COMPUTER on 2016-08-15.
 */
public class Storage {
    public Storage(FileReader reader, FileWriter writer) {
        mReader = reader;
        mWriter = writer;
    }

    protected final JSONArray read(String fileName, String arrayName) throws IOException, JSONException {
        String inputString = mReader.read(fileName, CHARSET);
        if(!inputString.isEmpty()) {
            JSONObject inputsJson = new JSONObject(inputString);
            return inputsJson.getJSONArray(arrayName);
        }

        return new JSONArray();
    }

    protected final void write(String fileName, String arrayName, JSONArray array) throws IOException, JSONException {
        JSONObject inputsJson = new JSONObject();
        inputsJson.put(arrayName, array);
        mWriter.write(inputsJson.toString(), fileName, CHARSET);
    }

    protected final FileReader mReader;
    protected final FileWriter mWriter;

    private static final String CHARSET = "UTF-8";
}
