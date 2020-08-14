package com.microntek.f1x.mtcdtools.adapters;

import android.content.Context;
import com.microntek.f1x.mtcdtools.named.NamedObjectId;
import android.widget.ArrayAdapter;

import com.microntek.f1x.mtcdtools.named.NamedObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by f1x on 2017-02-05.
 */

public class NamedObjectIdsArrayAdapter extends ArrayAdapter<NamedObjectId> {
    public NamedObjectIdsArrayAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_1);
        mObjectTypeFilters = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
    }

    public NamedObjectIdsArrayAdapter(Context context, int layoutResId) {
        super(context, layoutResId);
        mObjectTypeFilters = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
    }

    public void reset(Map<NamedObjectId, NamedObject> items) {
        clear();

        for(Map.Entry<NamedObjectId, NamedObject> entry : items.entrySet()) {
            if(mObjectTypeFilters.isEmpty() || mObjectTypeFilters.contains(entry.getValue().getObjectType())) {
                add(entry.getKey());
            }
        }
    }

    public void reset(List<NamedObjectId> items) {
        clear();
        addAll(items);
    }

    public void setObjectTypeFilters(Set<String> objectTypeFilters) {
        mObjectTypeFilters.clear();
        mObjectTypeFilters.addAll(objectTypeFilters);
    }

    public List<NamedObjectId> getItems() {
        List<NamedObjectId> items = new ArrayList<>();

        for(int i = 0; i < getCount(); ++i) {
            items.add(getItem(i));
        }

        return items;
    }

    public void removeAt(int position) {
        if(position != -1 && position < getCount()) {
            List<NamedObjectId> items = new ArrayList<>();

            for(int i = 0; i < getCount(); ++i) {
                items.add(getItem(i));
            }

            items.remove(position);
            clear();
            addAll(items);
        }
    }

    private Set<String> mObjectTypeFilters;
}
