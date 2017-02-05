package com.f1x.mtcdtools.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by COMPUTER on 2017-02-05.
 */

public class NamesArrayAdapter extends ArrayAdapter<String> {
    public NamesArrayAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_1);
    }

    public void reset(List<String> names) {
        clear();
        addAll(names);
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

    public List<String> getItems() {
        List<String> items = new ArrayList<>();

        for(int i = 0; i < getCount(); ++i) {
            items.add(getItem(i));
        }

        return items;
    }
}
