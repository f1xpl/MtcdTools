package android.microntek.f1x.mtcdtools.service.dispatching;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import android.microntek.f1x.mtcdtools.named.objects.containers.ActionsList;
import android.microntek.f1x.mtcdtools.named.objects.containers.ActionsSequence;
import android.microntek.f1x.mtcdtools.named.objects.containers.ModeList;
import android.microntek.f1x.mtcdtools.named.NamedObjectId;
import android.microntek.f1x.mtcdtools.named.objects.actions.KeyAction;
import android.microntek.f1x.mtcdtools.service.storage.NamedObjectsStorage;

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
 * Created by f1x on 2017-02-05.
 */

@PowerMockIgnore({ "org.mockito.*", "org.robolectric.*", "android.*" })
@PrepareForTest({NamedObjectDispatcher.class, NamedObjectsDispatchTask.class})
@RunWith(PowerMockRunner.class)
public class NamedObjectDispatcherTest {
    @Before
    public void init() throws Exception {
        initMocks(this);
        PowerMockito.whenNew(NamedObjectsDispatchTask.class).withArguments(mMockNamedObjectsStorage, mMockContext).thenReturn(mActionsDispatchTask);
    }

    @Test
    public void test_ActionDispatch() {
        KeyAction action = mock(KeyAction.class);
        NamedObjectId actionId = new NamedObjectId("testActionName");
        when(action.getId()).thenReturn(actionId);
        when(action.getObjectType()).thenReturn(KeyAction.OBJECT_TYPE);

        when(mMockNamedObjectsStorage.getItem(actionId)).thenReturn(action);

        NamedObjectDispatcher dispatcher = new NamedObjectDispatcher(mMockNamedObjectsStorage);
        dispatcher.dispatch(actionId, mMockContext);
        verify(action, times(1)).evaluate(mMockContext);
    }

    @Test
    public void test_ActionsListDispatch() throws Exception {
        PowerMockito.whenNew(Intent.class).withAnyArguments().thenReturn(mMockIntent);

        NamedObjectId actionsListId = new NamedObjectId("actionsListName");
        ActionsList actionsList = mock(ActionsList.class);
        when(actionsList.getId()).thenReturn(actionsListId);
        when(actionsList.getObjectType()).thenReturn(ActionsList.OBJECT_TYPE);

        when(mMockNamedObjectsStorage.getItem(actionsListId)).thenReturn(actionsList);

        NamedObjectDispatcher dispatcher = new NamedObjectDispatcher(mMockNamedObjectsStorage);
        dispatcher.dispatch(actionsListId, mMockContext);
        verify(mMockContext, times(1)).startActivity(mMockIntent);
    }

    @Test
    public void test_ActionsSequenceDispatch() throws Exception {
        NamedObjectId actionsSequenceId = new NamedObjectId("actionsSequenceName");
        ActionsSequence actionsSequence = mock(ActionsSequence.class);
        when(actionsSequence.getId()).thenReturn(actionsSequenceId);
        when(actionsSequence.getObjectType()).thenReturn(ActionsSequence.OBJECT_TYPE);

        when(mMockNamedObjectsStorage.getItem(actionsSequenceId)).thenReturn(actionsSequence);

        NamedObjectDispatcher dispatcher = new NamedObjectDispatcher(mMockNamedObjectsStorage);
        dispatcher.dispatch(actionsSequenceId, mMockContext);

        verify(mActionsDispatchTask, times(1)).execute(actionsSequenceId);
    }

    @Test
    public void test_Dispatch_When_PreviousIs_InProgress() throws Exception {
        NamedObjectId actionsSequenceId = new NamedObjectId("actionsSequenceName");
        ActionsSequence actionsSequence = mock(ActionsSequence.class);
        when(actionsSequence.getId()).thenReturn(actionsSequenceId);
        when(actionsSequence.getObjectType()).thenReturn(ActionsSequence.OBJECT_TYPE);
        when(mMockNamedObjectsStorage.getItem(actionsSequenceId)).thenReturn(actionsSequence);

        NamedObjectDispatcher dispatcher = new NamedObjectDispatcher(mMockNamedObjectsStorage);
        dispatcher.dispatch(actionsSequenceId, mMockContext);

        when(mActionsDispatchTask.getStatus()).thenReturn(AsyncTask.Status.RUNNING);
        dispatcher.dispatch(actionsSequenceId, mMockContext);

        verify(mActionsDispatchTask, times(1)).cancel(true);
        verify(mActionsDispatchTask, times(2)).execute(actionsSequenceId);
    }

    @Test
    public void test_ModeListDispatch() throws Exception {
        NamedObjectId modeListId = new NamedObjectId("modeListName");
        ModeList modeList = mock(ModeList.class);
        when(modeList.getId()).thenReturn(modeListId);
        when(modeList.getObjectType()).thenReturn(ModeList.OBJECT_TYPE);

        KeyAction action = mock(KeyAction.class);
        NamedObjectId actionId = new NamedObjectId("actionName");
        when(action.getId()).thenReturn(actionId);
        when(action.getObjectType()).thenReturn(KeyAction.OBJECT_TYPE);

        when(modeList.evaluate()).thenReturn(actionId);
        when(mMockNamedObjectsStorage.getItem(actionId)).thenReturn(action);
        when(mMockNamedObjectsStorage.getItem(modeListId)).thenReturn(modeList);

        NamedObjectDispatcher dispatcher = new NamedObjectDispatcher(mMockNamedObjectsStorage);
        dispatcher.dispatch(modeListId, mMockContext);
        verify(action, times(1)).evaluate(mMockContext);
    }

    @Test
    public void test_EmptyModeListDispatch() throws Exception {
        NamedObjectId modeListId = new NamedObjectId("modeListName");
        ModeList modeList = mock(ModeList.class);

        when(modeList.getId()).thenReturn(modeListId);
        when(modeList.getObjectType()).thenReturn(ModeList.OBJECT_TYPE);
        when(modeList.evaluate()).thenReturn(null);
        when(mMockNamedObjectsStorage.getItem(modeListId)).thenReturn(modeList);

        NamedObjectDispatcher dispatcher = new NamedObjectDispatcher(mMockNamedObjectsStorage);
        dispatcher.dispatch(modeListId, mMockContext);
    }

    @Test
    public void test_Dispatch_NullObject() {
        NamedObjectDispatcher dispatcher = new NamedObjectDispatcher(mMockNamedObjectsStorage);

        NamedObjectId namedObjectId = new NamedObjectId("namedObjectName");
        when(mMockNamedObjectsStorage.getItem(namedObjectId)).thenReturn(null);
        dispatcher.dispatch(namedObjectId, mMockContext);
    }

    @Mock
    NamedObjectsDispatchTask mActionsDispatchTask;

    @Mock
    NamedObjectsStorage mMockNamedObjectsStorage;

    @Mock
    Intent mMockIntent;

    @Mock
    Context mMockContext;

    @Mock
    Intent mMockStartActivityIntent;
}
