package com.f1x.mtcdtools.actions;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by COMPUTER on 2017-01-09.
 */

public class StartActivityAction extends CustomIntentAction {
    public StartActivityAction(JSONObject json) throws JSONException {
        super(json);
        mClassName = json.getString(CLASS_NAME_PROPERTY);
    }

    public StartActivityAction(String actionName, String intentPackage, String intentAction,
                                 String intentCategory, String intentData, String intentType,
                                 JSONObject intentExtras, String className) throws JSONException {
        super(actionName, ACTION_TYPE, intentPackage, intentAction, intentCategory, intentData, intentType, intentExtras);
        mClassName = className;
    }

    @Override
    public void evaluate(Context context) {
        Intent intent = getIntent();
        if(!mIntentPackage.isEmpty() && !mClassName.isEmpty()) {
            intent.setClassName(mIntentPackage, mClassName);
        }

        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public JSONObject toJson() throws JSONException {
        JSONObject json = super.toJson();
        json.put(CLASS_NAME_PROPERTY, mClassName);

        return json;
    }

    public String getClassName() {
        return mClassName;
    }

    private final String mClassName;

    static public final String ACTION_TYPE = "StartActivityAction";
    static public final String CLASS_NAME_PROPERTY = "className";
}
