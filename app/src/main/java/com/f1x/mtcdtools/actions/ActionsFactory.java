package com.f1x.mtcdtools.actions;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by COMPUTER on 2017-01-15.
 */

public class ActionsFactory {
    public static Action createAction(JSONObject json, Context context) throws JSONException {
        String actionType = json.getString(Action.TYPE_PROPERTY);

        if(actionType.equals(KeyAction.ACTION_TYPE)) {
            return new KeyAction(json, context);
        } else if(actionType.equals(LaunchAction.ACTION_TYPE)) {
            return new LaunchAction(json, context);
        } else if(actionType.equals(StartActivityAction.ACTION_TYPE)) {
            return new StartActivityAction(json, context);
        } else if(actionType.equals(BroadcastIntentAction.ACTION_TYPE)) {
            return new BroadcastIntentAction(json, context);
        }

        return null;
    }
}
