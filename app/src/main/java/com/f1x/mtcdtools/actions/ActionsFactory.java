package com.f1x.mtcdtools.actions;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by COMPUTER on 2017-01-15.
 */

public class ActionsFactory {
    public static Action createAction(JSONObject json) throws JSONException {
        String actionType = json.getString(Action.TYPE_PROPERTY);

        switch(actionType) {
            case KeyAction.ACTION_TYPE:
                return new KeyAction(json);
            case LaunchAction.ACTION_TYPE:
                return new LaunchAction(json);
            case StartActivityAction.ACTION_TYPE:
                return new StartActivityAction(json);
            case BroadcastIntentAction.ACTION_TYPE:
                return new BroadcastIntentAction(json);
            default:
                return null;
        }
    }
}
