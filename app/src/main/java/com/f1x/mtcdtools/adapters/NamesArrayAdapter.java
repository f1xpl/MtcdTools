package com.f1x.mtcdtools.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.Set;
import java.util.TreeSet;

/**
 * Created by COMPUTER on 2017-01-25.
 */

public class NamesArrayAdapter extends ArrayAdapter<String>  {
    public NamesArrayAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_1);
    }

    public void reset(Set<String> actionsList) {
        clear();

        for(String actionName : actionsList) {
            add(actionName);
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
}
