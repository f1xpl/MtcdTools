package com.f1x.mtcdtools.actions;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by COMPUTER on 2017-01-09.
 */

public class StartActivityAction extends CustomIntentAction {
    public StartActivityAction(JSONObject json, Context context) throws JSONException {
        super(json);
        mContext = context;
    }

    @Override
    public void evaluate() {
        mContext.startActivity(getIntent());
    }

    private final Context mContext;

    static public final String ACTION_TYPE = "StartActivityAction";
}
