package com.f1x.mtcdtools.named.objects;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.f1x.mtcdtools.named.objects.NamedObjectDispatcher;
import com.f1x.mtcdtools.named.objects.actions.Action;
import com.f1x.mtcdtools.named.objects.actions.KeyAction;
import com.f1x.mtcdtools.storage.NamedObjectsStorage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

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
        KeyAction action = mock(KeyAction.class);
        String actionName = "testActionName";
        when(action.getName()).thenReturn(actionName);
        when(action.getObjectType()).thenReturn(KeyAction.OBJECT_TYPE);

        when(mMockNamedObjectsStorage.getItem(actionName)).thenReturn(action);

        NamedObjectDispatcher dispatcher = new NamedObjectDispatcher(mMockNamedObjectsStorage);
        dispatcher.dispatch(actionName, mMockContext);
        verify(action, times(1)).evaluate(mMockContext);
    }

    @Test
    public void test_ActionsListDispatch() throws Exception {
        PowerMockito.whenNew(Intent.class).withAnyArguments().thenReturn(mMockIntent);

        String actionsListName = "actionsListName";
        ActionsList actionsList = mock(ActionsList.class);
        when(actionsList.getName()).thenReturn(actionsListName);
        when(actionsList.getObjectType()).thenReturn(ActionsList.OBJECT_TYPE);

        when(mMockNamedObjectsStorage.getItem(actionsListName)).thenReturn(actionsList);

        NamedObjectDispatcher dispatcher = new NamedObjectDispatcher(mMockNamedObjectsStorage);
        dispatcher.dispatch(actionsListName, mMockContext);
        verify(mMockContext, times(1)).startActivity(mMockIntent);
    }

    @Test
    public void test_ActionsSequenceDispatch() throws Exception {
        String actionsSequenceName = "actionsSequenceName";
        ActionsSequence actionsSequence = mock(ActionsSequence.class);
        when(actionsSequence.getName()).thenReturn(actionsSequenceName);
        when(actionsSequence.getObjectType()).thenReturn(ActionsSequence.OBJECT_TYPE);

        List<String> actionsNames = new ArrayList<>(Arrays.asList("action12", "action15", "action16"));
        when(actionsSequence.getActionNames()).thenReturn(actionsNames);

        Action action = mock(Action.class);

        when(mMockNamedObjectsStorage.getItem(actionsSequenceName)).thenReturn(actionsSequence);
        when(mMockNamedObjectsStorage.getItem(actionsNames.get(0))).thenReturn(action);
        when(mMockNamedObjectsStorage.getItem(actionsNames.get(1))).thenReturn(action);
        when(mMockNamedObjectsStorage.getItem(actionsNames.get(2))).thenReturn(action);

        ActionsSequenceDispatchTask actionsSequenceDispatchTask = mock(ActionsSequenceDispatchTask.class);
        PowerMockito.whenNew(ActionsSequenceDispatchTask.class).withArguments(actionsNames, mMockContext, mMockNamedObjectsStorage).thenReturn(actionsSequenceDispatchTask);

        NamedObjectDispatcher dispatcher = new NamedObjectDispatcher(mMockNamedObjectsStorage);
        dispatcher.dispatch(actionsSequenceName, mMockContext);

        verify(actionsSequenceDispatchTask, times(1)).execute(3000);
    }

    @Test
    public void test_Dispatch_NullObject() {
        NamedObjectDispatcher dispatcher = new NamedObjectDispatcher(mMockNamedObjectsStorage);

        String namedObjectName = "namedObjectName";
        when(mMockNamedObjectsStorage.getItem(namedObjectName)).thenReturn(null);
        dispatcher.dispatch(namedObjectName, mMockContext);
    }

    @Test
    public void test_Dispatch_WrongObjectType() {
        NamedObjectDispatcher dispatcher = new NamedObjectDispatcher(mMockNamedObjectsStorage);

        String actionsSequenceName = "actionsSequenceName";
        ActionsSequence actionsSequence = mock(ActionsSequence.class);
        when(actionsSequence.getName()).thenReturn(actionsSequenceName);
        when(actionsSequence.getObjectType()).thenReturn(ActionsList.OBJECT_TYPE);

        when(mMockNamedObjectsStorage.getItem(actionsSequenceName)).thenReturn(actionsSequence);
        dispatcher.dispatch(actionsSequenceName, mMockContext);
    }

    @Mock
    NamedObjectsStorage mMockNamedObjectsStorage;

    @Mock
    Intent mMockIntent;

    @Mock
    Context mMockContext;

    @Mock
    Intent mMockStartActivityIntent;
}
