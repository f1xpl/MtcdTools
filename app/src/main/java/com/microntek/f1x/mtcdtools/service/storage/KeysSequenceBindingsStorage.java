package com.microntek.f1x.mtcdtools.service.storage;

import com.microntek.f1x.mtcdtools.service.input.KeysSequenceBinding;
import com.microntek.f1x.mtcdtools.named.NamedObjectId;
import com.microntek.f1x.mtcdtools.service.storage.exceptions.DuplicatedEntryException;
import com.microntek.f1x.mtcdtools.service.storage.exceptions.EntryCreationFailed;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by f1x on 2017-01-29.
 */

public class KeysSequenceBindingsStorage extends UniqueObjectsStorage<List<Integer>, KeysSequenceBinding> {
    public KeysSequenceBindingsStorage(FileReader reader, FileWriter writer) {
        super(reader, writer);
    }

    @Override
    public void read() throws JSONException, IOException, DuplicatedEntryException, EntryCreationFailed {
        JSONArray keysSequenceBindingsArray = read(STORAGE_FILE_NAME, ROOT_ARRAY_NAME);

        for (int i = 0; i < keysSequenceBindingsArray.length(); ++i) {
            KeysSequenceBinding keysSequenceBinding = new KeysSequenceBinding(keysSequenceBindingsArray.getJSONObject(i));

            put(keysSequenceBinding.getKeysSequence(), keysSequenceBinding);
        }
    }

    @Override
    public void write() throws JSONException, IOException {
        JSONArray keysSequenceBindingsArray = new JSONArray();
        for(Map.Entry<List<Integer>, KeysSequenceBinding> entry : mItems.entrySet()) {
            keysSequenceBindingsArray.put(entry.getValue().toJson());
        }

        write(STORAGE_FILE_NAME, ROOT_ARRAY_NAME, keysSequenceBindingsArray);
    }

    public void removeBindingWithTarget(NamedObjectId targetId) throws IOException, JSONException {
        Iterator<Map.Entry<List<Integer>, KeysSequenceBinding>> iter = mItems.entrySet().iterator();

        while(iter.hasNext()) {
            Map.Entry<List<Integer>, KeysSequenceBinding> entry = iter.next();

            KeysSequenceBinding keysSequenceBinding = entry.getValue();
            if(keysSequenceBinding.getTargetId().equals(targetId)) {
                iter.remove();
            }
        }

        write();
    }

    public void replaceTarget(NamedObjectId oldId, NamedObjectId newId) throws IOException, JSONException {
        for(Map.Entry<List<Integer>, KeysSequenceBinding> entry : mItems.entrySet()) {
            KeysSequenceBinding keysSequenceBinding = entry.getValue();

            if(keysSequenceBinding.getTargetId().equals(oldId)) {
                keysSequenceBinding.setTargetId(newId);
            }
        }

        write();
    }

    public static final String STORAGE_FILE_NAME = "keysSequenceBindings.json";
    public static final String ROOT_ARRAY_NAME = "keysSequenceBindings";
}
