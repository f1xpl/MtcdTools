package com.f1x.mtcdtools.storage;

import com.f1x.mtcdtools.actions.Action;
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
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by COMPUTER on 2017-01-15.
 */

@PowerMockIgnore({ "org.mockito.*", "org.robolectric.*", "android.*" })
@PrepareForTest({ActionsStorage.class})
@RunWith(PowerMockRunner.class)

public class ActionsStorageTest {
    public void useDefaultData() throws JSONException, IOException {
        mActionsArray.put(mActions.get(0).toJson());
        mActionsArray.put(mActions.get(1).toJson());
        mActionsJson.put(ActionsStorage.ROOT_ARRAY_NAME, mActionsArray);

        when(mMockFileReader.read(ActionsStorage.STORAGE_FILE_NAME, "UTF-8")).thenReturn(mActionsJson.toString());
    }

    @Before
    public void init() throws JSONException {
        initMocks(this);
        mActions = new ArrayList<>();

        mActions.add(new KeyAction("action1", 123));
        mActions.add(new LaunchAction("action2", "com.test.package"));

        mActionsArray = new JSONArray();
        mActionsJson = new JSONObject();
    }

    @Test
    public void test_Read() throws JSONException, IOException, EntryCreationFailed, DuplicatedEntryException {
        useDefaultData();

        ActionsStorage storage = new ActionsStorage(mMockFileReader, mMockFileWriter);
        storage.read();
    }

    @Test(expected=DuplicatedEntryException.class)
    public void test_Read_Duplicated_Name() throws JSONException, IOException, EntryCreationFailed, DuplicatedEntryException {
        mActionsArray.put(mActions.get(0).toJson());
        mActionsArray.put(mActions.get(0).toJson());
        mActionsJson.put(ActionsStorage.ROOT_ARRAY_NAME, mActionsArray);

        when(mMockFileReader.read(ActionsStorage.STORAGE_FILE_NAME, "UTF-8")).thenReturn(mActionsJson.toString());

        ActionsStorage storage = new ActionsStorage(mMockFileReader, mMockFileWriter);
        storage.read();
    }

    @Test
    public void test_Write() throws JSONException, IOException, EntryCreationFailed, DuplicatedEntryException {
        useDefaultData();

        ActionsStorage storage = new ActionsStorage(mMockFileReader, mMockFileWriter);
        storage.read();
        storage.write();
        verify(mMockFileWriter, times(1)).write(mActionsJson.toString(), ActionsStorage.STORAGE_FILE_NAME, "UTF-8");
    }

    @Test(expected=DuplicatedEntryException.class)
    public void test_IgnoreNameCase() throws JSONException, IOException, EntryCreationFailed, DuplicatedEntryException {
        useDefaultData();

        ActionsStorage storage = new ActionsStorage(mMockFileReader, mMockFileWriter);
        storage.read();

        Action anotherAction = mock(Action.class);
        storage.insert(mActions.get(0).getName().toUpperCase(), anotherAction);
    }

    @Mock
    FileReader mMockFileReader;

    @Mock
    FileWriter mMockFileWriter;

    private List<Action> mActions;
    private JSONArray mActionsArray;
    private JSONObject mActionsJson;
}
