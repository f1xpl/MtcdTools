package com.f1x.mtcdtools;

import android.content.Context;
import android.content.Intent;

import com.f1x.mtcdtools.actions.KeyAction;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by COMPUTER on 2017-02-05.
 */

@PowerMockIgnore({ "org.mockito.*", "org.robolectric.*", "android.*" })
@PrepareForTest({NamedObjectDispatcher.class})
@RunWith(PowerMockRunner.class)
public class NamedObjectDispatcherTest {
    @Before
    public void init() throws Exception {
        initMocks(this);
    }

    @Test
    public void test_ActionDispatch() {
        NamedObjectDispatcher dispatcher = new NamedObjectDispatcher();

        KeyAction action = mock(KeyAction.class);
        when(action.getObjectType()).thenReturn(KeyAction.OBJECT_TYPE);
        dispatcher.dispatch(action, mMockContext);

        verify(action, times(1)).evaluate(mMockContext);
    }

    @Test
    public void test_ActionsListDispatch() throws Exception {
        PowerMockito.whenNew(Intent.class).withAnyArguments().thenReturn(mMockIntent);
        NamedObjectDispatcher dispatcher = new NamedObjectDispatcher();

        ActionsList actionsList = mock(ActionsList.class);
        when(actionsList.getObjectType()).thenReturn(ActionsList.OBJECT_TYPE);

        dispatcher.dispatch(actionsList, mMockContext);
        verify(mMockContext, times(1)).startActivity(mMockIntent);
    }

    @Mock
    Intent mMockIntent;

    @Mock
    Context mMockContext;

    @Mock
    Intent mMockStartActivityIntent;
}
