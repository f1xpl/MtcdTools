package com.f1x.mtcdtools.storage;

import com.f1x.mtcdtools.ActionsList;
import com.f1x.mtcdtools.actions.Action;
import com.f1x.mtcdtools.actions.BroadcastIntentAction;
import com.f1x.mtcdtools.actions.KeyAction;
import com.f1x.mtcdtools.actions.LaunchAction;
import com.f1x.mtcdtools.actions.StartActivityAction;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by COMPUTER on 2017-02-05.
 */

public class NamedObjectsFactory {
    public static NamedObject createNamedObject(JSONObject json) throws JSONException {
        String actionType = json.getString(Action.OBJECT_TYPE_PROPERTY);

        switch(actionType) {
            case KeyAction.OBJECT_TYPE:
                return new KeyAction(json);
            case LaunchAction.OBJECT_TYPE:
                return new LaunchAction(json);
            case StartActivityAction.OBJECT_TYPE:
                return new StartActivityAction(json);
            case BroadcastIntentAction.OBJECT_TYPE:
                return new BroadcastIntentAction(json);
            case ActionsList.OBJECT_TYPE:
                return new ActionsList(json);
            default:
                return null;
        }
    }
}
