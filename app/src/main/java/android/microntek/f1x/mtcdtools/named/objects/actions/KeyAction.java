package android.microntek.f1x.mtcdtools.named.objects.actions;

import android.content.Context;
import android.media.AudioManager;
import android.microntek.CarManager;
import android.view.KeyEvent;
import android.widget.Toast;

import android.microntek.f1x.mtcdtools.utils.PlatformChecker;
import android.microntek.f1x.mtcdtools.named.NamedObjectId;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by f1x on 2017-01-09.
 */

public class KeyAction extends Action {
    public KeyAction(JSONObject json) throws JSONException {
        super(json);
        mKeyCode = json.getInt(KEYCODE_PROPERTY);
        mCarManager = PlatformChecker.isPX5Platform() ? new CarManager() : null;
    }

    public KeyAction(NamedObjectId id, int keyCode) {
        super(id, OBJECT_TYPE);
        mKeyCode = keyCode;
        mCarManager = PlatformChecker.isPX5Platform() ? new CarManager() : null;
    }

    @Override
    public void evaluate(Context context) {
        try {
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

            if(canEvaluate(audioManager)) { // workaround for MTC stuff
                KeyEvent keyEventDown = new KeyEvent(KeyEvent.ACTION_DOWN, mKeyCode);
                audioManager.dispatchMediaKeyEvent(keyEventDown);

                KeyEvent keyEventUp = new KeyEvent(KeyEvent.ACTION_UP, mKeyCode);
                audioManager.dispatchMediaKeyEvent(keyEventUp);
            }
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

    private boolean canEvaluate(AudioManager audioManager) {
        if(mCarManager != null) {
            return mCarManager.getStringState("av_channel").equals("sys");
        } else {
            String parameterValue = audioManager.getParameters("av_channel=");
            return parameterValue.equals("sys");
        }
    }

    private final int mKeyCode;
    private final CarManager mCarManager;

    static public final String OBJECT_TYPE = "KeyAction";
    static public final String KEYCODE_PROPERTY = "keycode";
}
