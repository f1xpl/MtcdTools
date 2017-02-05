package com.f1x.mtcdtools;

import android.content.Context;

import com.f1x.mtcdtools.input.KeysSequenceBinding;
import com.f1x.mtcdtools.input.KeysSequenceListener;
import com.f1x.mtcdtools.storage.KeysSequenceBindingsStorage;
import com.f1x.mtcdtools.storage.NamedObject;
import com.f1x.mtcdtools.storage.NamedObjectsStorage;

import java.util.List;

/**
 * Created by COMPUTER on 2017-01-28.
 */

public class KeysSequenceDispatcher extends NamedObjectDispatcher implements KeysSequenceListener {
    public KeysSequenceDispatcher(Context context, NamedObjectsStorage namedObjectsStorage, KeysSequenceBindingsStorage keysSequenceBindingsStorage) {
        mContext = context;
        mNamedObjectsStorage = namedObjectsStorage;
        mKeysSequenceBindingsStorage = keysSequenceBindingsStorage;
    }

    @Override
    public void handleKeysSequence(List<Integer> keysSequence) {
        KeysSequenceBinding keysSequenceBinding = mKeysSequenceBindingsStorage.getItem(keysSequence);

        if(keysSequenceBinding != null) {
            NamedObject namedObject = mNamedObjectsStorage.getItem(keysSequenceBinding.getTargetName());

            if (namedObject != null) {
                dispatch(namedObject, mContext);
            }
        }
    }

    @Override
    public void handleSingleKey(int keyCode) {

    }

    private final Context mContext;
    private final NamedObjectsStorage mNamedObjectsStorage;
    private final KeysSequenceBindingsStorage mKeysSequenceBindingsStorage;
}
