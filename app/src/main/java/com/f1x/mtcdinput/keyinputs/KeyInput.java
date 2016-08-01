package com.f1x.mtcdinput.keyinputs;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by COMPUTER on 2016-08-01.
 */
public class KeyInput {
    public static final String KEY_CODE_NAME = "keyCode";
    public static final String TYPE_NAME = "type";
    public static final String COMMAND_NAME = "command";

    public KeyInput(JSONObject json) throws JSONException{
        mKeyCode = json.getInt(KEY_CODE_NAME);
        mType = KeyInputType.fromString(json.getString(TYPE_NAME));
        mCommand = json.getString(COMMAND_NAME);
    }

    public KeyInput(int keyCode, KeyInputType type, String command) {
        mKeyCode = keyCode;
        mType = type;
        mCommand = command;
    }

    public int getKeyCode() {
        return mKeyCode;
    }

    public KeyInputType getType() {
        return mType;
    }

    public String getCommand() {
        return mCommand;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(KEY_CODE_NAME, mKeyCode);
        json.put(TYPE_NAME, KeyInputType.toString(mType));
        json.put(COMMAND_NAME, mCommand);

        return json;
    }

    public boolean equals(Object object) {
        if (object == null) {
            return false;
        } else if (!KeyInput.class.isAssignableFrom(object.getClass())) {
            return false;
        } else {
            final KeyInput other = (KeyInput) object;
            return mKeyCode == other.mKeyCode && mType == other.mType && mCommand.equals(other.mCommand);
        }
    }

    private final int mKeyCode;
    private final KeyInputType mType;
    private final String mCommand;
}
