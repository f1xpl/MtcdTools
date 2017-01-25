package com.f1x.mtcdtools.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.f1x.mtcdtools.actions.Action;

import java.util.Map;

/**
 * Created by COMPUTER on 2017-01-25.
 */

public class ActionsArrayAdapter extends ArrayAdapter<String>  {
    public ActionsArrayAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_1);
    }

    public void reset(Map<String, Action> actions) {
        clear();

        for(Map.Entry<String, Action> entry : actions.entrySet()) {
            add(entry.getKey());
        }
    }
}
