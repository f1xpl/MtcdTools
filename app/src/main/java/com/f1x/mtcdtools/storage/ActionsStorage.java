package com.f1x.mtcdtools.storage;

import com.f1x.mtcdtools.actions.Action;
import com.f1x.mtcdtools.actions.ActionsFactory;
import com.f1x.mtcdtools.storage.exceptions.DuplicatedEntryException;
import com.f1x.mtcdtools.storage.exceptions.EntryCreationFailed;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by COMPUTER on 2017-01-29.
 */

public class ActionsStorage extends Storage<String, Action> {
    public ActionsStorage(FileReader reader, FileWriter writer) {
        super(reader, writer);
    }

    @Override
    protected TreeMap<String, Action> createContainer() {
        return new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    }

    @Override
    protected boolean keysEqual(String left, String right) {
        return left.equalsIgnoreCase(right);
    }

    public void read() throws JSONException, IOException, DuplicatedEntryException, EntryCreationFailed {
        JSONArray actionsArray = read(STORAGE_FILE_NAME, ROOT_ARRAY_NAME);

        for (int i = 0; i < actionsArray.length(); ++i) {
            JSONObject actionJson = actionsArray.getJSONObject(i);
            Action action = ActionsFactory.createAction(actionJson);

            if(action == null) {
                throw new EntryCreationFailed(actionJson.getString(Action.NAME_PROPERTY));
            } else {
                put(action.getName(), action);
            }
        }
    }

    public void write() throws IOException, JSONException {
        JSONArray actionsArray = new JSONArray();
        for (Map.Entry<String, Action> action : mItems.entrySet()) {
            actionsArray.put(action.getValue().toJson());
        }

        write(STORAGE_FILE_NAME, ROOT_ARRAY_NAME, actionsArray);
    }

    public static final String STORAGE_FILE_NAME = "actions.json";
    public static final String ROOT_ARRAY_NAME = "actions";
}
