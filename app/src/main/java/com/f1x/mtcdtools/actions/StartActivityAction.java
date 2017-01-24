package com.f1x.mtcdtools.actions;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by COMPUTER on 2017-01-09.
 */

public class StartActivityAction extends CustomIntentAction {
    public StartActivityAction(JSONObject json) throws JSONException {
        super(json);
    }

    public StartActivityAction(String actionName, String intentPackage, String intentAction,
                                 String intentCategory, String intentData, String intentType,
                                 JSONObject intentExtras) throws JSONException {
        super(actionName, ACTION_TYPE, intentPackage, intentAction, intentCategory, intentData, intentType, intentExtras);
    }

    @Override
    public void evaluate(Context context) {
        context.startActivity(getIntent());
    }

    static public final String ACTION_TYPE = "StartActivityAction";
}
