package com.f1x.mtcdtools.keys.input;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by COMPUTER on 2016-08-01.
 */
public class KeyInput {
    public static final String KEY_CODE_NAME = "keyCode";
    public static final String TYPE_NAME = "type";
    public static final String PARAMETER_NAME = "parameter";

    public KeyInput(JSONObject json) throws JSONException {
        mKeyCode = json.getInt(KEY_CODE_NAME);
        mType = KeyInputType.fromString(json.getString(TYPE_NAME));
        mParameter = json.getString(PARAMETER_NAME);
    }

    public KeyInput(int keyCode, KeyInputType type, String parameter) {
        mKeyCode = keyCode;
        mType = type;
        mParameter = parameter;
    }

    public int getKeyCode() {
        return mKeyCode;
    }

    public KeyInputType getType() {
        return mType;
    }

    public String getParameter() {
        return mParameter;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(KEY_CODE_NAME, mKeyCode);
        json.put(TYPE_NAME, KeyInputType.toString(mType));
        json.put(PARAMETER_NAME, mParameter);

        return json;
    }

    public boolean equals(Object object) {
        if (object == null) {
            return false;
        } else if (!KeyInput.class.isAssignableFrom(object.getClass())) {
            return false;
        } else {
            final KeyInput other = (KeyInput) object;
            return mKeyCode == other.mKeyCode && mType == other.mType && mParameter.equals(other.mParameter);
        }
    }

    private final int mKeyCode;
    private final KeyInputType mType;
    private final String mParameter;
}
