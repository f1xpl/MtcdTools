package com.f1x.mtcdtools.named.objects;

import com.f1x.mtcdtools.ListIndexer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by COMPUTER on 2017-02-23.
 */

public class ModeList extends NamedObjectsContainer {
    public ModeList(JSONObject json) throws JSONException {
        super(json);
        mListIndexer = new ListIndexer(mActionIds.size());
    }

    public ModeList(NamedObjectId id, List<NamedObjectId> actionIds) {
        super(id, OBJECT_TYPE, actionIds);
        mListIndexer = new ListIndexer(mActionIds.size());
    }

    public NamedObjectId evaluate() {
        try {
            int index = mListIndexer.current();
            mListIndexer.down();

            return mActionIds.get(index);
        } catch(IndexOutOfBoundsException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void removeDependency(NamedObjectId id) {
        super.removeDependency(id);
        mListIndexer.reset(mActionIds.size());
    }

    private final ListIndexer mListIndexer;

    static public final String OBJECT_TYPE = "ModeList";
}
