package com.f1x.mtcdtools.named.objects.actions;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.f1x.mtcdtools.named.objects.NamedObjectId;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by f1x on 2017-01-09.
 */

public class StartActivityAction extends CustomIntentAction {
    public StartActivityAction(JSONObject json) throws JSONException {
        super(json);
        mClassName = json.getString(CLASS_NAME_PROPERTY);
        mFlags = json.getInt(FLAGS_PROPERTY);
    }

    public StartActivityAction(NamedObjectId id, String intentPackage, String intentAction,
                               String intentCategory, String intentData, String intentType,
                               JSONObject intentExtras, String className, int flags) throws JSONException {
        super(id, OBJECT_TYPE, intentPackage, intentAction, intentCategory, intentData, intentType, intentExtras);
        mClassName = className;
        mFlags = flags;
    }

    @Override
    public void evaluate(Context context) {
        Intent intent = getIntent();
        intent.setFlags(mFlags);

        if(!mIntentPackage.isEmpty() && !mClassName.isEmpty()) {
            intent.setClassName(mIntentPackage, mClassName);
        }

        try {
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public JSONObject toJson() throws JSONException {
        JSONObject json = super.toJson();
        json.put(CLASS_NAME_PROPERTY, mClassName);
        json.put(FLAGS_PROPERTY, mFlags);

        return json;
    }

    public String getClassName() {
        return mClassName;
    }

    public int getFlags() { return mFlags; }

    private final String mClassName;
    private final int mFlags;

    static public final String OBJECT_TYPE = "StartActivityAction";
    static public final String CLASS_NAME_PROPERTY = "className";
    static public final String FLAGS_PROPERTY = "flags";
}
