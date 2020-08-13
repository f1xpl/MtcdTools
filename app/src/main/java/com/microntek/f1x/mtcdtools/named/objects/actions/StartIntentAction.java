package com.microntek.f1x.mtcdtools.named.objects.actions;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.microntek.f1x.mtcdtools.named.NamedObjectId;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by f1x on 2017-01-09.
 */

public class StartIntentAction extends CustomIntentAction {
    public StartIntentAction(JSONObject json) throws JSONException {
        super(json);
        mClassName = json.getString(CLASS_NAME_PROPERTY);
        mFlags = json.getInt(FLAGS_PROPERTY);
        mTarget = json.getInt(TARGET_PROPERTY);
    }

    public StartIntentAction(NamedObjectId id, String intentPackage, String intentAction,
                             String intentCategory, String intentData, String intentType,
                             JSONObject intentExtras, String className, int flags, int target) throws JSONException {
        super(id, OBJECT_TYPE, intentPackage, intentAction, intentCategory, intentData, intentType, intentExtras);
        mClassName = className;
        mFlags = flags;
        mTarget = target;
    }

    @Override
    public void evaluate(Context context) {
        Intent intent = getIntent();
        intent.setFlags(mFlags);

        if(!mIntentPackage.isEmpty() && !mClassName.isEmpty()) {
            intent.setClassName(mIntentPackage, mClassName);
        }

        try {
            if(mTarget == TARGET_ACTIVITY) {
                context.startActivity(intent);
            } else {
                context.startService(intent);
            }
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
        json.put(TARGET_PROPERTY, mTarget);



        return json;
    }

    public String getClassName() {
        return mClassName;
    }

    public int getFlags() { return mFlags; }

    public int getTarget() {
        return mTarget;
    }

    private final String mClassName;
    private final int mFlags;
    private final int mTarget;

    static public final String OBJECT_TYPE = "StartIntentAction";
    static public final String CLASS_NAME_PROPERTY = "className";
    static public final String FLAGS_PROPERTY = "flags";
    static public final String TARGET_PROPERTY = "Target";

    static public final int TARGET_ACTIVITY = 1;
    static public final int TARGET_SERVICE = 2;
}
