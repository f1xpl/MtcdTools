package com.f1x.mtcdtools.storage;

import com.f1x.mtcdtools.ActionsList;
import com.f1x.mtcdtools.storage.exceptions.DuplicatedEntryException;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by COMPUTER on 2017-01-29.
 */

public class ActionsListsStorage extends Storage<String, ActionsList> {
    public ActionsListsStorage(FileReader reader, FileWriter writer) {
        super(reader, writer);
    }

    @Override
    public void read() throws JSONException, IOException, DuplicatedEntryException {
        JSONArray actionsSequencesArray = read(STORAGE_FILE_NAME, ROOT_ARRAY_NAME);

        for (int i = 0; i < actionsSequencesArray.length(); ++i) {
            ActionsList actionsList = new ActionsList(actionsSequencesArray.getJSONObject(i));
            put(actionsList.getName(), actionsList);
        }
    }

    @Override
    public void write() throws JSONException, IOException {
        JSONArray actionsSequencesArray = new JSONArray();

        for(Map.Entry<String, ActionsList> entry : mItems.entrySet()) {
            actionsSequencesArray.put(entry.getValue().toJson());
        }

        write(STORAGE_FILE_NAME, ROOT_ARRAY_NAME, actionsSequencesArray);
    }

    @Override
    protected TreeMap<String, ActionsList> createContainer() {
        return new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    }

    @Override
    protected boolean keysEqual(String left, String right) {
        return left.equalsIgnoreCase(right);
    }

    public static final String STORAGE_FILE_NAME = "actionsSequences.json";
    public static final String ROOT_ARRAY_NAME = "actionsSequences";
}
