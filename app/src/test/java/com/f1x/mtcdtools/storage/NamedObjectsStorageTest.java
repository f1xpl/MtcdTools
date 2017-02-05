package com.f1x.mtcdtools.storage;

import com.f1x.mtcdtools.ActionsList;
import com.f1x.mtcdtools.actions.KeyAction;
import com.f1x.mtcdtools.actions.LaunchAction;
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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by COMPUTER on 2017-02-05.
 */

public class NamedObjectsStorageTest {
    public void prepareDefaultData() {
        mNamedObjectsLists.add(new KeyAction("KeyActionName", 123));
        mNamedObjectsLists.add(new KeyAction("KeyActionName2", 51));
        mNamedObjectsLists.add(new LaunchAction("LaunchActionName", "com.test.package"));

        mNamedObjectsLists.add(new ActionsList("ZActionsListName", Arrays.asList(7, 8, 9), Arrays.asList(0, 3, 9),
                new TreeSet<>(Arrays.asList(mNamedObjectsLists.get(0).getName(), mNamedObjectsLists.get(1).getName()))));
        mNamedObjectsLists.add(new ActionsList("ZActionsListName2", Arrays.asList(5, 9, 1), Arrays.asList(7, 7, 3),
                new TreeSet<>(Arrays.asList(mNamedObjectsLists.get(0).getName(), mNamedObjectsLists.get(1).getName(), mNamedObjectsLists.get(2).getName()))));
    }

    public void useDefaultData() throws JSONException, IOException {
        for(int i = 0; i < mNamedObjectsLists.size(); ++i) {
            mNamedObjectsArray.put(mNamedObjectsLists.get(i).toJson());
        }

        mNamedObjectsJson.put(NamedObjectsStorage.ROOT_ARRAY_NAME, mNamedObjectsArray);

        when(mMockFileReader.read(NamedObjectsStorage.STORAGE_FILE_NAME, "UTF-8")).thenReturn(mNamedObjectsJson.toString());
    }

    @Before
    public void init() throws JSONException, IOException {
        initMocks(this);

        mNamedObjectsLists = new ArrayList<>();
        mNamedObjectsArray = new JSONArray();
        mNamedObjectsJson = new JSONObject();

        prepareDefaultData();
    }

    @Test
    public void test_Read() throws JSONException, IOException, EntryCreationFailed, DuplicatedEntryException {
        useDefaultData();

        NamedObjectsStorage storage = new NamedObjectsStorage(mMockFileReader, mMockFileWriter);
        storage.read();
    }

    @Test(expected=DuplicatedEntryException.class)
    public void test_Read_Duplicated_Name() throws JSONException, IOException, EntryCreationFailed, DuplicatedEntryException {
        mNamedObjectsArray.put(mNamedObjectsLists.get(0).toJson());
        mNamedObjectsArray.put(mNamedObjectsLists.get(0).toJson());
        mNamedObjectsJson.put(NamedObjectsStorage.ROOT_ARRAY_NAME, mNamedObjectsArray);
        when(mMockFileReader.read(NamedObjectsStorage.STORAGE_FILE_NAME, "UTF-8")).thenReturn(mNamedObjectsJson.toString());

        NamedObjectsStorage storage = new NamedObjectsStorage(mMockFileReader, mMockFileWriter);
        storage.read();
    }

    @Test
    public void test_Write() throws JSONException, IOException, EntryCreationFailed, DuplicatedEntryException {
        useDefaultData();

        NamedObjectsStorage storage = new NamedObjectsStorage(mMockFileReader, mMockFileWriter);
        storage.read();
        storage.write();
        verify(mMockFileWriter, times(1)).write(mNamedObjectsJson.toString(), NamedObjectsStorage.STORAGE_FILE_NAME, "UTF-8");
    }

    @Test(expected=DuplicatedEntryException.class)
    public void test_IgnoreNameCase() throws JSONException, IOException, EntryCreationFailed, DuplicatedEntryException {
        useDefaultData();

        NamedObjectsStorage storage = new NamedObjectsStorage(mMockFileReader, mMockFileWriter);
        storage.read();

        NamedObject anotherNamedObject = mock(NamedObject.class);
        storage.insert(mNamedObjectsLists.get(0).getName().toUpperCase(), anotherNamedObject);
    }

    @Test
    public void test_RemoveDependency_NonExistentName() throws IOException, JSONException, EntryCreationFailed, DuplicatedEntryException {
        useDefaultData();

        NamedObjectsStorage storage = new NamedObjectsStorage(mMockFileReader, mMockFileWriter);
        storage.read();

        String nonExistentName = "nonExistentName";
        storage.remove(nonExistentName);

        for(int i = 0; i < mNamedObjectsLists.size(); ++i) {
            assertNotNull(storage.getItem(mNamedObjectsLists.get(i).getName()));
        }
    }

    @Test
    public void test_RemoveDependency() throws IOException, JSONException, EntryCreationFailed, DuplicatedEntryException {
        useDefaultData();

        NamedObjectsStorage storage = new NamedObjectsStorage(mMockFileReader, mMockFileWriter);
        storage.read();

        String removedObjectName = mNamedObjectsLists.get(0).getName();
        storage.remove(removedObjectName);

        ActionsList actionsList1 = (ActionsList)storage.getItem(mNamedObjectsLists.get(3).getName());
        assertFalse(actionsList1.getActionNames().contains(removedObjectName));
        assertTrue(actionsList1.getActionNames().contains(mNamedObjectsLists.get(1).getName()));

        ActionsList actionsList2 = (ActionsList)storage.getItem(mNamedObjectsLists.get(4).getName());
        assertFalse(actionsList2.getActionNames().contains(removedObjectName));
        assertTrue(actionsList2.getActionNames().contains(mNamedObjectsLists.get(1).getName()));
        assertTrue(actionsList2.getActionNames().contains(mNamedObjectsLists.get(2).getName()));

        assertNull(storage.getItem(removedObjectName));

        verify(mMockFileWriter, times(1)).write(any(String.class), eq(NamedObjectsStorage.STORAGE_FILE_NAME), eq("UTF-8"));
    }

    @Test
    public void test_ReplaceDependency_TheSameName() throws IOException, JSONException, EntryCreationFailed, DuplicatedEntryException {
        useDefaultData();

        NamedObjectsStorage storage = new NamedObjectsStorage(mMockFileReader, mMockFileWriter);
        storage.read();

        storage.replace(mNamedObjectsLists.get(0).getName(), mNamedObjectsLists.get(0).getName(), mNamedObjectsLists.get(0));

        ActionsList actionsList1 = (ActionsList)storage.getItem(mNamedObjectsLists.get(3).getName());
        assertTrue(actionsList1.getActionNames().contains(mNamedObjectsLists.get(0).getName()));
        assertTrue(actionsList1.getActionNames().contains(mNamedObjectsLists.get(1).getName()));

        ActionsList actionsList2 = (ActionsList)storage.getItem(mNamedObjectsLists.get(4).getName());
        assertTrue(actionsList2.getActionNames().contains(mNamedObjectsLists.get(0).getName()));
        assertTrue(actionsList2.getActionNames().contains(mNamedObjectsLists.get(1).getName()));
        assertTrue(actionsList2.getActionNames().contains(mNamedObjectsLists.get(2).getName()));
    }

    @Test
    public void test_ReplaceDependency_NonExistentName() throws IOException, JSONException, EntryCreationFailed, DuplicatedEntryException {
        useDefaultData();

        NamedObjectsStorage storage = new NamedObjectsStorage(mMockFileReader, mMockFileWriter);
        storage.read();

        String nonExistentName = "nonExistentName";
        KeyAction newKeyAction = new KeyAction("newKeyAction", 54);
        storage.replace(nonExistentName, newKeyAction.getName(), newKeyAction);

        assertNull(storage.getItem(newKeyAction.getName()));

        ActionsList actionsList1 = (ActionsList)storage.getItem(mNamedObjectsLists.get(3).getName());
        assertTrue(actionsList1.getActionNames().contains(mNamedObjectsLists.get(0).getName()));
        assertTrue(actionsList1.getActionNames().contains(mNamedObjectsLists.get(1).getName()));

        ActionsList actionsList2 = (ActionsList)storage.getItem(mNamedObjectsLists.get(4).getName());
        assertTrue(actionsList2.getActionNames().contains(mNamedObjectsLists.get(0).getName()));
        assertTrue(actionsList2.getActionNames().contains(mNamedObjectsLists.get(1).getName()));
        assertTrue(actionsList2.getActionNames().contains(mNamedObjectsLists.get(2).getName()));
    }

    @Test
    public void test_ReplaceDependency() throws IOException, JSONException, EntryCreationFailed, DuplicatedEntryException {
        useDefaultData();

        NamedObjectsStorage storage = new NamedObjectsStorage(mMockFileReader, mMockFileWriter);
        storage.read();

        String replacedObjectName = mNamedObjectsLists.get(1).getName();
        KeyAction newKeyAction = new KeyAction("newKeyAction", 54);
        storage.replace(replacedObjectName, newKeyAction.getName(), newKeyAction);

        ActionsList actionsList1 = (ActionsList)storage.getItem(mNamedObjectsLists.get(3).getName());
        assertFalse(actionsList1.getActionNames().contains(replacedObjectName));
        assertTrue(actionsList1.getActionNames().contains(mNamedObjectsLists.get(0).getName()));

        ActionsList actionsList2 = (ActionsList)storage.getItem(mNamedObjectsLists.get(4).getName());
        assertFalse(actionsList2.getActionNames().contains(replacedObjectName));
        assertTrue(actionsList2.getActionNames().contains(mNamedObjectsLists.get(0).getName()));
        assertTrue(actionsList2.getActionNames().contains(mNamedObjectsLists.get(2).getName()));
        assertTrue(actionsList2.getActionNames().contains(newKeyAction.getName()));

        assertNull(storage.getItem(replacedObjectName));
        assertEquals(newKeyAction, storage.getItem(newKeyAction.getName()));

        verify(mMockFileWriter, times(1)).write(any(String.class), eq(NamedObjectsStorage.STORAGE_FILE_NAME), eq("UTF-8"));
    }

    @Mock
    FileReader mMockFileReader;

    @Mock
    FileWriter mMockFileWriter;

    private List<NamedObject> mNamedObjectsLists;
    private JSONArray mNamedObjectsArray;
    private JSONObject mNamedObjectsJson;
}
