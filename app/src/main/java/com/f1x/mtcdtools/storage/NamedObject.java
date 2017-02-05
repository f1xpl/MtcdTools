package com.f1x.mtcdtools.storage;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by COMPUTER on 2017-02-05.
 */

public abstract class NamedObject {
    public NamedObject(String name, String objectType) {
        mName = name;
        mObjectType = objectType;
    }

    public NamedObject(JSONObject json) throws JSONException {
        mName = json.getString(NAME_PROPERTY);
        mObjectType = json.getString(OBJECT_TYPE_PROPERTY);
    }

    public String getName() {
        return mName;
    }

    public String getObjectType() {
        return mObjectType;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(NAME_PROPERTY, mName);
        json.put(OBJECT_TYPE_PROPERTY, mObjectType);

        return json;
    }

    public abstract void removeDependency(String dependencyName);
    public abstract void replaceDependency(String oldDependencyName, String newDependencyName);

    private String mName;
    private final String mObjectType;

    static public final String NAME_PROPERTY = "name";
    static public final String OBJECT_TYPE_PROPERTY = "objectType";
}
