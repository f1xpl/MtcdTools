package com.f1x.mtcdtools.storage;

import com.f1x.mtcdtools.input.KeysSequenceBinding;
import com.f1x.mtcdtools.storage.exceptions.DuplicatedEntryException;
import com.f1x.mtcdtools.storage.exceptions.EntryCreationFailed;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by COMPUTER on 2017-01-29.
 */

public class KeysSequenceBindingsStorage extends Storage<List<Integer>, KeysSequenceBinding> {
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

    @Override
    protected Map<List<Integer>, KeysSequenceBinding> createContainer() {
        return new HashMap<>();
    }

    public void removeBindingsWithAction(String actionName) throws IOException, JSONException {
        removeBindingWithTarget(KeysSequenceBinding.TARGET_TYPE_ACTION, actionName);
    }

    public void removeBindingsWithActionsList(String actionsListName) throws IOException, JSONException {
        removeBindingWithTarget(KeysSequenceBinding.TARGET_TYPE_ACTIONS_LIST, actionsListName);
    }

    private void removeBindingWithTarget(String targetType, String targetName) throws IOException, JSONException {
        Iterator<Map.Entry<List<Integer>, KeysSequenceBinding>> iter = mItems.entrySet().iterator();

        while(iter.hasNext()) {
            Map.Entry<List<Integer>, KeysSequenceBinding> entry = iter.next();

            KeysSequenceBinding keysSequenceBinding = entry.getValue();
            if(keysSequenceBinding.getTargetType().equals(targetType) && keysSequenceBinding.getTargetName().equals(targetName)) {
                iter.remove();
            }
        }

        write();
    }

    public void replaceActionName(String oldName, String newName) throws IOException, JSONException {
        replaceBindingTarget(KeysSequenceBinding.TARGET_TYPE_ACTION, oldName, newName);
    }

    public void replaceActionsListName(String oldName, String newName) throws IOException, JSONException {
        replaceBindingTarget(KeysSequenceBinding.TARGET_TYPE_ACTIONS_LIST, oldName, newName);
    }

    private void replaceBindingTarget(String targetType, String oldName, String newName) throws IOException, JSONException {
        for(Map.Entry<List<Integer>, KeysSequenceBinding> entry : mItems.entrySet()) {
            KeysSequenceBinding keysSequenceBinding = entry.getValue();

            if(keysSequenceBinding.getTargetType().equals(targetType) && keysSequenceBinding.getTargetName().equals(oldName)) {
                keysSequenceBinding.setTargetName(newName);
            }
        }

        write();
    }

    public static final String STORAGE_FILE_NAME = "keysSequenceBindings.json";
    public static final String ROOT_ARRAY_NAME = "keysSequenceBindings";
}
