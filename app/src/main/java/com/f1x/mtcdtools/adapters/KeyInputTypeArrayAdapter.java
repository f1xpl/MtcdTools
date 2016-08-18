package com.f1x.mtcdtools.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.f1x.mtcdtools.R;
import com.f1x.mtcdtools.input.KeyInputType;

/**
 * Created by COMPUTER on 2016-08-08.
 */
public class KeyInputTypeArrayAdapter extends ArrayAdapter<String> {
    public KeyInputTypeArrayAdapter(Context context) {
        super(context, R.layout.key_input_type_row);

        for(KeyInputType keyInputType : KeyInputType.values()) {
            add(KeyInputType.toString(keyInputType));
        }
    }
}
