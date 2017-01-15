package com.f1x.mtcdtools.actions;

import android.content.Context;
import android.media.AudioManager;
import android.view.KeyEvent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by COMPUTER on 2017-01-09.
 */

public class KeyAction extends Action {
    public KeyAction(JSONObject json, Context context) throws JSONException {
        super(json);
        mAudioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        mKeyCode = json.getInt(KEYCODE_PROPERTY);
    }

    @Override
    public void evaluate() {
        KeyEvent keyEventDown = new KeyEvent(KeyEvent.ACTION_DOWN, mKeyCode);
        mAudioManager.dispatchMediaKeyEvent(keyEventDown);

        KeyEvent keyEventUp = new KeyEvent(KeyEvent.ACTION_UP, mKeyCode);
        mAudioManager.dispatchMediaKeyEvent(keyEventUp);
    }

    @Override
    public JSONObject toJson() throws JSONException {
        JSONObject json = super.toJson();
        json.put(KEYCODE_PROPERTY, mKeyCode);

        return json;
    }

    private final AudioManager mAudioManager;
    private final int mKeyCode;

    static public final String ACTION_TYPE = "KeyAction";
    static public final String KEYCODE_PROPERTY = "keycode";
}
