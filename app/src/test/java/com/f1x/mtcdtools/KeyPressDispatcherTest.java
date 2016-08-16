package com.f1x.mtcdtools;

import android.view.KeyEvent;

import com.f1x.mtcdtools.evaluation.KeyPressDispatcher;
import com.f1x.mtcdtools.input.KeyInput;
import com.f1x.mtcdtools.input.KeyInputType;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by COMPUTER on 2016-08-04.
 */
public class KeyPressDispatcherTest {
    public KeyPressDispatcherTest() {
        mKeyInputs = new HashMap<>();

        KeyInput launchKeyInput = new KeyInput(1, KeyInputType.LAUNCH, "com.test.package");
        mKeyInputs.put(1, launchKeyInput);

        KeyInput nextKeyInput = new KeyInput(2, KeyInputType.NEXT, "");
        mKeyInputs.put(2, nextKeyInput);

        KeyInput previousKeyInput = new KeyInput(3, KeyInputType.PREVIOUS, "");
        mKeyInputs.put(3, previousKeyInput);

        KeyInput togglePlayKeyInput = new KeyInput(4, KeyInputType.TOGGLE_PLAY, "");
        mKeyInputs.put(4, togglePlayKeyInput);

        KeyInput modeKeyInput = new KeyInput(5, KeyInputType.MODE, "");
        mKeyInputs.put(5, modeKeyInput);

        mKeyPressEvaluator = new MockKeyPressEvaluator();
        mKeyPressDispatcher = new KeyPressDispatcher(mKeyPressEvaluator);
        mKeyPressDispatcher.updateKeyInputs(mKeyInputs);
    }

    @Test
    public void evaluateLaunchKeyEvent() {
        KeyInput keyInput = mKeyInputs.get(1);
        mKeyPressDispatcher.dispatch(keyInput.getKeyCode());

        assertTrue(mKeyPressEvaluator.wasLaunchInputEvaluated());
        assertEquals(keyInput.getLaunchPackage(), mKeyPressEvaluator.getLaunchPackageName());
        assertFalse(mKeyPressEvaluator.wasModeInputEvaluated());
        assertEquals(null, mKeyPressEvaluator.getAndroidKeyCode());
    }

    @Test
    public void evaluateMediaKeyEventNext() {
        KeyInput keyInput = mKeyInputs.get(2);
        mKeyPressDispatcher.dispatch(keyInput.getKeyCode());

        assertFalse(mKeyPressEvaluator.wasLaunchInputEvaluated());
        assertFalse(mKeyPressEvaluator.wasModeInputEvaluated());
        assertEquals(KeyEvent.KEYCODE_MEDIA_NEXT, mKeyPressEvaluator.getAndroidKeyCode().intValue());
    }

    @Test
    public void evaluateMediaKeyEventPrevious() {
        KeyInput keyInput = mKeyInputs.get(3);
        mKeyPressDispatcher.dispatch(keyInput.getKeyCode());

        assertFalse(mKeyPressEvaluator.wasLaunchInputEvaluated());
        assertFalse(mKeyPressEvaluator.wasModeInputEvaluated());
        assertEquals(KeyEvent.KEYCODE_MEDIA_PREVIOUS, mKeyPressEvaluator.getAndroidKeyCode().intValue());
    }

    @Test
    public void evaluateMediaKeyEventTogglePlay() {
        KeyInput keyInput = mKeyInputs.get(4);
        mKeyPressDispatcher.dispatch(keyInput.getKeyCode());

        assertFalse(mKeyPressEvaluator.wasLaunchInputEvaluated());
        assertFalse(mKeyPressEvaluator.wasModeInputEvaluated());
        assertEquals(KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE, mKeyPressEvaluator.getAndroidKeyCode().intValue());
    }

    @Test
    public void evaluateModeKeyEvent() {
        KeyInput keyInput = mKeyInputs.get(5);
        mKeyPressDispatcher.dispatch(keyInput.getKeyCode());

        assertFalse(mKeyPressEvaluator.wasLaunchInputEvaluated());
        assertTrue(mKeyPressEvaluator.wasModeInputEvaluated());
        assertEquals(null, mKeyPressEvaluator.getAndroidKeyCode());
    }

    private final Map<Integer, KeyInput> mKeyInputs;
    private final MockKeyPressEvaluator mKeyPressEvaluator;
    private final KeyPressDispatcher mKeyPressDispatcher;
}
