package com.f1x.mtcdtools.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by COMPUTER on 2017-01-28.
 */

public class KeysSequenceArrayAdapter extends ArrayAdapter<Integer> {
    public KeysSequenceArrayAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_1);
    }

    public void reset(List<Integer> keysSequence) {
        clear();

        for(int i = 0; i < keysSequence.size(); ++i) {
            add(keysSequence.get(i));
        }
    }
}
