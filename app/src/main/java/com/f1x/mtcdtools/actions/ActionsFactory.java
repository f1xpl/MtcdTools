package com.f1x.mtcdtools.actions;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by COMPUTER on 2017-01-15.
 */

public class ActionsFactory {
    public static Action createAction(JSONObject json) throws JSONException {
        String actionType = json.getString(Action.TYPE_PROPERTY);

        if(actionType.equals(KeyAction.ACTION_TYPE)) {
            return new KeyAction(json);
        } else if(actionType.equals(LaunchAction.ACTION_TYPE)) {
            return new LaunchAction(json);
        } else if(actionType.equals(StartActivityAction.ACTION_TYPE)) {
            return new StartActivityAction(json);
        } else if(actionType.equals(BroadcastIntentAction.ACTION_TYPE)) {
            return new BroadcastIntentAction(json);
        }

        return null;
    }
}
