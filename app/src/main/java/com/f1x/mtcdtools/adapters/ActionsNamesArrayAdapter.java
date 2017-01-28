package com.f1x.mtcdtools.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.f1x.mtcdtools.actions.Action;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by COMPUTER on 2017-01-25.
 */

public class ActionsNamesArrayAdapter extends ArrayAdapter<String>  {
    public ActionsNamesArrayAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_1);
    }

    public void reset(Set<String> actionsList) {
        clear();

        for(String actionName : actionsList) {
            add(actionName);
        }
    }

    public boolean containsAction(String actionName) {
        for(int i = 0; i < getCount(); ++i) {
            String name = getItem(i);

            if(name != null && name.equalsIgnoreCase(actionName)) {
                return true;
            }
        }

        return false;
    }
}
