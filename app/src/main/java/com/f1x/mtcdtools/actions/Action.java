package com.f1x.mtcdtools.actions;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by COMPUTER on 2017-01-09.
 */

public abstract class Action {
    Action(JSONObject json) throws JSONException {
        mName = json.getString(NAME_PROPERTY);
        mType = json.getString(TYPE_PROPERTY);
    }

    public abstract void evaluate();

    public String getType() {
        return mType;
    }

    public String getName() {
        return mName;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(NAME_PROPERTY, mName);
        json.put(TYPE_PROPERTY, mType);

        return json;
    }

    private final String mName;
    private final String mType;

    static public final String NAME_PROPERTY = "name";
    static public final String TYPE_PROPERTY = "type";
}
