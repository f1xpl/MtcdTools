package com.f1x.mtcdtools.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by f1x on 2017-02-05.
 */

public class NamesArrayAdapter extends ArrayAdapter<String> {
    public NamesArrayAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_1);
    }

    public List<String> getItems() {
        List<String> items = new ArrayList<>();

        for(int i = 0; i < getCount(); ++i) {
            items.add(getItem(i));
        }

        return items;
    }

    public void removeAt(int position) {
        if(position != -1 && position < getCount()) {
            List<String> items = new ArrayList<>();

            for(int i = 0; i < getCount(); ++i) {
                items.add(getItem(i));
            }

            items.remove(position);
            clear();
            addAll(items);
        }
    }
}
