package com.f1x.mtcdtools.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.f1x.mtcdtools.named.objects.NamedObject;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by f1x on 2017-01-25.
 */

public class NamedObjectsArrayAdapter extends ArrayAdapter<String>  {
    public NamedObjectsArrayAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_1);
        mObjectTypeFilters = new HashSet<>();
    }

    public void reset(Map<String, NamedObject> namedObjects) {
        clear();

        for(Map.Entry<String, NamedObject> entry : namedObjects.entrySet()) {
            if(mObjectTypeFilters.isEmpty() || mObjectTypeFilters.contains(entry.getValue().getObjectType())) {
                add(entry.getKey());
            }
        }
    }

    public void setObjectTypeFilters(Set<String> objectTypeFilters) {
        mObjectTypeFilters = objectTypeFilters;
    }

    private Set<String> mObjectTypeFilters;
}
