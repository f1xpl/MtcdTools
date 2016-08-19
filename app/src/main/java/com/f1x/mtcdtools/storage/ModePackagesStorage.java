package com.f1x.mtcdtools.storage;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by COMPUTER on 2016-08-15.
 */
public class ModePackagesStorage extends Storage {
    public ModePackagesStorage(FileReaderInterface reader, FileWriterInterface writer) {
        super(reader, writer);
        mPackages = new ArrayList<>();
    }

    public void setPackages(List<String> packages) throws IOException, JSONException {
        mPackages = packages;
        write();
    }

    public List<String> getPackages() {
        return new ArrayList<>(mPackages);
    }

    public void read() throws IOException, JSONException {
        JSONArray inputsArray = read(STORAGE_FILE_NAME, ROOT_ARRAY_NAME);

        for (int i = 0; i < inputsArray.length(); ++i) {
            mPackages.add(inputsArray.getString(i));
        }
    }

    private void write() throws IOException, JSONException {
        JSONArray inputsArray = new JSONArray();

        for(String packageName : mPackages) {
            inputsArray.put(packageName);
        }

        write(STORAGE_FILE_NAME, ROOT_ARRAY_NAME, inputsArray);
    }

    private List<String> mPackages;

    private static final String STORAGE_FILE_NAME = "modePackages.json";
    private static final String ROOT_ARRAY_NAME = "packages";
}
