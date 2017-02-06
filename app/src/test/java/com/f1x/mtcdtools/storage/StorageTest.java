package com.f1x.mtcdtools.storage;

import com.f1x.mtcdtools.storage.exceptions.DuplicatedEntryException;
import com.f1x.mtcdtools.storage.exceptions.EntryCreationFailed;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

/**
 * Created by f1x on 2017-01-29.
 */

public class StorageTest {
    private class SimpleStorage extends Storage<Integer, Integer> {
        SimpleStorage() {
            super(null, null);
        }

        @Override
        public void read() throws JSONException, IOException, DuplicatedEntryException, EntryCreationFailed {
            for(Map.Entry<Integer, Integer> entry : mTestItems.entrySet()) {
                mItems.put(entry.getKey(), entry.getValue());
            }
        }

        @Override
        public void write() throws JSONException, IOException {

        }

        @Override
        protected Map<Integer, Integer> createContainer() {
            return new HashMap<>();
        }
    }

    @Before
    public void init() {
        mTestItems = new HashMap<>();
        mTestItems.put(1, 10);
        mTestItems.put(2, 20);
        mTestItems.put(3, 30);
        mTestItems.put(4, 40);
    }

    @Test
    public void test_Insert() throws IOException, JSONException, DuplicatedEntryException, EntryCreationFailed {
        HashMap<Integer, Integer> testItems = new HashMap<>(mTestItems);
        testItems.put(5, 50);

        SimpleStorage storage = new SimpleStorage();
        storage.read();
        storage.insert(5, 50);

        assertEquals(testItems, storage.getItems());
    }

    @Test(expected=DuplicatedEntryException.class)
    public void test_Insert_Duplicated() throws IOException, JSONException, DuplicatedEntryException, EntryCreationFailed {
        SimpleStorage storage = new SimpleStorage();
        storage.read();
        storage.insert(4, 40);
    }

    @Test
    public void test_Remove() throws IOException, JSONException, DuplicatedEntryException, EntryCreationFailed {
        HashMap<Integer, Integer> testItems = new HashMap<>(mTestItems);
        testItems.remove(3);

        SimpleStorage storage = new SimpleStorage();
        storage.read();
        storage.remove(3);

        assertEquals(testItems, storage.getItems());
    }

    @Test
    public void test_Remove_NonExistentElement() throws IOException, JSONException, DuplicatedEntryException, EntryCreationFailed {
        SimpleStorage storage = new SimpleStorage();
        storage.read();
        storage.remove(5);

        assertEquals(mTestItems, storage.getItems());
    }

    @Test
    public void test_Replace() throws IOException, JSONException, DuplicatedEntryException, EntryCreationFailed {
        HashMap<Integer, Integer> testItems = new HashMap<>(mTestItems);
        testItems.put(3, 100);

        SimpleStorage storage = new SimpleStorage();
        storage.read();
        storage.replace(3, 3, 132);
        testItems.put(3, 132);

        assertEquals(testItems, storage.getItems());
        assertEquals(Integer.valueOf(132), storage.getItem(3));
    }

    @Test
    public void test_Replace_NewKey() throws IOException, JSONException, DuplicatedEntryException, EntryCreationFailed {
        HashMap<Integer, Integer> testItems = new HashMap<>(mTestItems);
        testItems.remove(2);
        testItems.put(9, 1000);

        SimpleStorage storage = new SimpleStorage();
        storage.read();
        storage.replace(2, 9, 1000);

        assertEquals(testItems, storage.getItems());
    }

    @Test(expected=DuplicatedEntryException.class)
    public void test_Replace_NewKeyAlreadyExists() throws IOException, JSONException, DuplicatedEntryException, EntryCreationFailed {
        SimpleStorage storage = new SimpleStorage();
        storage.read();
        storage.replace(2, 3, 1000);
    }

    @Test
    public void test_Replace_NonExistentKey() throws IOException, JSONException, DuplicatedEntryException, EntryCreationFailed {
        SimpleStorage storage = new SimpleStorage();
        storage.read();
        storage.replace(11, 33, 1000);

        assertNull(storage.getItem(33));
    }

    @Test
    public void test_getItem() throws IOException, JSONException, DuplicatedEntryException, EntryCreationFailed {
        SimpleStorage storage = new SimpleStorage();
        storage.read();

        for(Map.Entry<Integer, Integer> entry : mTestItems.entrySet()) {
            assertEquals(entry.getValue(), storage.getItem(entry.getKey()));
        }

        assertEquals(null, storage.getItem(5));
    }

    private HashMap<Integer, Integer> mTestItems;
}
