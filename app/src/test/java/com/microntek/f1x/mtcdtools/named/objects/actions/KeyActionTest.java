package com.microntek.f1x.mtcdtools.named.objects.actions;

import android.content.Context;
import android.media.AudioManager;
import android.view.KeyEvent;

import com.microntek.f1x.mtcdtools.named.NamedObjectId;

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
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by f1x on 2017-01-13.
 */

@PowerMockIgnore({ "org.mockito.*", "org.robolectric.*", "android.*" })
@PrepareForTest(KeyAction.class)
@RunWith(PowerMockRunner.class)

public class KeyActionTest {
    @Before
    public void init() throws JSONException {
        initMocks(this);
        Mockito.doReturn(mMockAudioManager).when(mMockContext).getSystemService(Context.AUDIO_SERVICE);

        mActionId = new NamedObjectId("TestKeyAction");

        mActionJson = new JSONObject();
        mActionJson.put(KeyAction.NAME_PROPERTY, mActionId.toString());
        mActionJson.put(KeyAction.OBJECT_TYPE_PROPERTY, KeyAction.OBJECT_TYPE);
        mActionJson.put(KeyAction.KEYCODE_PROPERTY, 123);
    }

    @Test
    public void test_evaluate() throws Exception {
        PowerMockito.whenNew(KeyEvent.class).withArguments(KeyEvent.ACTION_DOWN, mActionJson.getInt(KeyAction.KEYCODE_PROPERTY)).thenReturn(mMockKeyEventDown);
        PowerMockito.whenNew(KeyEvent.class).withArguments(KeyEvent.ACTION_UP, mActionJson.getInt(KeyAction.KEYCODE_PROPERTY)).thenReturn(mMockKeyEventUp);

        when(mMockAudioManager.getParameters("av_channel=")).thenReturn("sys");

        KeyAction keyAction = new KeyAction(mActionJson);
        keyAction.evaluate(mMockContext);

        InOrder keyEventsOrder = inOrder(mMockAudioManager);
        keyEventsOrder.verify(mMockAudioManager).dispatchMediaKeyEvent(mMockKeyEventDown);
        keyEventsOrder.verify(mMockAudioManager).dispatchMediaKeyEvent(mMockKeyEventUp);
    }

    @Test
    public void test_evaluate_system_is_not_focused() throws Exception {
        PowerMockito.whenNew(KeyEvent.class).withArguments(KeyEvent.ACTION_DOWN, mActionJson.getInt(KeyAction.KEYCODE_PROPERTY)).thenReturn(mMockKeyEventDown);
        PowerMockito.whenNew(KeyEvent.class).withArguments(KeyEvent.ACTION_UP, mActionJson.getInt(KeyAction.KEYCODE_PROPERTY)).thenReturn(mMockKeyEventUp);

        when(mMockAudioManager.getParameters("av_channel=")).thenReturn("fm");

        KeyAction keyAction = new KeyAction(mActionJson);
        keyAction.evaluate(mMockContext);

        InOrder keyEventsOrder = inOrder(mMockAudioManager);
        keyEventsOrder.verify(mMockAudioManager, times(0)).dispatchMediaKeyEvent(any(KeyEvent.class));
        keyEventsOrder.verify(mMockAudioManager, times(0)).dispatchMediaKeyEvent(any(KeyEvent.class));
    }

    @Test
    public void test_ConstructFromParameters() throws JSONException {
        KeyAction keyAction = new KeyAction(mActionId,
                                            mActionJson.getInt(KeyAction.KEYCODE_PROPERTY));

        assertEquals(keyAction.toJson().toString(), mActionJson.toString());
        assertEquals(mActionId, keyAction.getId());
        assertEquals(mActionJson.getString(KeyAction.OBJECT_TYPE_PROPERTY), keyAction.getObjectType());
        assertEquals(mActionJson.getInt(KeyAction.KEYCODE_PROPERTY), keyAction.getKeyCode());
    }

    @Test
    public void test_toJson() throws JSONException {
        KeyAction keyAction = new KeyAction(mActionJson);
        assertEquals(keyAction.toJson().toString(), mActionJson.toString());
        assertEquals(mActionId, keyAction.getId());
        assertEquals(mActionJson.getString(KeyAction.OBJECT_TYPE_PROPERTY), keyAction.getObjectType());
        assertEquals(mActionJson.getInt(KeyAction.KEYCODE_PROPERTY), keyAction.getKeyCode());
    }

    private NamedObjectId mActionId;

    @Mock
    KeyEvent mMockKeyEventUp;

    @Mock
    KeyEvent mMockKeyEventDown;

    @Mock
    Context mMockContext;

    @Mock
    AudioManager mMockAudioManager;

    private JSONObject mActionJson;
}
