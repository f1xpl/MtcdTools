package com.f1x.mtcdtools.named.objects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by f1x on 2017-02-05.
 */

public class ActionsSequence extends NamedObjectsContainer {
    public ActionsSequence(JSONObject json) throws JSONException {
        super(json);

        mDelays = new HashMap<>();

        try {
            JSONArray delaysArray = json.getJSONArray(ACTION_DELAYS_PROPERTY);

            for (int i = 0; i < delaysArray.length(); ++i) {
                JSONObject delayJson = delaysArray.getJSONObject(i);
                mDelays.put(delayJson.getString(NAME_PROPERTY), delayJson.getInt(ACTION_DELAY_PROPERTY));
            }
        } catch (JSONException e) { // Backward compatibility with version 1.3
            e.printStackTrace();

            for(String actionName : this.getActionsNames()) {
                mDelays.put(actionName, 0);
            }
        }
    }

    public ActionsSequence(String name, List<String> actionsNames, Map<String, Integer> actionDelays) {
        super(name, OBJECT_TYPE, actionsNames);
        mDelays = actionDelays;
    }

    public int getDelayForAction(String actionName) {
        return mDelays.containsKey(actionName) ? mDelays.get(actionName) : 0;
    }

    @Override
    public void removeDependency(String dependencyName) {
        super.removeDependency(dependencyName);
        mDelays.remove(dependencyName);
    }

    @Override
    public void replaceDependency(String oldDependencyName, String newDependencyName) {
        super.replaceDependency(oldDependencyName, newDependencyName);

        if(mDelays.containsKey(oldDependencyName)) {
            int delay = mDelays.get(oldDependencyName);
            mDelays.remove(oldDependencyName);
            mDelays.put(newDependencyName, delay);
        }
    }

    @Override
    public JSONObject toJson() throws JSONException {
        JSONObject json = super.toJson();
        JSONArray delaysArray = new JSONArray();

        for(Map.Entry<String, Integer> entry : mDelays.entrySet()) {
            JSONObject delayJson = new JSONObject();

            delayJson.put(NAME_PROPERTY, entry.getKey());
            delayJson.put(ACTION_DELAY_PROPERTY, entry.getValue());
            delaysArray.put(delayJson);
        }

        json.put(ACTION_DELAYS_PROPERTY, delaysArray);

        return json;
    }

    public Map<String, Integer> getActionDelays() {
        return new HashMap<>(mDelays);
    }

    private final Map<String, Integer> mDelays;

    static public final String ACTION_DELAY_PROPERTY = "actionDelay";
    static public final String ACTION_DELAYS_PROPERTY = "actionDelays";
    static public final String OBJECT_TYPE = "ActionsSequence";
}
