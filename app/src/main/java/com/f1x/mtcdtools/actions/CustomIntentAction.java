package com.f1x.mtcdtools.actions;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by COMPUTER on 2017-01-09.
 */

public abstract class CustomIntentAction extends Action {
    public CustomIntentAction(JSONObject json) throws JSONException {
        super(json);

        mIntentPackage = json.getString(INTENT_PACKAGE_PROPERTY);
        mIntentAction = json.getString(INTENT_ACTION_PROPERTY);
        mIntentCategory = json.getString(INTENT_CATEGORY_PROPERTY);
        mIntentData = json.getString(INTENT_DATA_PROPERTY);
        mIntentType = json.getString(INTENT_TYPE_PROPERTY);
        mIntentExtras = ExtrasParser.fromJSON(json.getJSONObject(INTENT_EXTRAS_PROPERTY));
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

    protected Intent getIntent() {
        Intent intent = new Intent();
        intent.setPackage(mIntentPackage);
        intent.setAction(mIntentAction);
        intent.addCategory(mIntentCategory);
        intent.setDataAndType(UriParser.fromString(mIntentData), mIntentType);
        intent.putExtras(mIntentExtras);

        return intent;
    }

    protected final String mIntentPackage;
    protected final String mIntentAction;
    protected final String mIntentCategory;
    protected final String mIntentData;
    protected final String mIntentType;
    protected final Bundle mIntentExtras;

    static public final String INTENT_PACKAGE_PROPERTY = "intentPackage";
    static public final String INTENT_ACTION_PROPERTY = "intentAction";
    static public final String INTENT_CATEGORY_PROPERTY = "intentCategory";
    static public final String INTENT_DATA_PROPERTY = "intentData";
    static public final String INTENT_TYPE_PROPERTY = "intentType";
    static public final String INTENT_EXTRAS_PROPERTY = "intentExtras";

}
