package com.f1x.mtcdtools.adapters;

import android.content.Context;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by COMPUTER on 2017-01-23.
 */

public class KeyCodesArrayAdapter extends ArrayAdapter<String> {
    public KeyCodesArrayAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_1);

        mKeyCodesMap.put("KEYCODE_MEDIA_FAST_FORWARD", KeyEvent.KEYCODE_MEDIA_FAST_FORWARD);
        mKeyCodesMap.put("KEYCODE_MEDIA_NEXT", KeyEvent.KEYCODE_MEDIA_NEXT);
        mKeyCodesMap.put("KEYCODE_MEDIA_PAUSE", KeyEvent.KEYCODE_MEDIA_PAUSE);
        mKeyCodesMap.put("KEYCODE_MEDIA_PLAY", KeyEvent.KEYCODE_MEDIA_PLAY);
        mKeyCodesMap.put("KEYCODE_MEDIA_PLAY_PAUSE", KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE);
        mKeyCodesMap.put("KEYCODE_MEDIA_PREVIOUS", KeyEvent.KEYCODE_MEDIA_PREVIOUS);
        mKeyCodesMap.put("KEYCODE_MEDIA_REWIND", KeyEvent.KEYCODE_MEDIA_REWIND);
        mKeyCodesMap.put("KEYCODE_MEDIA_STOP", KeyEvent.KEYCODE_MEDIA_STOP);

        for(Map.Entry<String, Integer> entry : mKeyCodesMap.entrySet()) {
            add(entry.getKey());
        }
    }

    int getValue(String key) {
        if(mKeyCodesMap.containsKey(key)) {
            return mKeyCodesMap.get(key);
        }

        return INVALID_KEY;
    }

    private static final Map<String, Integer> mKeyCodesMap = new HashMap<>();
    public static int INVALID_KEY = -1;
}
