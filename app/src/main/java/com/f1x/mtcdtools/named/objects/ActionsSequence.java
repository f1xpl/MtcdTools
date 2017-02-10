package com.f1x.mtcdtools.named.objects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by f1x on 2017-02-05.
 */

public class ActionsSequence extends NamedObjectsContainer {
    public ActionsSequence(JSONObject json) throws JSONException {
        super(json);

        mDelays = new ArrayList<>();

        try {
            JSONArray delaysArray = json.getJSONArray(ACTION_DELAYS_PROPERTY);

            for (int i = 0; i < delaysArray.length(); ++i) {
                JSONObject delayJson = delaysArray.getJSONObject(i);
                mDelays.add(new AbstractMap.SimpleEntry<>(delayJson.getString(NAME_PROPERTY), delayJson.getInt(ACTION_DELAY_PROPERTY)));
            }
        } catch (JSONException e) { // Backward compatibility with version 1.3
            e.printStackTrace();

            for(String actionName : this.getActionsNames()) {
                mDelays.add(new AbstractMap.SimpleEntry<>(actionName, 0));
            }
        }
    }

    public ActionsSequence(String name, List<String> actionsNames, List<Map.Entry<String, Integer>> actionDelays) {
        super(name, OBJECT_TYPE, actionsNames);
        mDelays = actionDelays;
    }

    public int getDelayForAction(int index) {
        return (index != -1 && index < mDelays.size()) ? mDelays.get(index).getValue() : 0;
    }

    @Override
    public void removeDependency(String dependencyName) {
        super.removeDependency(dependencyName);

        Iterator<Map.Entry<String, Integer>> iterator = mDelays.iterator();

        while(iterator.hasNext()) {
            Map.Entry<String, Integer> entry = iterator.next();

            if(entry.getKey().equalsIgnoreCase(dependencyName)) {
                iterator.remove();
            }
        }
    }

    @Override
    public void replaceDependency(String oldDependencyName, String newDependencyName) {
        super.replaceDependency(oldDependencyName, newDependencyName);

        for(int i = 0; i < mDelays.size(); ++i) {
            Map.Entry<String, Integer> entry = mDelays.get(i);

            if(entry.getKey().equalsIgnoreCase(oldDependencyName)) {
                mDelays.set(i, new AbstractMap.SimpleEntry<>(newDependencyName, entry.getValue()));
            }
        }
    }

    @Override
    public JSONObject toJson() throws JSONException {
        JSONObject json = super.toJson();
        JSONArray delaysArray = new JSONArray();

        for(int i = 0; i < mDelays.size(); ++i) {
            Map.Entry<String, Integer> entry = mDelays.get(i);
            JSONObject delayJson = new JSONObject();

            delayJson.put(NAME_PROPERTY, entry.getKey());
            delayJson.put(ACTION_DELAY_PROPERTY, entry.getValue());
            delaysArray.put(delayJson);
        }

        json.put(ACTION_DELAYS_PROPERTY, delaysArray);

        return json;
    }

    public List<Map.Entry<String, Integer>> getActionDelays() {
        return new ArrayList<>(mDelays);
    }

    private final List<Map.Entry<String, Integer>> mDelays;

    static public final String ACTION_DELAY_PROPERTY = "actionDelay";
    static public final String ACTION_DELAYS_PROPERTY = "actionDelays";
    static public final String OBJECT_TYPE = "ActionsSequence";
}
