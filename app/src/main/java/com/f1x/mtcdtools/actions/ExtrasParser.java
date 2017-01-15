package com.f1x.mtcdtools.actions;

import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by COMPUTER on 2017-01-10.
 */

public class ExtrasParser {
    static public Bundle fromJSON(JSONObject json) throws JSONException {
        Bundle bundle = new Bundle();
        Iterator<?> keys = json.keys();

        while(keys.hasNext()) {
            String key = (String)keys.next();
            Object value = json.get(key);

            if(value instanceof Integer) {
                bundle.putInt(key, (int)value);
            } else if(value instanceof Long) {
                bundle.putLong(key, (long)value);
            } else if(value instanceof Double) {
                bundle.putDouble(key, (double)value);
            } else if(value instanceof Boolean) {
                bundle.putBoolean(key, (boolean)value);
            } else {
                bundle.putString(key, (String)value);
            }
        }

        return bundle;
    }

    static public JSONObject toJSON(Bundle bundle) throws JSONException {
        JSONObject json = new JSONObject();

        for (String key : bundle.keySet()) {
            json.put(key, bundle.get(key));
        }

        return json;
    }
}
