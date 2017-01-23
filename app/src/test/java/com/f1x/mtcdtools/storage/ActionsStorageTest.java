package com.f1x.mtcdtools.storage;

import android.content.Context;

import com.f1x.mtcdtools.actions.Action;
import com.f1x.mtcdtools.actions.ActionsFactory;
import com.f1x.mtcdtools.actions.KeyAction;
import com.f1x.mtcdtools.actions.LaunchAction;
import com.f1x.mtcdtools.storage.exceptions.DuplicatedEntryException;
import com.f1x.mtcdtools.storage.exceptions.EntryCreationFailed;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by COMPUTER on 2017-01-15.
 */

@PowerMockIgnore({ "org.mockito.*", "org.robolectric.*", "android.*" })
@PrepareForTest({ActionsStorage.class, ActionsFactory.class})
@RunWith(PowerMockRunner.class)

public class ActionsStorageTest {
    static class JSONMatcher extends ArgumentMatcher<JSONObject> {
        private final JSONObject mExpected;

        public JSONMatcher(JSONObject expected) {
            mExpected = expected;
        }

        @Override
        public boolean matches(Object actual) {
            if(actual == null) {
                return false;
            } else {
                JSONObject actualJSON = (JSONObject) actual;
                return mExpected.toString().equals(actualJSON.toString());
            }
        }
    }

    static JSONObject jsonEq(JSONObject expected) {
        return argThat(new JSONMatcher(expected));
    }

    @Before
    public void init() throws JSONException {
        initMocks(this);
        mSampleActions = new HashMap<>();
        mActionsArray = new JSONArray();
        mActionsJson = new JSONObject();

        mAction1Json = new JSONObject();
        mAction1Json.put(KeyAction.NAME_PROPERTY, "action1");
        mAction1Json.put(KeyAction.TYPE_PROPERTY, KeyAction.ACTION_TYPE);
        mAction1Json.put(KeyAction.KEYCODE_PROPERTY, 123);
        when(mMockAction1.getName()).thenReturn(mAction1Json.getString(Action.NAME_PROPERTY));
        when(mMockAction1.getType()).thenReturn(mAction1Json.getString(Action.TYPE_PROPERTY));
        when(mMockAction1.toJson()).thenReturn(mAction1Json);

        mAction2Json = new JSONObject();
        mAction2Json.put(LaunchAction.NAME_PROPERTY, "action2");
        mAction2Json.put(LaunchAction.TYPE_PROPERTY, LaunchAction.ACTION_TYPE);
        mAction2Json.put(LaunchAction.PACKAGE_NAME_PROPERTY, "com.test.package");
        when(mMockAction2.getName()).thenReturn(mAction2Json.getString(Action.NAME_PROPERTY));
        when(mMockAction2.getType()).thenReturn(mAction2Json.getString(Action.TYPE_PROPERTY));
        when(mMockAction2.toJson()).thenReturn(mAction2Json);
    }

    @Test
    public void test_Read() throws JSONException, IOException, EntryCreationFailed, DuplicatedEntryException {
        mActionsArray.put(mAction1Json);
        mActionsArray.put(mAction2Json);
        mActionsJson.put(ActionsStorage.ROOT_ARRAY_NAME, mActionsArray);

        when(mMockFileReader.read(ActionsStorage.STORAGE_FILE_NAME, "UTF-8")).thenReturn(mActionsJson.toString());
        PowerMockito.mockStatic(ActionsFactory.class);

        when(ActionsFactory.createAction(jsonEq(mAction1Json))).thenReturn(mMockAction1);
        when(ActionsFactory.createAction(jsonEq(mAction2Json))).thenReturn(mMockAction2);

        ActionsStorage actionsStorage = new ActionsStorage(mMockFileReader, mMockFileWriter, mMockContext);
        actionsStorage.read();
    }

    @Test
    public void test_Read_Duplicated_Name() throws JSONException, IOException, EntryCreationFailed {
        mActionsArray.put(mAction1Json);
        mActionsArray.put(mAction2Json);
        mActionsJson.put(ActionsStorage.ROOT_ARRAY_NAME, mActionsArray);

        when(mMockFileReader.read(ActionsStorage.STORAGE_FILE_NAME, "UTF-8")).thenReturn(mActionsJson.toString());
        PowerMockito.mockStatic(ActionsFactory.class);

        when(ActionsFactory.createAction(jsonEq(mAction1Json))).thenReturn(mMockAction1);
        when(ActionsFactory.createAction(jsonEq(mAction2Json))).thenReturn(mMockAction1);

        ActionsStorage actionsStorage = new ActionsStorage(mMockFileReader, mMockFileWriter, mMockContext);

        boolean exceptionCaught = false;
        try {
            actionsStorage.read();
        } catch(DuplicatedEntryException e) {
            exceptionCaught = true;
        }

        assertTrue(exceptionCaught);
    }

    @Test
    public void test_Insert() throws JSONException, IOException, DuplicatedEntryException {
        mActionsArray.put(mAction1Json);
        mActionsJson.put(ActionsStorage.ROOT_ARRAY_NAME, mActionsArray);

        ActionsStorage actionsStorage = new ActionsStorage(mMockFileReader, mMockFileWriter, mMockContext);
        actionsStorage.insert(mMockAction1);
        verify(mMockFileWriter, times(1)).write(mActionsJson.toString(), ActionsStorage.STORAGE_FILE_NAME, "UTF-8");
    }

    @Test
    public void test_Insert_Duplicated_Name() throws JSONException, IOException, DuplicatedEntryException {
        mActionsArray.put(mAction1Json);
        mActionsJson.put(ActionsStorage.ROOT_ARRAY_NAME, mActionsArray);

        ActionsStorage actionsStorage = new ActionsStorage(mMockFileReader, mMockFileWriter, mMockContext);
        actionsStorage.insert(mMockAction1);
        verify(mMockFileWriter, times(1)).write(mActionsJson.toString(), ActionsStorage.STORAGE_FILE_NAME, "UTF-8");

        boolean exceptionCaught = false;
        try {
            actionsStorage.insert(mMockAction1);
        } catch(DuplicatedEntryException e) {
            exceptionCaught = true;
        }

        assertTrue(exceptionCaught);
    }

    @Test
    public void test_Remove() throws JSONException, IOException, DuplicatedEntryException {
        mActionsArray.put(mAction2Json);
        mActionsJson.put(ActionsStorage.ROOT_ARRAY_NAME, mActionsArray);

        ActionsStorage actionsStorage = new ActionsStorage(mMockFileReader, mMockFileWriter, mMockContext);
        actionsStorage.insert(mMockAction1);
        actionsStorage.insert(mMockAction2);
        actionsStorage.remove(mMockAction1);
        verify(mMockFileWriter, times(1)).write(mActionsJson.toString(), ActionsStorage.STORAGE_FILE_NAME, "UTF-8");
    }

    @Test
    public void test_Remove_NonExistent() throws JSONException, IOException, DuplicatedEntryException {
        ActionsStorage actionsStorage = new ActionsStorage(mMockFileReader, mMockFileWriter, mMockContext);
        actionsStorage.remove(mMockAction1);
        actionsStorage.remove(mMockAction2);
        Mockito.verify(mMockFileWriter, never()).write(any(String.class), any(String.class), any(String.class));
    }

    @Mock
    Context mMockContext;

    @Mock
    FileReader mMockFileReader;

    @Mock
    FileWriter mMockFileWriter;

    @Mock
    Action mMockAction1;

    @Mock
    Action mMockAction2;

    Map<String, Action> mSampleActions;

    JSONObject mAction1Json;
    JSONObject mAction2Json;
    JSONArray mActionsArray;
    JSONObject mActionsJson;
}
