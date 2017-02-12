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
                mDelays.add(new AbstractMap.SimpleEntry<>(new NamedObjectId(delayJson.getString(NAME_PROPERTY)), delayJson.getInt(ACTION_DELAY_PROPERTY)));
            }
        } catch (JSONException e) { // Backward compatibility with version 1.3
            e.printStackTrace();

            for(NamedObjectId actionId : this.getActionIds()) {
                mDelays.add(new AbstractMap.SimpleEntry<>(actionId, 0));
            }
        }
    }

    public ActionsSequence(NamedObjectId id, List<NamedObjectId> actionIds, List<Map.Entry<NamedObjectId, Integer>> actionDelays) {
        super(id, OBJECT_TYPE, actionIds);
        mDelays = actionDelays;
    }

    public int getDelayForAction(int index) {
        return (index != -1 && index < mDelays.size()) ? mDelays.get(index).getValue() : 0;
    }

    @Override
    public void removeDependency(NamedObjectId id) {
        super.removeDependency(id);

        Iterator<Map.Entry<NamedObjectId, Integer>> iterator = mDelays.iterator();

        while(iterator.hasNext()) {
            Map.Entry<NamedObjectId, Integer> entry = iterator.next();

            if(entry.getKey().equals(id)) {
                iterator.remove();
            }
        }
    }

    @Override
    public void replaceDependency(NamedObjectId oldId, NamedObjectId newId) {
        super.replaceDependency(oldId, newId);

        for(int i = 0; i < mDelays.size(); ++i) {
            Map.Entry<NamedObjectId, Integer> entry = mDelays.get(i);

            if(entry.getKey().equals(oldId)) {
                mDelays.set(i, new AbstractMap.SimpleEntry<>(newId, entry.getValue()));
            }
        }
    }

    @Override
    public JSONObject toJson() throws JSONException {
        JSONObject json = super.toJson();
        JSONArray delaysArray = new JSONArray();

        for(int i = 0; i < mDelays.size(); ++i) {
            Map.Entry<NamedObjectId, Integer> entry = mDelays.get(i);
            JSONObject delayJson = new JSONObject();

            delayJson.put(NAME_PROPERTY, entry.getKey());
            delayJson.put(ACTION_DELAY_PROPERTY, entry.getValue());
            delaysArray.put(delayJson);
        }

        json.put(ACTION_DELAYS_PROPERTY, delaysArray);

        return json;
    }

    public List<Map.Entry<NamedObjectId, Integer>> getActionDelays() {
        return new ArrayList<>(mDelays);
    }

    private final List<Map.Entry<NamedObjectId, Integer>> mDelays;

    static public final String ACTION_DELAY_PROPERTY = "actionDelay";
    static public final String ACTION_DELAYS_PROPERTY = "actionDelays";
    static public final String OBJECT_TYPE = "ActionsSequence";
}
