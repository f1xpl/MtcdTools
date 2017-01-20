package com.f1x.mtcdtools.storage;

import com.f1x.mtcdtools.input.KeysSequenceBinding;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by COMPUTER on 2017-01-16.
 */

public class KeysSequenceBindingsStorage extends Storage {
    public KeysSequenceBindingsStorage(FileReader reader, FileWriter writer) {
        super(reader, writer);

        mKeysSequenceBindings = new ArrayList<>();
    }

    public void read() throws Exception {
        JSONArray keysSequenceBindingsArray = read(STORAGE_FILE_NAME, ROOT_ARRAY_NAME);

        for (int i = 0; i < keysSequenceBindingsArray.length(); ++i) {
            KeysSequenceBinding keysSequenceBinding = new KeysSequenceBinding(keysSequenceBindingsArray.getJSONObject(i));
            mKeysSequenceBindings.add(keysSequenceBinding);
        }
    }

    private void write() throws IOException, JSONException {
        JSONArray keysSequenceBindingsArray = new JSONArray();
        for(int i = 0; i < mKeysSequenceBindings.size(); ++i) {
            keysSequenceBindingsArray.put(mKeysSequenceBindings.get(i).toJson());
        }

        write(STORAGE_FILE_NAME, ROOT_ARRAY_NAME, keysSequenceBindingsArray);
    }

    public void insert(KeysSequenceBinding keysSequenceBinding) throws Exception {
        mKeysSequenceBindings.add(keysSequenceBinding);
        write();
    }

    public void remove(KeysSequenceBinding keysSequenceBinding) throws IOException, JSONException {
        if(mKeysSequenceBindings.contains(keysSequenceBinding)) {
            mKeysSequenceBindings.remove(keysSequenceBinding);
            write();
        }
    }

    List<KeysSequenceBinding> getKeysSequenceBindings() {
        return new ArrayList<>(mKeysSequenceBindings);
    }

    private final List<KeysSequenceBinding> mKeysSequenceBindings;

    public static final String STORAGE_FILE_NAME = "keysSequenceBindings.json";
    public static final String ROOT_ARRAY_NAME = "keysSequenceBindings";
}
