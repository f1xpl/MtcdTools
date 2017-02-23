package com.f1x.mtcdtools.named.objects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by f1x on 2017-02-05.
 */

public class NamedObjectsContainer extends NamedObject {
    NamedObjectsContainer(JSONObject json) throws JSONException {
        super(json);
        mActionIds = new ArrayList<>();
        JSONArray actionsArray = json.getJSONArray(ACTIONS_PROPERTY);

        for (int i = 0; i < actionsArray.length(); ++i) {
            mActionIds.add(new NamedObjectId(actionsArray.getString(i)));
        }
    }

    NamedObjectsContainer(NamedObjectId id, String objectType, List<NamedObjectId> actionIds) {
        super(id, objectType);
        mActionIds = actionIds;
    }

    public List<NamedObjectId> getActionIds() {
        return new ArrayList<>(mActionIds);
    }

    @Override
    public JSONObject toJson() throws JSONException {
        JSONObject json = super.toJson();

        JSONArray actionsArray = new JSONArray();
        for (NamedObjectId id : mActionIds) {
            actionsArray.put(id);
        }

        json.put(ACTIONS_PROPERTY, actionsArray);

        return json;
    }

    @Override
    public void removeDependency(NamedObjectId id) {
        mActionIds.remove(id);
    }

    @Override
    public void replaceDependency(NamedObjectId oldId, NamedObjectId newId) {
        if(mActionIds.contains(oldId)) {
            int index = mActionIds.indexOf(oldId);
            mActionIds.set(index, newId);
        }
    }

    protected List<NamedObjectId> mActionIds;

    static public final String ACTIONS_PROPERTY = "actions";
}
