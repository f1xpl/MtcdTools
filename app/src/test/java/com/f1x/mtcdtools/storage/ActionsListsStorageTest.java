package com.f1x.mtcdtools.storage;

import com.f1x.mtcdtools.ActionsList;
import com.f1x.mtcdtools.storage.exceptions.DuplicatedEntryException;
import com.f1x.mtcdtools.storage.exceptions.EntryCreationFailed;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by COMPUTER on 2017-01-15.
 */

public class ActionsListsStorageTest {
    public void useDefaultData() throws JSONException, IOException {
        mActionsListsArray.put(mActionsLists.get(0).toJson());
        mActionsListsArray.put(mActionsLists.get(1).toJson());

        mActionsListsJson.put(ActionsListsStorage.ROOT_ARRAY_NAME, mActionsListsArray);

        when(mMockFileReader.read(ActionsListsStorage.STORAGE_FILE_NAME, "UTF-8")).thenReturn(mActionsListsJson.toString());
    }

    @Before
    public void init() throws JSONException, IOException {
        initMocks(this);

        mActionsLists = new ArrayList<>();
        mActionsLists.add(new ActionsList("List1",
                Arrays.asList(1, 2, 3),
                Arrays.asList(4, 5, 6),
                new TreeSet<>(Arrays.asList("action1", "action2", "action3"))));

        mActionsLists.add(new ActionsList("List2",
                Arrays.asList(7, 8, 9),
                Arrays.asList(0, 3, 9),
                new TreeSet<>(Arrays.asList("action5", "action6", "action7", "action2"))));

        mActionsListsArray = new JSONArray();
        mActionsListsJson = new JSONObject();
    }

    @Test
    public void test_Read() throws JSONException, IOException, EntryCreationFailed, DuplicatedEntryException {
        useDefaultData();

        ActionsListsStorage storage = new ActionsListsStorage(mMockFileReader, mMockFileWriter);
        storage.read();
    }

    @Test(expected=DuplicatedEntryException.class)
    public void test_Read_Duplicated_Name() throws JSONException, IOException, EntryCreationFailed, DuplicatedEntryException {
        mActionsListsArray.put(mActionsLists.get(0).toJson());
        mActionsListsArray.put(mActionsLists.get(0).toJson());
        mActionsListsJson.put(ActionsListsStorage.ROOT_ARRAY_NAME, mActionsListsArray);
        when(mMockFileReader.read(ActionsListsStorage.STORAGE_FILE_NAME, "UTF-8")).thenReturn(mActionsListsJson.toString());

        ActionsListsStorage storage = new ActionsListsStorage(mMockFileReader, mMockFileWriter);
        storage.read();
    }

    @Test
    public void test_Write() throws JSONException, IOException, EntryCreationFailed, DuplicatedEntryException {
        useDefaultData();

        ActionsListsStorage storage = new ActionsListsStorage(mMockFileReader, mMockFileWriter);
        storage.read();
        storage.write();
        verify(mMockFileWriter, times(1)).write(mActionsListsJson.toString(), ActionsListsStorage.STORAGE_FILE_NAME, "UTF-8");
    }

    @Test(expected=DuplicatedEntryException.class)
    public void test_IgnoreNameCase() throws JSONException, IOException, EntryCreationFailed, DuplicatedEntryException {
        useDefaultData();

        ActionsListsStorage storage = new ActionsListsStorage(mMockFileReader, mMockFileWriter);
        storage.read();

        ActionsList anotherActionsList = mock(ActionsList.class);
        storage.insert(mActionsLists.get(0).getName().toUpperCase(), anotherActionsList);
    }

    @Test
    public void test_RemoveActionFromActionsList() throws IOException, JSONException, EntryCreationFailed, DuplicatedEntryException {
        useDefaultData();

        ActionsListsStorage storage = new ActionsListsStorage(mMockFileReader, mMockFileWriter);
        storage.read();

        storage.removeActionFromActionsList("action2");
        assertFalse(storage.getItem(mActionsLists.get(0).getName()).getActionNames().contains("action2"));
        assertTrue(storage.getItem(mActionsLists.get(0).getName()).getActionNames().contains("action1"));
        assertTrue(storage.getItem(mActionsLists.get(0).getName()).getActionNames().contains("action3"));

        assertFalse(storage.getItem(mActionsLists.get(1).getName()).getActionNames().contains("action2"));
        assertTrue(storage.getItem(mActionsLists.get(1).getName()).getActionNames().contains("action5"));
        assertTrue(storage.getItem(mActionsLists.get(1).getName()).getActionNames().contains("action6"));
        assertTrue(storage.getItem(mActionsLists.get(1).getName()).getActionNames().contains("action7"));
    }

    @Mock
    FileReader mMockFileReader;

    @Mock
    FileWriter mMockFileWriter;

    private List<ActionsList> mActionsLists;

    private JSONArray mActionsListsArray;
    private JSONObject mActionsListsJson;
}
