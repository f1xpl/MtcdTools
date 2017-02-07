package com.f1x.mtcdtools.named.objects.actions;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by f1x on 2017-01-09.
 */

public class LaunchAction extends Action {
    public LaunchAction(JSONObject json) throws JSONException {
        super(json);
        mPackageName = json.getString(PACKAGE_NAME_PROPERTY);
    }

    public LaunchAction(String actionName, String packageName) {
        super(actionName, OBJECT_TYPE);
        mPackageName = packageName;
    }

    @Override
    public void evaluate(Context context) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(mPackageName);

        if(intent != null) {
            try {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } catch(Exception e) {
                e.printStackTrace();
                Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public JSONObject toJson() throws JSONException {
        JSONObject json = super.toJson();
        json.put(PACKAGE_NAME_PROPERTY, mPackageName);

        return json;
    }

    public String getPackageName() {
        return mPackageName;
    }

    private final String mPackageName;

    static public final String OBJECT_TYPE = "LaunchAction";
    static public final String PACKAGE_NAME_PROPERTY = "packageName";
}
