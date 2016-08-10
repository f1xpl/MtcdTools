package com.f1x.mtcdtools;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.f1x.mtcdtools.keys.input.KeyInputType;

/**
 * Created by COMPUTER on 2016-08-08.
 */
public class KeyInputTypeArrayAdapter extends ArrayAdapter<String> {
    public KeyInputTypeArrayAdapter(Context context) {
        super(context, R.layout.key_input_row);

        for(KeyInputType keyInputType : KeyInputType.values()) {
            add(KeyInputType.toString(keyInputType));
        }
    }
}
