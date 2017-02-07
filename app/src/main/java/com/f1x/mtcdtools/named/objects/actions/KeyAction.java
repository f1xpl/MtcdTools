package com.f1x.mtcdtools.named.objects.actions;

import android.content.Context;
import android.media.AudioManager;
import android.view.KeyEvent;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by f1x on 2017-01-09.
 */

public class KeyAction extends Action {
    public KeyAction(JSONObject json) throws JSONException {
        super(json);
        mKeyCode = json.getInt(KEYCODE_PROPERTY);
    }

    public KeyAction(String actionName, int keyCode) {
        super(actionName, OBJECT_TYPE);
        mKeyCode = keyCode;
    }

    @Override
    public void evaluate(Context context) {
        try {
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

            KeyEvent keyEventDown = new KeyEvent(KeyEvent.ACTION_DOWN, mKeyCode);
            audioManager.dispatchMediaKeyEvent(keyEventDown);

            KeyEvent keyEventUp = new KeyEvent(KeyEvent.ACTION_UP, mKeyCode);
            audioManager.dispatchMediaKeyEvent(keyEventUp);
        } catch(Exception e) {
            e.printStackTrace();
            Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public JSONObject toJson() throws JSONException {
        JSONObject json = super.toJson();
        json.put(KEYCODE_PROPERTY, mKeyCode);

        return json;
    }

    public int getKeyCode() {
        return mKeyCode;
    }

    private final int mKeyCode;

    static public final String OBJECT_TYPE = "KeyAction";
    static public final String KEYCODE_PROPERTY = "keycode";
}
