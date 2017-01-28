package com.f1x.mtcdtools.storage;

import com.f1x.mtcdtools.input.KeysSequenceBinding;
import com.f1x.mtcdtools.input.KeysSequenceConverter;
import com.f1x.mtcdtools.storage.exceptions.DuplicatedEntryException;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by COMPUTER on 2017-01-16.
 */

public class KeysSequenceBindingsStorage extends Storage {
    public KeysSequenceBindingsStorage(FileReader reader, FileWriter writer) {
        super(reader, writer);

        mKeysSequenceBindings = new HashMap<>();
    }

    public void read() throws JSONException, IOException, DuplicatedEntryException {
        JSONArray keysSequenceBindingsArray = read(STORAGE_FILE_NAME, ROOT_ARRAY_NAME);

        for (int i = 0; i < keysSequenceBindingsArray.length(); ++i) {
            KeysSequenceBinding keysSequenceBinding = new KeysSequenceBinding(keysSequenceBindingsArray.getJSONObject(i));

            if(mKeysSequenceBindings.containsKey(keysSequenceBinding.getKeysSequence())) {
                throw new DuplicatedEntryException(KeysSequenceConverter.toJsonArray(keysSequenceBinding.getKeysSequence()).toString());
            } else {
                mKeysSequenceBindings.put(keysSequenceBinding.getKeysSequence(), keysSequenceBinding);
            }
        }
    }

    private void write() throws IOException, JSONException {
        JSONArray keysSequenceBindingsArray = new JSONArray();
        for(Map.Entry<List<Integer>, KeysSequenceBinding> entry : mKeysSequenceBindings.entrySet()) {
            keysSequenceBindingsArray.put(entry.getValue().toJson());
        }

        write(STORAGE_FILE_NAME, ROOT_ARRAY_NAME, keysSequenceBindingsArray);
    }

    public void insert(KeysSequenceBinding keysSequenceBinding) throws JSONException, IOException, DuplicatedEntryException {
        if(mKeysSequenceBindings.containsKey(keysSequenceBinding.getKeysSequence())) {
            throw new DuplicatedEntryException("KeysSequenceBinding");
        } else {
            mKeysSequenceBindings.put(keysSequenceBinding.getKeysSequence(), keysSequenceBinding);
            write();
        }
    }

    public void remove(KeysSequenceBinding keysSequenceBinding) throws IOException, JSONException {
        if(mKeysSequenceBindings.containsKey(keysSequenceBinding.getKeysSequence())) {
            mKeysSequenceBindings.remove(keysSequenceBinding.getKeysSequence());
            write();
        }
    }

    public KeysSequenceBinding getKeysSequenceBinding(List<Integer> keysSequence) {
        return mKeysSequenceBindings.containsKey(keysSequence) ? mKeysSequenceBindings.get(keysSequence) : null;
    }

    Map<List<Integer>, KeysSequenceBinding> getKeysSequenceBindings() {
        return new HashMap<>(mKeysSequenceBindings);
    }

    private final Map<List<Integer>, KeysSequenceBinding> mKeysSequenceBindings;

    public static final String STORAGE_FILE_NAME = "keysSequenceBindings.json";
    public static final String ROOT_ARRAY_NAME = "keysSequenceBindings";
}
