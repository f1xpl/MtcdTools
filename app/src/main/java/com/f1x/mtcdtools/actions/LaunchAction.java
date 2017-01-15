package com.f1x.mtcdtools.actions;

import android.content.Context;
import android.content.Intent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by COMPUTER on 2017-01-09.
 */

public class LaunchAction extends Action {
    public LaunchAction(JSONObject json, Context context) throws JSONException {
        super(json);
        mContext = context;
        mPackageName = json.getString(PACKAGE_NAME_PROPERTY);
    }

    @Override
    public void evaluate() {
        Intent intent = mContext.getPackageManager().getLaunchIntentForPackage(mPackageName);

        if(intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }
    }

    @Override
    public JSONObject toJson() throws JSONException {
        JSONObject json = super.toJson();
        json.put(PACKAGE_NAME_PROPERTY, mPackageName);

        return json;
    }

    private final Context mContext;
    private final String mPackageName;

    static public final String ACTION_TYPE = "LaunchAction";
    static public final String PACKAGE_NAME_PROPERTY = "packageName";
}
