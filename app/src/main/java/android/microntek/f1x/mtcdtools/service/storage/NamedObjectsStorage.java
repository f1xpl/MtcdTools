package android.microntek.f1x.mtcdtools.service.storage;

import android.microntek.f1x.mtcdtools.named.NamedObject;
import android.microntek.f1x.mtcdtools.named.NamedObjectId;
import android.microntek.f1x.mtcdtools.named.NamedObjectsFactory;
import android.microntek.f1x.mtcdtools.named.objects.actions.Action;
import android.microntek.f1x.mtcdtools.named.objects.actions.StartIntentAction;
import android.microntek.f1x.mtcdtools.service.storage.exceptions.DuplicatedEntryException;
import android.microntek.f1x.mtcdtools.service.storage.exceptions.EntryCreationFailed;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

/**
 * Created by f1x on 2017-02-05.
 */

public class NamedObjectsStorage extends UniqueObjectsStorage<NamedObjectId, NamedObject> {
    public NamedObjectsStorage(FileReader reader, FileWriter writer) {
        super(reader, writer);
    }

    @Override
    public void read() throws JSONException, IOException, DuplicatedEntryException, EntryCreationFailed {
        JSONArray namedObjectsArray = read(STORAGE_FILE_NAME, ROOT_ARRAY_NAME);

        for (int i = 0; i < namedObjectsArray.length(); ++i) {
            JSONObject namedObjectJson = namedObjectsArray.getJSONObject(i);
            backwardCompatibilityWith1_5_ChangeActionType(namedObjectJson);
            NamedObject namedObject = NamedObjectsFactory.createNamedObject(namedObjectJson);

            if(namedObject == null) {
                throw new EntryCreationFailed(namedObjectJson.getString(NamedObject.NAME_PROPERTY));
            } else {
                put(namedObject.getId(), namedObject);
            }
        }
    }

    @Override
    public void write() throws JSONException, IOException {
        JSONArray namedObjectsArray = new JSONArray();

        for(Map.Entry<NamedObjectId, NamedObject> entry : mItems.entrySet()) {
            namedObjectsArray.put(entry.getValue().toJson());
        }

        write(STORAGE_FILE_NAME, ROOT_ARRAY_NAME, namedObjectsArray);
    }

    @Override
    public void remove(NamedObjectId id) throws IOException, JSONException {
        removeDependency(id);
        super.remove(id);
    }

    @Override
    public void replace(NamedObjectId oldId, NamedObjectId newId, NamedObject newItem) throws JSONException, IOException, DuplicatedEntryException {
        replaceDependency(oldId, newId);
        super.replace(oldId, newId, newItem);
    }

    private void removeDependency(NamedObjectId dependencyId) {
        for(Map.Entry<NamedObjectId, NamedObject> entry : mItems.entrySet()) {
            entry.getValue().removeDependency(dependencyId);
        }
    }

    private void replaceDependency(NamedObjectId oldId, NamedObjectId newId) {
        for(Map.Entry<NamedObjectId, NamedObject> entry : mItems.entrySet()) {
            entry.getValue().replaceDependency(oldId, newId);
        }
    }

    private void backwardCompatibilityWith1_5_ChangeActionType(JSONObject namedObjectJson) {
        try {
            if(namedObjectJson.getString(Action.OBJECT_TYPE_PROPERTY).equals("StartActivityAction")) {
                namedObjectJson.remove(Action.OBJECT_TYPE_PROPERTY);
                namedObjectJson.put(Action.OBJECT_TYPE_PROPERTY, StartIntentAction.OBJECT_TYPE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static final String STORAGE_FILE_NAME = "namedObjects.json";
    public static final String ROOT_ARRAY_NAME = "namedObjects";
}
