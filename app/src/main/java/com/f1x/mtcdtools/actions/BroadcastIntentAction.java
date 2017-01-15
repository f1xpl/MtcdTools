package com.f1x.mtcdtools.actions;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by COMPUTER on 2017-01-09.
 */

public class BroadcastIntentAction extends CustomIntentAction {
    public BroadcastIntentAction(JSONObject json, Context context) throws JSONException {
        super(json);
        mContext = context;
        mPermissions = json.getString(PERMISSIONS_PROPERTY);
    }

    @Override
    public void evaluate() {
        mContext.sendOrderedBroadcast(getIntent(), mPermissions);
    }

    @Override
    public JSONObject toJson() throws JSONException {
        JSONObject json = super.toJson();
        json.put(PERMISSIONS_PROPERTY, mPermissions);

        return json;
    }

    private final Context mContext;
    private final String mPermissions;

    static public final String ACTION_TYPE = "BroadcastIntentAction";
    static public final String PERMISSIONS_PROPERTY = "permissions";
}
