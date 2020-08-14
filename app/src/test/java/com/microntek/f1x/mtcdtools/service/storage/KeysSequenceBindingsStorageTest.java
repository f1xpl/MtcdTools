package com.microntek.f1x.mtcdtools.service.storage;

import com.microntek.f1x.mtcdtools.service.input.KeysSequenceBinding;
import com.microntek.f1x.mtcdtools.named.NamedObjectId;
import com.microntek.f1x.mtcdtools.service.storage.exceptions.DuplicatedEntryException;
import com.microntek.f1x.mtcdtools.service.storage.exceptions.EntryCreationFailed;

import junit.framework.Assert;

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
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by f1x on 2017-01-29.
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
        mKeysSequenceBindings.add(new KeysSequenceBinding(Arrays.asList(1, 2, 5), new NamedObjectId("binding1"), false));
        mKeysSequenceBindings.add(new KeysSequenceBinding(Arrays.asList(5, 6, 7), new NamedObjectId("binding2"), true));
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
    public void test_RemoveBindingWithTarget() throws IOException, JSONException, EntryCreationFailed, DuplicatedEntryException {
        useDefaultData();

        KeysSequenceBindingsStorage storage = new KeysSequenceBindingsStorage(mMockFileReader, mMockFileWriter);
        storage.read();

        storage.removeBindingWithTarget(mKeysSequenceBindings.get(0).getTargetId());
        assertNull(storage.getItem(mKeysSequenceBindings.get(0).getKeysSequence()));

        verify(mMockFileWriter, times(1)).write(any(String.class), eq(KeysSequenceBindingsStorage.STORAGE_FILE_NAME), eq("UTF-8"));
    }

    @Test
    public void test_Replace() throws IOException, JSONException, EntryCreationFailed, DuplicatedEntryException {
        useDefaultData();

        KeysSequenceBindingsStorage storage = new KeysSequenceBindingsStorage(mMockFileReader, mMockFileWriter);
        storage.read();

        storage.replace(mKeysSequenceBindings.get(0).getKeysSequence(), mKeysSequenceBindings.get(0).getKeysSequence(), mKeysSequenceBindings.get(0));
        Assert.assertEquals(mKeysSequenceBindings.get(0), storage.getItem(mKeysSequenceBindings.get(0).getKeysSequence()));
    }

    @Test
    public void test_ReplaceTargetName() throws IOException, JSONException, EntryCreationFailed, DuplicatedEntryException {
        useDefaultData();

        KeysSequenceBindingsStorage storage = new KeysSequenceBindingsStorage(mMockFileReader, mMockFileWriter);
        storage.read();

        NamedObjectId newTargetId = new NamedObjectId("targetNewName");
        storage.replaceTarget(mKeysSequenceBindings.get(0).getTargetId(), newTargetId);
        assertEquals(newTargetId, storage.getItem(mKeysSequenceBindings.get(0).getKeysSequence()).getTargetId());

        verify(mMockFileWriter, times(1)).write(any(String.class), eq(KeysSequenceBindingsStorage.STORAGE_FILE_NAME), eq("UTF-8"));
    }

    @Test
    public void test_ReplaceTargetName_NonExistentName() throws IOException, JSONException, EntryCreationFailed, DuplicatedEntryException {
        useDefaultData();

        KeysSequenceBindingsStorage storage = new KeysSequenceBindingsStorage(mMockFileReader, mMockFileWriter);
        storage.read();

        NamedObjectId nonExistentTargetId = new NamedObjectId("nonExistentTargetName");
        NamedObjectId newTargetId = new NamedObjectId("targetNewName");
        storage.replaceTarget(nonExistentTargetId, newTargetId);

        for(int i = 0; i < mKeysSequenceBindings.size(); ++i){
            assertNotSame(newTargetId, storage.getItem(mKeysSequenceBindings.get(i).getKeysSequence()).getTargetId());
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
