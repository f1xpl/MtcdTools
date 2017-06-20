package android.microntek.f1x.mtcdtools.service.input;

import android.microntek.CarManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.microntek.f1x.mtcdtools.service.configuration.Configuration;

/**
 * Created by COMPUTER on 2017-06-19.
 */

public class PX5PressedKeysSequenceManager extends PressedKeysSequenceManager {
    public PX5PressedKeysSequenceManager(Configuration configuration) {
        super(configuration);
        mCarManager = new CarManager();
    }

    @Override
    public void init() {
        mCarManager.attach(mPressedKeysHandler, KEYDOWN_ACTION_NAME);
    }

    @Override
    public void destroy() {
        mCarManager.detach();
        super.destroy();
    }

    private final Handler mPressedKeysHandler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            if (KEYDOWN_ACTION_NAME.equals(message.obj)) {
                Bundle data = message.getData();

                if (KEY_PARAM_TYPE.equals(data.getString(KEY_PARAM_TYPE_NAME))) {
                    final int keyCode = data.getInt(KEY_PARAM_VALUE_NAME);

                    if (keyCode != DEFAULT_KEY_CODE) {
                        insertKeyCode(keyCode);
                    }
                }
            }
        }
    };

    private final CarManager mCarManager;

    private static final int DEFAULT_KEY_CODE = -1;
    private static final String KEYDOWN_ACTION_NAME = "KeyDown";
    private static final String KEY_PARAM_TYPE = "key";
    private static final String KEY_PARAM_TYPE_NAME = "type";
    private static final String KEY_PARAM_VALUE_NAME = "value";
}
