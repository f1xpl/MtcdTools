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

    @Override
    public void evaluate(Context context) {
        context.startActivity(getIntent());
    }

    static public final String ACTION_TYPE = "StartActivityAction";
}
