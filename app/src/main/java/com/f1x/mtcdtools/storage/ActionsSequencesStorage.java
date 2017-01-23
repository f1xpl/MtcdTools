package com.f1x.mtcdtools.storage;

import com.f1x.mtcdtools.actions.ActionsSequence;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by COMPUTER on 2017-01-16.
 */

public class ActionsSequencesStorage extends Storage {
    public ActionsSequencesStorage(FileReader reader, FileWriter writer) {
        super(reader, writer);

        mActionsSequences = new HashMap<>();
    }

    public void read() throws Exception {
        JSONArray actionsSequencesArray = read(STORAGE_FILE_NAME, ROOT_ARRAY_NAME);

        for (int i = 0; i < actionsSequencesArray.length(); ++i) {
            ActionsSequence actionsSequence = new ActionsSequence(actionsSequencesArray.getJSONObject(i));
            mActionsSequences.put(actionsSequence.getName(), actionsSequence);
        }
    }

    private void write() throws IOException, JSONException {
        JSONArray actionsSequencesArray = new JSONArray();

        for(Map.Entry<String, ActionsSequence> entry : mActionsSequences.entrySet()) {
            actionsSequencesArray.put(entry.getValue().toJson());
        }

        write(STORAGE_FILE_NAME, ROOT_ARRAY_NAME, actionsSequencesArray);
    }

    private final Map<String, ActionsSequence> mActionsSequences;

    public static final String STORAGE_FILE_NAME = "actionsSequences.json";
    public static final String ROOT_ARRAY_NAME = "actionsSequences";
}
