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
        mActionNames = new ArrayList<>();
        JSONArray actionsArray = json.getJSONArray(ACTIONS_PROPERTY);

        for (int i = 0; i < actionsArray.length(); ++i) {
            mActionNames.add(actionsArray.getString(i));
        }
    }

    NamedObjectsContainer(String name, String objectType, List<String> actionsNames) {
        super(name, objectType);
        mActionNames = actionsNames;
    }

    public List<String> getActionsNames() {
        return new ArrayList<>(mActionNames);
    }

    @Override
    public JSONObject toJson() throws JSONException {
        JSONObject json = super.toJson();

        JSONArray actionsArray = new JSONArray();
        for (String action : mActionNames) {
            actionsArray.put(action);
        }

        json.put(ACTIONS_PROPERTY, actionsArray);

        return json;
    }

    @Override
    public void removeDependency(String dependencyName) {
        mActionNames.remove(dependencyName);
    }

    @Override
    public void replaceDependency(String oldDependencyName, String newDependencyName) {
        if(mActionNames.contains(oldDependencyName)) {
            int index = mActionNames.indexOf(oldDependencyName);
            mActionNames.set(index, newDependencyName);
        }
    }

    private List<String> mActionNames;

    static public final String ACTIONS_PROPERTY = "actions";
}
