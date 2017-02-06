package com.f1x.mtcdtools.named.objects.actions;

import android.content.Intent;
import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by f1x on 2017-01-09.
 */

public abstract class CustomIntentAction extends Action {
    CustomIntentAction(JSONObject json) throws JSONException {
        super(json);

        mIntentPackage = json.getString(INTENT_PACKAGE_PROPERTY);
        mIntentAction = json.getString(INTENT_ACTION_PROPERTY);
        mIntentCategory = json.getString(INTENT_CATEGORY_PROPERTY);
        mIntentData = json.getString(INTENT_DATA_PROPERTY);
        mIntentType = json.getString(INTENT_TYPE_PROPERTY);
        mIntentExtras = ExtrasParser.fromJSON(json.getJSONObject(INTENT_EXTRAS_PROPERTY));
    }

    CustomIntentAction(String actionName, String actionType, String intentPackage,
                       String intentAction, String intentCategory, String intentData,
                       String intentType, JSONObject intentExtras) throws JSONException {
        super(actionName, actionType);
        mIntentPackage = intentPackage;
        mIntentAction = intentAction;
        mIntentCategory = intentCategory;
        mIntentData = intentData;
        mIntentType = intentType;
        mIntentExtras = ExtrasParser.fromJSON(intentExtras);
    }

    @Override
    public JSONObject toJson() throws JSONException {
        JSONObject json = super.toJson();

        json.put(INTENT_PACKAGE_PROPERTY, mIntentPackage);
        json.put(INTENT_ACTION_PROPERTY, mIntentAction);
        json.put(INTENT_CATEGORY_PROPERTY, mIntentCategory);
        json.put(INTENT_DATA_PROPERTY, mIntentData);
        json.put(INTENT_TYPE_PROPERTY, mIntentType);
        json.put(INTENT_EXTRAS_PROPERTY, ExtrasParser.toJSON(mIntentExtras));

        return json;
    }

    Intent getIntent() {
        Intent intent = new Intent();

        if(!mIntentPackage.isEmpty()) {
            intent.setPackage(mIntentPackage);
        }

        if(!mIntentAction.isEmpty()) {
            intent.setAction(mIntentAction);
        }

        if(!mIntentCategory.isEmpty()) {
            intent.addCategory(mIntentCategory);
        }

        if(!mIntentData.isEmpty()) {
            intent.setData(UriParser.fromString(mIntentData));
        }

        if(!mIntentType.isEmpty()) {
            intent.setType(mIntentType);
        }

        if(!mIntentExtras.isEmpty()) {
            intent.putExtras(mIntentExtras);
        }

        return intent;
    }

    public String getIntentPackage() {
        return mIntentPackage;
    }

    public String getIntentAction() {
        return mIntentAction;
    }

    public String getIntentCategory() {
        return mIntentCategory;
    }

    public String getIntentData() {
        return mIntentData;
    }

    public String getIntentType() {
        return mIntentType;
    }

    public JSONObject getIntentExtras() throws JSONException {
        return ExtrasParser.toJSON(mIntentExtras);
    }

    final String mIntentPackage;
    private final String mIntentAction;
    private final String mIntentCategory;
    private final String mIntentData;
    private final String mIntentType;
    private final Bundle mIntentExtras;

    static public final String INTENT_PACKAGE_PROPERTY = "intentPackage";
    static public final String INTENT_ACTION_PROPERTY = "intentAction";
    static public final String INTENT_CATEGORY_PROPERTY = "intentCategory";
    static public final String INTENT_DATA_PROPERTY = "intentData";
    static public final String INTENT_TYPE_PROPERTY = "intentType";
    static public final String INTENT_EXTRAS_PROPERTY = "intentExtras";

}
