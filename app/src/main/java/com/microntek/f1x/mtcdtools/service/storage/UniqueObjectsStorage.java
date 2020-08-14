package com.microntek.f1x.mtcdtools.service.storage;

import com.microntek.f1x.mtcdtools.service.storage.exceptions.DuplicatedEntryException;

import org.json.JSONException;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by f1x on 2017-01-29.
 */

public abstract class UniqueObjectsStorage<Key, Value> extends FileStorage {
    public UniqueObjectsStorage(FileReader reader, FileWriter writer) {
        super(reader, writer);
        mItems = new LinkedHashMap<>();
    }

    public void insert(Key key, Value item) throws JSONException, IOException, DuplicatedEntryException {
        put(key, item);
        write();
    }

    public void remove(Key key) throws IOException, JSONException {
        if(mItems.containsKey(key)) {
            mItems.remove(key);
            write();
        }
    }

    public void replace(Key oldKey, Key newKey, Value newItem) throws JSONException, IOException, DuplicatedEntryException {
        if(mItems.containsKey(oldKey)) {
            if (!oldKey.equals(newKey) && mItems.containsKey(newKey)) {
                throw new DuplicatedEntryException(newKey.toString());
            }

            mItems.remove(oldKey);
            mItems.put(newKey, newItem);
            write();
        }
    }

    public Value getItem(Key key) {
        return mItems.containsKey(key) ? mItems.get(key) : null;
    }

    public Map<Key, Value> getItems() {
        return new LinkedHashMap<>(mItems);
    }

    void put(Key key, Value value) throws DuplicatedEntryException {
        if(mItems.containsKey(key)) {
            throw new DuplicatedEntryException(key.toString());
        } else {
            mItems.put(key, value);
        }
    }

    final Map<Key, Value> mItems;
}
