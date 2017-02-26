package com.f1x.mtcdtools.storage;

import com.f1x.mtcdtools.named.objects.NamedObjectId;
import com.f1x.mtcdtools.storage.exceptions.DuplicatedEntryException;
import com.f1x.mtcdtools.storage.exceptions.EntryCreationFailed;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by COMPUTER on 2017-02-26.
 */

public class AutorunStorage extends FileStorage {
    public AutorunStorage(FileReader reader, FileWriter writer) {
        super(reader, writer);
        mItems = new ArrayList<>();
    }

    @Override
    public void read() throws JSONException, IOException, DuplicatedEntryException, EntryCreationFailed {
        JSONArray namedObjectIdsArray = read(STORAGE_FILE_NAME, ROOT_ARRAY_NAME);

        for (int i = 0; i < namedObjectIdsArray.length(); ++i) {
            mItems.add(new NamedObjectId(namedObjectIdsArray.getString(i)));
        }
    }

    @Override
    public void write() throws JSONException, IOException {
        JSONArray namedObjectIdsArray = new JSONArray();

        for(NamedObjectId item : mItems) {
            namedObjectIdsArray.put(item.toString());
        }

        write(STORAGE_FILE_NAME, ROOT_ARRAY_NAME, namedObjectIdsArray);
    }

    public void insert(NamedObjectId id) throws IOException, JSONException {
        mItems.add(id);
        write();
    }

    public void remove(NamedObjectId id) throws IOException, JSONException {
        if(mItems.contains(id)) {
            mItems.remove(id);
            write();
        }
    }

    public List<NamedObjectId> getItems() {
        return new ArrayList<>(mItems);
    }

    private List<NamedObjectId> mItems;
    public static final String STORAGE_FILE_NAME = "autorun.json";
    public static final String ROOT_ARRAY_NAME = "autorun";
}
