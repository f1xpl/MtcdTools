package com.f1x.mtcdtools.input;

import android.content.Context;

import com.f1x.mtcdtools.configuration.Configuration;
import com.f1x.mtcdtools.named.objects.NamedObjectDispatcher;
import com.f1x.mtcdtools.storage.KeysSequenceBindingsStorage;
import com.f1x.mtcdtools.storage.NamedObjectsStorage;

import java.util.List;

/**
 * Created by COMPUTER on 2017-01-28.
 */

public class KeysSequenceDispatcher implements KeysSequenceListener {
    public KeysSequenceDispatcher(Context context, KeysSequenceBindingsStorage keysSequenceBindingsStorage, NamedObjectDispatcher namedObjectDispatcher) {
        mContext = context;
        mKeysSequenceBindingsStorage = keysSequenceBindingsStorage;
        mNamedObjectDispatcher = namedObjectDispatcher;
    }

    @Override
    public void handleKeysSequence(List<Integer> keysSequence) {
        KeysSequenceBinding keysSequenceBinding = mKeysSequenceBindingsStorage.getItem(keysSequence);

        if(keysSequenceBinding != null) {
            mNamedObjectDispatcher.dispatch(keysSequenceBinding.getTargetName(), mContext);
        }
    }

    @Override
    public void handleSingleKey(int keyCode) {

    }

    private final Context mContext;
    private final KeysSequenceBindingsStorage mKeysSequenceBindingsStorage;
    private final NamedObjectDispatcher mNamedObjectDispatcher;
}
