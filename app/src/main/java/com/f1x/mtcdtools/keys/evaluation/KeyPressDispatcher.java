package com.f1x.mtcdtools.keys.evaluation;

import android.view.KeyEvent;

import com.f1x.mtcdtools.keys.input.KeyInput;
import com.f1x.mtcdtools.keys.input.KeyInputType;

import java.util.Map;

/**
 * Created by COMPUTER on 2016-08-03.
 */
public class KeyPressDispatcher {
    public KeyPressDispatcher(KeyPressEvaluatorInterface evaluator, Map<Integer, KeyInput> keyInputs) {
        mEvaluator = evaluator;
        updateKeyInputs(keyInputs);
    }

    public void dispatch(int keyCode, int actionType) {
        if(mKeyInputs.containsKey(keyCode)) {
            KeyInput keyInput = mKeyInputs.get(keyCode);
            dispatch(keyInput, actionType);
        }
    }

    private void dispatch(KeyInput keyInput, int actionType) {
        if(keyInput.getType() == KeyInputType.LAUNCH && actionType == KeyEvent.ACTION_DOWN) {
            mEvaluator.evaluateLaunchInput(keyInput.getParameter());
        } else if(keyInput.getType() == KeyInputType.TOGGLE_PLAY) {
            mEvaluator.evaluateMediaInput(actionType, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE);
        } else if(keyInput.getType() == KeyInputType.NEXT) {
            mEvaluator.evaluateMediaInput(actionType, KeyEvent.KEYCODE_MEDIA_NEXT);
        } else if(keyInput.getType() == KeyInputType.PREVIOUS) {
            mEvaluator.evaluateMediaInput(actionType, KeyEvent.KEYCODE_MEDIA_PREVIOUS);
        }
    }

    public void updateKeyInputs(Map<Integer, KeyInput> keyInputs) {
        mKeyInputs = keyInputs;
    }

    private final KeyPressEvaluatorInterface mEvaluator;
    private Map<Integer, KeyInput> mKeyInputs;
}
