package com.f1x.mtcdtools.storage;

import com.f1x.mtcdtools.ActionsList;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by COMPUTER on 2017-01-16.
 */

public class ActionsListStorage extends Storage {
    public ActionsListStorage(FileReader reader, FileWriter writer) {
        super(reader, writer);

        mActionsLists = new HashMap<>();
    }

    public void read() throws JSONException, IOException {
        JSONArray actionsSequencesArray = read(STORAGE_FILE_NAME, ROOT_ARRAY_NAME);

        for (int i = 0; i < actionsSequencesArray.length(); ++i) {
            ActionsList actionsList = new ActionsList(actionsSequencesArray.getJSONObject(i));
            mActionsLists.put(actionsList.getName(), actionsList);
        }
    }

    private void write() throws IOException, JSONException {
        JSONArray actionsSequencesArray = new JSONArray();

        for(Map.Entry<String, ActionsList> entry : mActionsLists.entrySet()) {
            actionsSequencesArray.put(entry.getValue().toJson());
        }

        write(STORAGE_FILE_NAME, ROOT_ARRAY_NAME, actionsSequencesArray);
    }

    private final Map<String, ActionsList> mActionsLists;

    public static final String STORAGE_FILE_NAME = "actionsSequences.json";
    public static final String ROOT_ARRAY_NAME = "actionsSequences";
}
