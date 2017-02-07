package com.f1x.mtcdtools.named.objects.actions;

import android.content.Context;

import com.f1x.mtcdtools.named.objects.NamedObject;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by f1x on 2017-01-09.
 */

public abstract class Action extends NamedObject {
    Action(String name, String type) {
        super(name, type);
    }

    Action(JSONObject json) throws JSONException {
        super(json);
    }

    public abstract void evaluate(Context context);

    @Override
    public void removeDependency(String dependencyName) {

    }

    @Override
    public void replaceDependency(String oldDependencyName, String newDependencyName) {
    }

    public static boolean isAction(String objectType) {
        return objectType.equals(KeyAction.OBJECT_TYPE) ||
                objectType.equals(LaunchAction.OBJECT_TYPE) ||
                objectType.equals(BroadcastIntentAction.OBJECT_TYPE) ||
                objectType.equals(StartActivityAction.OBJECT_TYPE);
    }
}
