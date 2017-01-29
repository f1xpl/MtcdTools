package com.f1x.mtcdtools.actions;

import android.content.Context;
import android.media.AudioManager;
import android.view.KeyEvent;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.inOrder;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by COMPUTER on 2017-01-13.
 */

@PowerMockIgnore({ "org.mockito.*", "org.robolectric.*", "android.*" })
@PrepareForTest(KeyAction.class)
@RunWith(PowerMockRunner.class)

public class KeyActionTest {
    @Before
    public void init() throws JSONException {
        initMocks(this);
        Mockito.doReturn(mMockAudioManager).when(mMockContext).getSystemService(Context.AUDIO_SERVICE);

        mActionJson = new JSONObject();
        mActionJson.put(KeyAction.NAME_PROPERTY, "TestKeyAction");
        mActionJson.put(KeyAction.TYPE_PROPERTY, KeyAction.ACTION_TYPE);
        mActionJson.put(KeyAction.KEYCODE_PROPERTY, 123);
    }

    @Test
    public void test_evaluate() throws Exception {
        PowerMockito.whenNew(KeyEvent.class).withArguments(KeyEvent.ACTION_DOWN, mActionJson.getInt(KeyAction.KEYCODE_PROPERTY)).thenReturn(mMockKeyEventDown);
        PowerMockito.whenNew(KeyEvent.class).withArguments(KeyEvent.ACTION_UP, mActionJson.getInt(KeyAction.KEYCODE_PROPERTY)).thenReturn(mMockKeyEventUp);

        KeyAction keyAction = new KeyAction(mActionJson);
        keyAction.evaluate(mMockContext);

        InOrder keyEventsOrder = inOrder(mMockAudioManager);
        keyEventsOrder.verify(mMockAudioManager).dispatchMediaKeyEvent(mMockKeyEventDown);
        keyEventsOrder.verify(mMockAudioManager).dispatchMediaKeyEvent(mMockKeyEventUp);
    }

    @Test
    public void test_ConstructFromParameters() throws JSONException {
        KeyAction keyAction = new KeyAction(mActionJson.getString(KeyAction.NAME_PROPERTY),
                                            mActionJson.getInt(KeyAction.KEYCODE_PROPERTY));

        assertEquals(keyAction.toJson().toString(), mActionJson.toString());
        assertEquals(mActionJson.getString(KeyAction.NAME_PROPERTY), keyAction.getName());
        assertEquals(mActionJson.getString(KeyAction.TYPE_PROPERTY), keyAction.getType());
        assertEquals(mActionJson.getInt(KeyAction.KEYCODE_PROPERTY), keyAction.getKeyCode());
    }

    @Test
    public void test_toJson() throws JSONException {
        KeyAction keyAction = new KeyAction(mActionJson);
        assertEquals(keyAction.toJson().toString(), mActionJson.toString());
        assertEquals(mActionJson.getString(KeyAction.NAME_PROPERTY), keyAction.getName());
        assertEquals(mActionJson.getString(KeyAction.TYPE_PROPERTY), keyAction.getType());
        assertEquals(mActionJson.getInt(KeyAction.KEYCODE_PROPERTY), keyAction.getKeyCode());
    }

    @Mock
    KeyEvent mMockKeyEventUp;

    @Mock
    KeyEvent mMockKeyEventDown;

    @Mock
    Context mMockContext;

    @Mock
    AudioManager mMockAudioManager;

    JSONObject mActionJson;
}
