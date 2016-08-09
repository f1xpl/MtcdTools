package com.f1x.mtcdtools;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.f1x.mtcdtools.keys.input.KeyInputType;

/**
 * Created by COMPUTER on 2016-08-08.
 */
public class KeyInputTypeArrayAdapter extends ArrayAdapter<String> {
    public KeyInputTypeArrayAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_1);

        for(KeyInputType keyInputType : KeyInputType.values()) {
            add(KeyInputType.toString(keyInputType));
        }
    }
}
