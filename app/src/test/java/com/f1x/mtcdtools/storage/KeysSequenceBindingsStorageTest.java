package com.f1x.mtcdtools.storage;

import com.f1x.mtcdtools.input.KeysSequenceBinding;
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
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by COMPUTER on 2017-01-29.
 */

public class KeysSequenceBindingsStorageTest {
    public void useDefaultData() throws JSONException, IOException {
        mKeysSequenceBindingsArray.put(mKeysSequenceBindings.get(0).toJson());
        mKeysSequenceBindingsArray.put(mKeysSequenceBindings.get(1).toJson());
        mKeysSequenceBindingsJson.put(KeysSequenceBindingsStorage.ROOT_ARRAY_NAME, mKeysSequenceBindingsArray);

        when(mMockFileReader.read(KeysSequenceBindingsStorage.STORAGE_FILE_NAME, "UTF-8")).thenReturn(mKeysSequenceBindingsJson.toString());
    }

    @Before
    public void init() throws JSONException, IOException {
        initMocks(this);

        mKeysSequenceBindings = new ArrayList<>();
        mKeysSequenceBindings.add(new KeysSequenceBinding(Arrays.asList(1, 2, 5), KeysSequenceBinding.TARGET_TYPE_ACTIONS_LIST, "binding1"));
        mKeysSequenceBindings.add(new KeysSequenceBinding(Arrays.asList(5, 6, 7), KeysSequenceBinding.TARGET_TYPE_ACTION, "binding2"));
        mKeysSequenceBindingsArray = new JSONArray();
        mKeysSequenceBindingsJson = new JSONObject();
    }

    @Test
    public void test_Read() throws JSONException, IOException, DuplicatedEntryException, EntryCreationFailed {
        useDefaultData();

        KeysSequenceBindingsStorage storage = new KeysSequenceBindingsStorage(mMockFileReader, mMockFileWriter);
        storage.read();
    }

    @Test(expected=DuplicatedEntryException.class)
    public void test_Read_Duplicated_Name() throws JSONException, IOException, EntryCreationFailed, DuplicatedEntryException {
        mKeysSequenceBindingsArray.put(mKeysSequenceBindings.get(0).toJson());
        mKeysSequenceBindingsArray.put(mKeysSequenceBindings.get(0).toJson());
        mKeysSequenceBindingsJson.put(KeysSequenceBindingsStorage.ROOT_ARRAY_NAME, mKeysSequenceBindingsArray);

        when(mMockFileReader.read(KeysSequenceBindingsStorage.STORAGE_FILE_NAME, "UTF-8")).thenReturn(mKeysSequenceBindingsJson.toString());

        KeysSequenceBindingsStorage storage = new KeysSequenceBindingsStorage(mMockFileReader, mMockFileWriter);
        storage.read();
    }

    @Test
    public void test_Write() throws JSONException, IOException, EntryCreationFailed, DuplicatedEntryException {
        useDefaultData();

        KeysSequenceBindingsStorage storage = new KeysSequenceBindingsStorage(mMockFileReader, mMockFileWriter);
        storage.read();
        storage.write();
        verify(mMockFileWriter, times(1)).write(mKeysSequenceBindingsJson.toString(), KeysSequenceBindingsStorage.STORAGE_FILE_NAME, "UTF-8");
    }

    @Test
    public void test_RemoveAction() throws IOException, JSONException, EntryCreationFailed, DuplicatedEntryException {
        useDefaultData();

        KeysSequenceBindingsStorage storage = new KeysSequenceBindingsStorage(mMockFileReader, mMockFileWriter);
        storage.read();

        storage.removeBindingsWithAction(mKeysSequenceBindings.get(1).getTargetName());
        assertNull(storage.getItem(mKeysSequenceBindings.get(1).getKeysSequence()));

        verify(mMockFileWriter, times(1)).write(any(String.class), eq(KeysSequenceBindingsStorage.STORAGE_FILE_NAME), eq("UTF-8"));
    }

    @Test
    public void test_RemoveActionsList() throws IOException, JSONException, EntryCreationFailed, DuplicatedEntryException {
        useDefaultData();

        KeysSequenceBindingsStorage storage = new KeysSequenceBindingsStorage(mMockFileReader, mMockFileWriter);
        storage.read();

        storage.removeBindingsWithActionsList(mKeysSequenceBindings.get(0).getTargetName());
        assertNull(storage.getItem(mKeysSequenceBindings.get(0).getKeysSequence()));

        verify(mMockFileWriter, times(1)).write(any(String.class), eq(KeysSequenceBindingsStorage.STORAGE_FILE_NAME), eq("UTF-8"));
    }

    @Test
    public void test_RemoveBindingWithTarget_WrongTargetType() throws IOException, JSONException, EntryCreationFailed, DuplicatedEntryException {
        useDefaultData();

        KeysSequenceBindingsStorage storage = new KeysSequenceBindingsStorage(mMockFileReader, mMockFileWriter);
        storage.read();

        storage.removeBindingsWithAction(mKeysSequenceBindings.get(0).getTargetName());
        assertNotNull(storage.getItem(mKeysSequenceBindings.get(0).getKeysSequence()));

        storage.removeBindingsWithActionsList(mKeysSequenceBindings.get(1).getTargetName());
        assertNotNull(storage.getItem(mKeysSequenceBindings.get(1).getKeysSequence()));

        verify(mMockFileWriter, times(2)).write(any(String.class), eq(KeysSequenceBindingsStorage.STORAGE_FILE_NAME), eq("UTF-8"));
    }

    @Test
    public void test_ReplaceActionName() throws IOException, JSONException, EntryCreationFailed, DuplicatedEntryException {
        useDefaultData();

        KeysSequenceBindingsStorage storage = new KeysSequenceBindingsStorage(mMockFileReader, mMockFileWriter);
        storage.read();

        String newActionName = "actionNewName";
        storage.replaceActionName(mKeysSequenceBindings.get(1).getTargetName(), newActionName);
        assertEquals(newActionName, storage.getItem(mKeysSequenceBindings.get(1).getKeysSequence()).getTargetName());

        verify(mMockFileWriter, times(1)).write(any(String.class), eq(KeysSequenceBindingsStorage.STORAGE_FILE_NAME), eq("UTF-8"));
    }

    @Test
    public void test_ReplaceActionName_WrongTargetType() throws IOException, JSONException, EntryCreationFailed, DuplicatedEntryException {
        useDefaultData();

        KeysSequenceBindingsStorage storage = new KeysSequenceBindingsStorage(mMockFileReader, mMockFileWriter);
        storage.read();

        String newActionName = "actionNewName";
        storage.replaceActionName(mKeysSequenceBindings.get(0).getTargetName(), newActionName);
        assertNotEquals(newActionName, storage.getItem(mKeysSequenceBindings.get(0).getKeysSequence()).getTargetName());

        verify(mMockFileWriter, times(1)).write(any(String.class), eq(KeysSequenceBindingsStorage.STORAGE_FILE_NAME), eq("UTF-8"));
    }

    @Test
    public void test_ReplaceActionName_NonExistentName() throws IOException, JSONException, EntryCreationFailed, DuplicatedEntryException {
        useDefaultData();

        KeysSequenceBindingsStorage storage = new KeysSequenceBindingsStorage(mMockFileReader, mMockFileWriter);
        storage.read();

        String nonExistentActionName = "nonExistentAction";
        String newActionName = "actionNewName";
        storage.replaceActionName(nonExistentActionName, newActionName);

        for(int i = 0; i < mKeysSequenceBindings.size(); ++i){
            assertNotSame(newActionName, storage.getItem(mKeysSequenceBindings.get(i).getKeysSequence()).getTargetName());
        }

        verify(mMockFileWriter, times(1)).write(any(String.class), eq(KeysSequenceBindingsStorage.STORAGE_FILE_NAME), eq("UTF-8"));
    }

    @Test
    public void test_ReplaceActionsListName() throws IOException, JSONException, EntryCreationFailed, DuplicatedEntryException {
        useDefaultData();

        KeysSequenceBindingsStorage storage = new KeysSequenceBindingsStorage(mMockFileReader, mMockFileWriter);
        storage.read();

        String newActionsListName = "actionsListNewName";
        storage.replaceActionsListName(mKeysSequenceBindings.get(0).getTargetName(), newActionsListName);
        assertEquals(newActionsListName, storage.getItem(mKeysSequenceBindings.get(0).getKeysSequence()).getTargetName());

        verify(mMockFileWriter, times(1)).write(any(String.class), eq(KeysSequenceBindingsStorage.STORAGE_FILE_NAME), eq("UTF-8"));
    }

    @Test
    public void test_ReplaceActionsListName_WrongTargetType() throws IOException, JSONException, EntryCreationFailed, DuplicatedEntryException {
        useDefaultData();

        KeysSequenceBindingsStorage storage = new KeysSequenceBindingsStorage(mMockFileReader, mMockFileWriter);
        storage.read();

        String newActionsListName = "actionsListNewName";
        storage.replaceActionsListName(mKeysSequenceBindings.get(1).getTargetName(), newActionsListName);
        assertNotEquals(newActionsListName, storage.getItem(mKeysSequenceBindings.get(1).getKeysSequence()).getTargetName());

        verify(mMockFileWriter, times(1)).write(any(String.class), eq(KeysSequenceBindingsStorage.STORAGE_FILE_NAME), eq("UTF-8"));
    }

    @Test
    public void test_ReplaceActionsListName_NonExistentName() throws IOException, JSONException, EntryCreationFailed, DuplicatedEntryException {
        useDefaultData();

        KeysSequenceBindingsStorage storage = new KeysSequenceBindingsStorage(mMockFileReader, mMockFileWriter);
        storage.read();

        String nonExistentActionsListName = "nonExistentActionsList";
        String newActionsListName = "actionsListNewName";
        storage.replaceActionsListName(nonExistentActionsListName, newActionsListName);

        for(int i = 0; i < mKeysSequenceBindings.size(); ++i){
            assertNotSame(newActionsListName, storage.getItem(mKeysSequenceBindings.get(i).getKeysSequence()).getTargetName());
        }

        verify(mMockFileWriter, times(1)).write(any(String.class), eq(KeysSequenceBindingsStorage.STORAGE_FILE_NAME), eq("UTF-8"));
    }

    @Mock
    FileReader mMockFileReader;

    @Mock
    FileWriter mMockFileWriter;

    private List<KeysSequenceBinding> mKeysSequenceBindings;
    private JSONObject mKeysSequenceBindingsJson;
    private JSONArray mKeysSequenceBindingsArray;
}
