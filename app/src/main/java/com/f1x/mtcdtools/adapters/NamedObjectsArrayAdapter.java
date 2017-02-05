package com.f1x.mtcdtools.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.f1x.mtcdtools.named.objects.NamedObject;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by COMPUTER on 2017-01-25.
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

    public boolean containsItem(String actionName) {
        for(int i = 0; i < getCount(); ++i) {
            String name = getItem(i);

            if(name != null && name.equalsIgnoreCase(actionName)) {
                return true;
            }
        }

        return false;
    }

    public Set<String> getItems() {
        Set<String> items = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);

        for(int i = 0; i < getCount(); ++i) {
            items.add(getItem(i));
        }

        return items;
    }

    public void setObjectTypeFilters(Set<String> objectTypeFilters) {
        mObjectTypeFilters = objectTypeFilters;
    }

    Set<String> mObjectTypeFilters;
}
