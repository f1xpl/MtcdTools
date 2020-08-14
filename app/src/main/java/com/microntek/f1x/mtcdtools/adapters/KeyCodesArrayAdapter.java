package com.microntek.f1x.mtcdtools.adapters;

import android.content.Context;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by f1x on 2017-01-23.
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

        addAll(mKeyCodesMap.keySet());
    }

    public int getKeyCode(String key) {
        if(mKeyCodesMap.containsKey(key)) {
            return mKeyCodesMap.get(key);
        }

        return INVALID_KEY;
    }

    public int getPosition(int keyCode) {
        for(int i = 0; i < getCount(); ++i) {
            String keyCodeString = getItem(i);
            if(mKeyCodesMap.containsKey(keyCodeString) && mKeyCodesMap.get(keyCodeString) == keyCode) {
                return i;
            }
        }

        return INVALID_KEY;
    }

    private final Map<String, Integer> mKeyCodesMap = new HashMap<>();
    private static final int INVALID_KEY = -1;
}
