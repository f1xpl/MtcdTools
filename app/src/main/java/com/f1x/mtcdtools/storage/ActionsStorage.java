package com.f1x.mtcdtools.storage;

import android.content.Context;

import com.f1x.mtcdtools.actions.Action;
import com.f1x.mtcdtools.actions.ActionsFactory;
import com.f1x.mtcdtools.storage.exceptions.DuplicatedEntryException;
import com.f1x.mtcdtools.storage.exceptions.ActionCreationFailed;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by COMPUTER on 2017-01-15.
 */

public class ActionsStorage extends Storage {
    public ActionsStorage(FileReader reader, FileWriter writer, Context context) {
        super(reader, writer);

        mActions = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        mContext = context;
    }

    public void read() throws JSONException, IOException, DuplicatedEntryException, ActionCreationFailed {
        JSONArray actionsArray = read(STORAGE_FILE_NAME, ROOT_ARRAY_NAME);

        for (int i = 0; i < actionsArray.length(); ++i) {
            JSONObject actionJson = actionsArray.getJSONObject(i);
            Action action = ActionsFactory.createAction(actionJson);

            if(action == null) {
                throw new ActionCreationFailed(actionJson.getString(Action.NAME_PROPERTY));
            } else if(mActions.containsKey(action.getName())) {
                throw new DuplicatedEntryException("action name: " + action.getName() + ", action type: " + action.getType());
            } else {
                mActions.put(action.getName(), action);
            }
        }
    }

    private void write() throws IOException, JSONException {
        JSONArray actionsArray = new JSONArray();
        for (Map.Entry<String, Action> action : mActions.entrySet()) {
            actionsArray.put(action.getValue().toJson());
        }

        write(STORAGE_FILE_NAME, ROOT_ARRAY_NAME, actionsArray);
    }

    public void insert(Action action) throws JSONException, IOException, DuplicatedEntryException {
        if(mActions.containsKey(action.getName())) {
            throw new DuplicatedEntryException("action name: " + action.getName() + ", action type: " + action.getType());
        } else {
            mActions.put(action.getName(), action);
            write();
        }
    }

    public void remove(String actionName) throws IOException, JSONException {
        if(mActions.containsKey(actionName)) {
            mActions.remove(actionName);
            write();
        }
    }

    public void replace(String actionName, Action action) throws JSONException, IOException, DuplicatedEntryException {
        if(!actionName.equalsIgnoreCase(action.getName()) && mActions.containsKey(action.getName())) {
            throw new DuplicatedEntryException("action name: " + action.getName() + ", action type: " + action.getType());
        }

        remove(actionName);
        insert(action);
    }

    public Action getAction(String actionName) {
        return mActions.containsKey(actionName )? mActions.get(actionName) : null;
    }

    public Map<String, Action> getActions() {
        return new TreeMap<>(mActions);
    }

    private final Map<String, Action> mActions;
    private final Context mContext;

    public static final String STORAGE_FILE_NAME = "actions.json";
    public static final String ROOT_ARRAY_NAME = "actions";
}
