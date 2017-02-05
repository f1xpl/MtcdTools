package com.f1x.mtcdtools.storage;

import com.f1x.mtcdtools.named.objects.NamedObject;
import com.f1x.mtcdtools.named.objects.NamedObjectsFactory;
import com.f1x.mtcdtools.storage.exceptions.DuplicatedEntryException;
import com.f1x.mtcdtools.storage.exceptions.EntryCreationFailed;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by COMPUTER on 2017-02-05.
 */

public class NamedObjectsStorage extends Storage<String, NamedObject> {
    public NamedObjectsStorage(FileReader reader, FileWriter writer) {
        super(reader, writer);
    }

    @Override
    public void read() throws JSONException, IOException, DuplicatedEntryException, EntryCreationFailed {
        JSONArray namedObjectsArray = read(STORAGE_FILE_NAME, ROOT_ARRAY_NAME);

        for (int i = 0; i < namedObjectsArray.length(); ++i) {
            JSONObject namedObjectJson = namedObjectsArray.getJSONObject(i);
            NamedObject namedObject = NamedObjectsFactory.createNamedObject(namedObjectJson);

            if(namedObject == null) {
                throw new EntryCreationFailed(namedObjectJson.getString(NamedObject.NAME_PROPERTY));
            } else {
                put(namedObject.getName(), namedObject);
            }
        }
    }

    @Override
    public void write() throws JSONException, IOException {
        JSONArray namedObjectsArray = new JSONArray();

        for(Map.Entry<String, NamedObject> entry : mItems.entrySet()) {
            namedObjectsArray.put(entry.getValue().toJson());
        }

        write(STORAGE_FILE_NAME, ROOT_ARRAY_NAME, namedObjectsArray);
    }

    @Override
    protected Map<String, NamedObject> createContainer() {
        return new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    }

    @Override
    public void remove(String name) throws IOException, JSONException {
        removeDependency(name);
        super.remove(name);
    }

    @Override
    public void replace(String oldName, String newName, NamedObject newItem) throws JSONException, IOException, DuplicatedEntryException {
        replaceDependency(oldName, newName);
        super.replace(oldName, newName, newItem);
    }

    private void removeDependency(String dependencyName) {
        for(Map.Entry<String, NamedObject> entry : mItems.entrySet()) {
            entry.getValue().removeDependency(dependencyName);
        }
    }

    private void replaceDependency(String oldName, String newName) throws IOException, JSONException {
        for(Map.Entry<String, NamedObject> entry : mItems.entrySet()) {
            entry.getValue().replaceDependency(oldName, newName);
        }
    }

    public static final String STORAGE_FILE_NAME = "namedObjects.json";
    public static final String ROOT_ARRAY_NAME = "namedObjects";
}
