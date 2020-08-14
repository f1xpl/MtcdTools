package com.microntek.f1x.mtcdtools.named.objects.actions;

import android.content.Context;

import com.microntek.f1x.mtcdtools.named.NamedObject;
import com.microntek.f1x.mtcdtools.named.NamedObjectId;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by f1x on 2017-01-09.
 */

public abstract class Action extends NamedObject {
    Action(NamedObjectId id, String type) {
        super(id, type);
    }

    Action(JSONObject json) throws JSONException {
        super(json);
    }

    public abstract void evaluate(Context context);

    @Override
    public void removeDependency(NamedObjectId id) {}

    @Override
    public void replaceDependency(NamedObjectId oldId, NamedObjectId newId) {}

    public static boolean isAction(String objectType) {
        return objectType.equals(KeyAction.OBJECT_TYPE) ||
                objectType.equals(LaunchAction.OBJECT_TYPE) ||
                objectType.equals(BroadcastIntentAction.OBJECT_TYPE) ||
                objectType.equals(StartIntentAction.OBJECT_TYPE);
    }
}
