package com.f1x.mtcdtools.storage;

import com.f1x.mtcdtools.named.objects.ActionsList;
import com.f1x.mtcdtools.named.objects.NamedObject;
import com.f1x.mtcdtools.named.objects.NamedObjectId;
import com.f1x.mtcdtools.named.objects.actions.KeyAction;
import com.f1x.mtcdtools.named.objects.actions.LaunchAction;
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
 * Created by f1x on 2017-02-05.
 */

public class NamedObjectsStorageTest {
    public void prepareDefaultData() {
        mNamedObjectsLists.add(new KeyAction(new NamedObjectId("KeyActionName"), 123));
        mNamedObjectsLists.add(new KeyAction(new NamedObjectId("KeyActionName2"), 51));
        mNamedObjectsLists.add(new LaunchAction(new NamedObjectId("LaunchActionName"), "com.test.package"));

        mNamedObjectsLists.add(new ActionsList(new NamedObjectId("ActionsListName"), Arrays.asList(7, 8, 9), Arrays.asList(0, 3, 9),
                Arrays.asList(mNamedObjectsLists.get(0).getId(), mNamedObjectsLists.get(1).getId())));
        mNamedObjectsLists.add(new ActionsList(new NamedObjectId("ActionsListName2"), Arrays.asList(5, 9, 1), Arrays.asList(7, 7, 3),
                Arrays.asList(mNamedObjectsLists.get(0).getId(), mNamedObjectsLists.get(1).getId(), mNamedObjectsLists.get(2).getId())));
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

    @Test
    public void test_RemoveDependency_NonExistentName() throws IOException, JSONException, EntryCreationFailed, DuplicatedEntryException {
        useDefaultData();

        NamedObjectsStorage storage = new NamedObjectsStorage(mMockFileReader, mMockFileWriter);
        storage.read();

        NamedObjectId nonExistentId = new NamedObjectId("nonExistentName");
        storage.remove(nonExistentId);

        for(int i = 0; i < mNamedObjectsLists.size(); ++i) {
            assertNotNull(storage.getItem(mNamedObjectsLists.get(i).getId()));
        }
    }

    @Test
    public void test_RemoveDependency() throws IOException, JSONException, EntryCreationFailed, DuplicatedEntryException {
        useDefaultData();

        NamedObjectsStorage storage = new NamedObjectsStorage(mMockFileReader, mMockFileWriter);
        storage.read();

        NamedObjectId removedObjectId = mNamedObjectsLists.get(0).getId();
        storage.remove(removedObjectId);

        ActionsList actionsList1 = (ActionsList)storage.getItem(mNamedObjectsLists.get(3).getId());

        assertNotNull(actionsList1);
        assertFalse(actionsList1.getActionIds().contains(removedObjectId));
        assertTrue(actionsList1.getActionIds().contains(mNamedObjectsLists.get(1).getId()));

        ActionsList actionsList2 = (ActionsList)storage.getItem(mNamedObjectsLists.get(4).getId());
        assertNotNull(actionsList2);

        assertFalse(actionsList2.getActionIds().contains(removedObjectId));
        assertTrue(actionsList2.getActionIds().contains(mNamedObjectsLists.get(1).getId()));
        assertTrue(actionsList2.getActionIds().contains(mNamedObjectsLists.get(2).getId()));

        assertNull(storage.getItem(removedObjectId));

        verify(mMockFileWriter, times(1)).write(any(String.class), eq(NamedObjectsStorage.STORAGE_FILE_NAME), eq("UTF-8"));
    }

    @Test
    public void test_ReplaceDependency_TheSameName() throws IOException, JSONException, EntryCreationFailed, DuplicatedEntryException {
        useDefaultData();

        NamedObjectsStorage storage = new NamedObjectsStorage(mMockFileReader, mMockFileWriter);
        storage.read();

        storage.replace(mNamedObjectsLists.get(0).getId(), mNamedObjectsLists.get(0).getId(), mNamedObjectsLists.get(0));

        ActionsList actionsList1 = (ActionsList)storage.getItem(mNamedObjectsLists.get(3).getId());
        assertTrue(actionsList1.getActionIds().contains(mNamedObjectsLists.get(0).getId()));
        assertTrue(actionsList1.getActionIds().contains(mNamedObjectsLists.get(1).getId()));

        ActionsList actionsList2 = (ActionsList)storage.getItem(mNamedObjectsLists.get(4).getId());
        assertTrue(actionsList2.getActionIds().contains(mNamedObjectsLists.get(0).getId()));
        assertTrue(actionsList2.getActionIds().contains(mNamedObjectsLists.get(1).getId()));
        assertTrue(actionsList2.getActionIds().contains(mNamedObjectsLists.get(2).getId()));
    }

    @Test
    public void test_ReplaceDependency_NonExistentName() throws IOException, JSONException, EntryCreationFailed, DuplicatedEntryException {
        useDefaultData();

        NamedObjectsStorage storage = new NamedObjectsStorage(mMockFileReader, mMockFileWriter);
        storage.read();

        NamedObjectId nonExistentId = new NamedObjectId("nonExistentName");
        KeyAction newKeyAction = new KeyAction(new NamedObjectId("newKeyAction"), 54);
        storage.replace(nonExistentId, newKeyAction.getId(), newKeyAction);

        assertNull(storage.getItem(newKeyAction.getId()));

        ActionsList actionsList1 = (ActionsList)storage.getItem(mNamedObjectsLists.get(3).getId());
        assertTrue(actionsList1.getActionIds().contains(mNamedObjectsLists.get(0).getId()));
        assertTrue(actionsList1.getActionIds().contains(mNamedObjectsLists.get(1).getId()));

        ActionsList actionsList2 = (ActionsList)storage.getItem(mNamedObjectsLists.get(4).getId());
        assertTrue(actionsList2.getActionIds().contains(mNamedObjectsLists.get(0).getId()));
        assertTrue(actionsList2.getActionIds().contains(mNamedObjectsLists.get(1).getId()));
        assertTrue(actionsList2.getActionIds().contains(mNamedObjectsLists.get(2).getId()));
    }

    @Test
    public void test_ReplaceDependency() throws IOException, JSONException, EntryCreationFailed, DuplicatedEntryException {
        useDefaultData();

        NamedObjectsStorage storage = new NamedObjectsStorage(mMockFileReader, mMockFileWriter);
        storage.read();

        NamedObjectId replacedObjectId = mNamedObjectsLists.get(1).getId();
        KeyAction newKeyAction = new KeyAction(new NamedObjectId("newKeyAction"), 54);
        storage.replace(replacedObjectId, newKeyAction.getId(), newKeyAction);

        ActionsList actionsList1 = (ActionsList)storage.getItem(mNamedObjectsLists.get(3).getId());
        assertFalse(actionsList1.getActionIds().contains(replacedObjectId));
        assertTrue(actionsList1.getActionIds().contains(mNamedObjectsLists.get(0).getId()));

        ActionsList actionsList2 = (ActionsList)storage.getItem(mNamedObjectsLists.get(4).getId());
        assertFalse(actionsList2.getActionIds().contains(replacedObjectId));
        assertTrue(actionsList2.getActionIds().contains(mNamedObjectsLists.get(0).getId()));
        assertTrue(actionsList2.getActionIds().contains(mNamedObjectsLists.get(2).getId()));
        assertTrue(actionsList2.getActionIds().contains(newKeyAction.getId()));

        assertNull(storage.getItem(replacedObjectId));
        assertEquals(newKeyAction, storage.getItem(newKeyAction.getId()));

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
