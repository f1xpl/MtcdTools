package com.f1x.mtcdtools;

import android.view.KeyEvent;

import com.f1x.mtcdtools.keys.evaluation.KeyPressDispatcher;
import com.f1x.mtcdtools.keys.input.KeyInput;
import com.f1x.mtcdtools.keys.input.KeyInputType;

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

        KeyInput nextKeyInput = new KeyInput(2, KeyInputType.NEXT, null);
        mKeyInputs.put(2, nextKeyInput);

        KeyInput previousKeyInput = new KeyInput(3, KeyInputType.PREVIOUS, null);
        mKeyInputs.put(3, previousKeyInput);

        KeyInput togglePlayKeyInput = new KeyInput(4, KeyInputType.TOGGLE_PLAY, "com.permission");
        mKeyInputs.put(4, togglePlayKeyInput);

        mKeyPressEvaluator = new MockKeyPressEvaluator();
        mKeyPressDispatcher = new KeyPressDispatcher(mKeyPressEvaluator, mKeyInputs);
    }

    @Test
    public void evaluateLaunchKeyEventDown() {
        KeyInput keyInput = mKeyInputs.get(1);
        mKeyPressDispatcher.dispatch(keyInput.getKeyCode(), KeyEvent.ACTION_DOWN);

        assertTrue(mKeyPressEvaluator.wasLaunchInputEvaluated());
        assertEquals(keyInput.getParameter(), mKeyPressEvaluator.getLaunchPackageName());
        assertEquals(null, mKeyPressEvaluator.getActionType());
        assertEquals(null, mKeyPressEvaluator.getAndroidKeyCode());
    }

    @Test
    public void evaluateLaunchKeyEventUp() {
        KeyInput keyInput = mKeyInputs.get(1);
        mKeyPressDispatcher.dispatch(keyInput.getKeyCode(), KeyEvent.ACTION_UP);

        assertFalse(mKeyPressEvaluator.wasLaunchInputEvaluated());
        assertEquals(null, mKeyPressEvaluator.getActionType());
        assertEquals(null, mKeyPressEvaluator.getAndroidKeyCode());
    }

    @Test
    public void evaluateMediaKeyEventNextDown() {
        KeyInput keyInput = mKeyInputs.get(2);
        mKeyPressDispatcher.dispatch(keyInput.getKeyCode(), KeyEvent.ACTION_DOWN);

        assertFalse(mKeyPressEvaluator.wasLaunchInputEvaluated());
        assertEquals(KeyEvent.ACTION_DOWN, mKeyPressEvaluator.getActionType().intValue());
        assertEquals(KeyEvent.KEYCODE_MEDIA_NEXT, mKeyPressEvaluator.getAndroidKeyCode().intValue());
    }

    @Test
    public void evaluateMediaKeyEventNextUp() {
        KeyInput keyInput = mKeyInputs.get(2);
        mKeyPressDispatcher.dispatch(keyInput.getKeyCode(), KeyEvent.ACTION_UP);

        assertFalse(mKeyPressEvaluator.wasLaunchInputEvaluated());
        assertEquals(KeyEvent.ACTION_UP, mKeyPressEvaluator.getActionType().intValue());
        assertEquals(KeyEvent.KEYCODE_MEDIA_NEXT, mKeyPressEvaluator.getAndroidKeyCode().intValue());
    }

    @Test
    public void evaluateMediaKeyEventPreviousDown() {
        KeyInput keyInput = mKeyInputs.get(3);
        mKeyPressDispatcher.dispatch(keyInput.getKeyCode(), KeyEvent.ACTION_DOWN);

        assertFalse(mKeyPressEvaluator.wasLaunchInputEvaluated());
        assertEquals(KeyEvent.ACTION_DOWN, mKeyPressEvaluator.getActionType().intValue());
        assertEquals(KeyEvent.KEYCODE_MEDIA_PREVIOUS, mKeyPressEvaluator.getAndroidKeyCode().intValue());
    }

    @Test
    public void evaluateMediaKeyEventPreviousUp() {
        KeyInput keyInput = mKeyInputs.get(3);
        mKeyPressDispatcher.dispatch(keyInput.getKeyCode(), KeyEvent.ACTION_UP);

        assertFalse(mKeyPressEvaluator.wasLaunchInputEvaluated());
        assertEquals(KeyEvent.ACTION_UP, mKeyPressEvaluator.getActionType().intValue());
        assertEquals(KeyEvent.KEYCODE_MEDIA_PREVIOUS, mKeyPressEvaluator.getAndroidKeyCode().intValue());
    }

    @Test
    public void evaluateMediaKeyEventTogglePlayDown() {
        KeyInput keyInput = mKeyInputs.get(4);
        mKeyPressDispatcher.dispatch(keyInput.getKeyCode(), KeyEvent.ACTION_DOWN);

        assertFalse(mKeyPressEvaluator.wasLaunchInputEvaluated());
        assertEquals(KeyEvent.ACTION_DOWN, mKeyPressEvaluator.getActionType().intValue());
        assertEquals(KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE, mKeyPressEvaluator.getAndroidKeyCode().intValue());
        assertEquals(keyInput.getParameter(), mKeyPressEvaluator.getMediaKeyPermission());
    }

    @Test
    public void evaluateMediaKeyEventTogglePlayUp() {
        KeyInput keyInput = mKeyInputs.get(4);
        mKeyPressDispatcher.dispatch(keyInput.getKeyCode(), KeyEvent.ACTION_UP);

        assertFalse(mKeyPressEvaluator.wasLaunchInputEvaluated());
        assertEquals(KeyEvent.ACTION_UP, mKeyPressEvaluator.getActionType().intValue());
        assertEquals(KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE, mKeyPressEvaluator.getAndroidKeyCode().intValue());
        assertEquals(keyInput.getParameter(), mKeyPressEvaluator.getMediaKeyPermission());
    }

    private final Map<Integer, KeyInput> mKeyInputs;
    private final MockKeyPressEvaluator mKeyPressEvaluator;
    private final KeyPressDispatcher mKeyPressDispatcher;
}
