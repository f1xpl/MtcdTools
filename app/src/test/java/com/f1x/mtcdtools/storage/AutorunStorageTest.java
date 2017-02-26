package com.f1x.mtcdtools.storage;

import com.f1x.mtcdtools.named.objects.NamedObjectId;
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
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by COMPUTER on 2017-02-26.
 */

public class AutorunStorageTest {
    @Before
    public void init() throws JSONException, IOException {
        initMocks(this);

        mNamedObjectIds = new ArrayList<>();
        mAutorunJson = new JSONObject();
        mAutorunJsonArray = new JSONArray();

        mNamedObjectIds.addAll(Arrays.asList(new NamedObjectId("object1"), new NamedObjectId("object2"), new NamedObjectId("object3"), new NamedObjectId("object4")));
    }

    public void useDefaultData() throws JSONException, IOException {
        for(NamedObjectId id : mNamedObjectIds) {
            mAutorunJsonArray.put(id.toString());
        }

        mAutorunJson.put(AutorunStorage.ROOT_ARRAY_NAME, mAutorunJsonArray);
        when(mMockFileReader.read(AutorunStorage.STORAGE_FILE_NAME, "UTF-8")).thenReturn(mAutorunJson.toString());
    }

    @Test
    public void test_Read() throws JSONException, IOException, DuplicatedEntryException, EntryCreationFailed {
        useDefaultData();

        AutorunStorage storage = new AutorunStorage(mMockFileReader, mMockFileWriter);
        storage.read();

        assertEquals(mNamedObjectIds, storage.getItems());
    }

    @Test
    public void test_Write() throws JSONException, IOException, EntryCreationFailed, DuplicatedEntryException {
        useDefaultData();

        AutorunStorage storage = new AutorunStorage(mMockFileReader, mMockFileWriter);
        storage.read();
        storage.write();

        verify(mMockFileWriter, times(1)).write(mAutorunJson.toString(), AutorunStorage.STORAGE_FILE_NAME, "UTF-8");
    }

    @Test
    public void test_Insert() throws JSONException, IOException, EntryCreationFailed, DuplicatedEntryException {
        useDefaultData();

        AutorunStorage storage = new AutorunStorage(mMockFileReader, mMockFileWriter);
        storage.read();

        NamedObjectId newObjectId = new NamedObjectId("newObject");
        storage.insert(newObjectId);

        mAutorunJsonArray.put(newObjectId.toString());
        mAutorunJson.remove(AutorunStorage.ROOT_ARRAY_NAME);
        mAutorunJson.put(AutorunStorage.ROOT_ARRAY_NAME, mAutorunJsonArray);

        verify(mMockFileWriter, times(1)).write(mAutorunJson.toString(), AutorunStorage.STORAGE_FILE_NAME, "UTF-8");
    }

    @Test
    public void test_Remove() throws JSONException, IOException, EntryCreationFailed, DuplicatedEntryException {
        useDefaultData();

        AutorunStorage storage = new AutorunStorage(mMockFileReader, mMockFileWriter);
        storage.read();

        NamedObjectId removedObjectId = mNamedObjectIds.get(2);
        storage.remove(removedObjectId);

        mAutorunJsonArray.remove(2);
        mAutorunJson.remove(AutorunStorage.ROOT_ARRAY_NAME);
        mAutorunJson.put(AutorunStorage.ROOT_ARRAY_NAME, mAutorunJsonArray);

        verify(mMockFileWriter, times(1)).write(mAutorunJson.toString(), AutorunStorage.STORAGE_FILE_NAME, "UTF-8");
    }

    @Test
    public void test_Remove_NonExistentItem() throws JSONException, IOException, EntryCreationFailed, DuplicatedEntryException {
        useDefaultData();

        AutorunStorage storage = new AutorunStorage(mMockFileReader, mMockFileWriter);
        storage.read();

        storage.remove(new NamedObjectId("nonexistentname"));
        verify(mMockFileWriter, times(0)).write(any(String.class), any(String.class), any(String.class));
    }

    @Mock
    FileReader mMockFileReader;

    @Mock
    FileWriter mMockFileWriter;

    private List<NamedObjectId> mNamedObjectIds;
    private JSONObject mAutorunJson;
    private JSONArray mAutorunJsonArray;
}
