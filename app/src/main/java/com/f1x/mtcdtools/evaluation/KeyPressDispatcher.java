package com.f1x.mtcdtools.evaluation;

import android.view.KeyEvent;
import com.f1x.mtcdtools.input.KeyInput;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by COMPUTER on 2016-08-03.
 */
public class KeyPressDispatcher {
    public KeyPressDispatcher(KeyPressEvaluatorInterface evaluator) {
        mEvaluator = evaluator;
        mKeyInputs = new HashMap<>();
    }

    public void dispatch(int keyCode) {
        if(mKeyInputs.containsKey(keyCode)) {
            KeyInput keyInput = mKeyInputs.get(keyCode);
            dispatch(keyInput);
        }
    }

    private void dispatch(KeyInput keyInput) {
        switch(keyInput.getType()) {
            case LAUNCH:
                mEvaluator.evaluateLaunchInput(keyInput.getLaunchPackage());
                break;
            case MODE:
                mEvaluator.evaluateModeInput();
                break;
            case TOGGLE_PLAY:
                mEvaluator.evaluateMediaInput(KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE);
                break;
            case NEXT:
                mEvaluator.evaluateMediaInput(KeyEvent.KEYCODE_MEDIA_NEXT);
                break;
            case PREVIOUS:
                mEvaluator.evaluateMediaInput(KeyEvent.KEYCODE_MEDIA_PREVIOUS);
            break;
        }
    }

    public void updateKeyInputs(Map<Integer, KeyInput> keyInputs) {
        mKeyInputs = keyInputs;
    }

    private final KeyPressEvaluatorInterface mEvaluator;
    private Map<Integer, KeyInput> mKeyInputs;
}
