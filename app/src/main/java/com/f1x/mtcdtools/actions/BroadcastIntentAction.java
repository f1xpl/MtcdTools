package com.f1x.mtcdtools.actions;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by COMPUTER on 2017-01-09.
 */

public class BroadcastIntentAction extends CustomIntentAction {
    public BroadcastIntentAction(JSONObject json) throws JSONException {
        super(json);
        mPermissions = json.getString(PERMISSIONS_PROPERTY);
    }

    public BroadcastIntentAction(String actionName, String intentPackage, String intentAction,
                                 String intentCategory, String intentData, String intentType,
                                 JSONObject intentExtras, String intentPermissions) throws JSONException {
        super(actionName, OBJECT_TYPE, intentPackage, intentAction, intentCategory, intentData, intentType, intentExtras);
        mPermissions = intentPermissions;
    }

    @Override
    public void evaluate(Context context) {
        if(mPermissions.isEmpty()) {
            context.sendBroadcast(getIntent());
        } else {
            context.sendOrderedBroadcast(getIntent(), mPermissions);
        }
    }

    @Override
    public JSONObject toJson() throws JSONException {
        JSONObject json = super.toJson();
        json.put(PERMISSIONS_PROPERTY, mPermissions);

        return json;
    }

    public String getPermissions() {
        return mPermissions;
    }

    private final String mPermissions;

    static public final String OBJECT_TYPE = "BroadcastIntentAction";
    static public final String PERMISSIONS_PROPERTY = "permissions";
}
