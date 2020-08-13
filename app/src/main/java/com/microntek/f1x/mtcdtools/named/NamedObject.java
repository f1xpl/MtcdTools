package com.microntek.f1x.mtcdtools.named;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by f1x on 2017-02-05.
 */

public abstract class NamedObject {
    protected NamedObject(NamedObjectId id, String objectType) {
        mId = id;
        mObjectType = objectType;
    }

    protected NamedObject(JSONObject json) throws JSONException {
        mId = new NamedObjectId(json.getString(NAME_PROPERTY));
        mObjectType = json.getString(OBJECT_TYPE_PROPERTY);
    }

    public NamedObjectId getId() {
        return mId;
    }

    public String getObjectType() {
        return mObjectType;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(NAME_PROPERTY, mId);
        json.put(OBJECT_TYPE_PROPERTY, mObjectType);

        return json;
    }

    public abstract void removeDependency(NamedObjectId dependencyId);
    public abstract void replaceDependency(NamedObjectId oldDependencyId, NamedObjectId newDependencyId);

    private NamedObjectId mId;
    private final String mObjectType;

    static public final String NAME_PROPERTY = "name";
    static public final String OBJECT_TYPE_PROPERTY = "objectType";
}
