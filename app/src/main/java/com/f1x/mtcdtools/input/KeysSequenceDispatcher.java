package com.f1x.mtcdtools.input;

import android.content.Context;

import com.f1x.mtcdtools.configuration.Configuration;
import com.f1x.mtcdtools.input.KeysSequenceBinding;
import com.f1x.mtcdtools.input.KeysSequenceListener;
import com.f1x.mtcdtools.named.objects.NamedObjectDispatcher;
import com.f1x.mtcdtools.storage.KeysSequenceBindingsStorage;
import com.f1x.mtcdtools.named.objects.NamedObject;
import com.f1x.mtcdtools.storage.NamedObjectsStorage;

import java.util.List;

/**
 * Created by COMPUTER on 2017-01-28.
 */

public class KeysSequenceDispatcher extends NamedObjectDispatcher implements KeysSequenceListener {
    public KeysSequenceDispatcher(Context context, Configuration configuration, NamedObjectsStorage namedObjectsStorage, KeysSequenceBindingsStorage keysSequenceBindingsStorage) {
        super(namedObjectsStorage, configuration);

        mContext = context;
        mKeysSequenceBindingsStorage = keysSequenceBindingsStorage;
    }

    @Override
    public void handleKeysSequence(List<Integer> keysSequence) {
        KeysSequenceBinding keysSequenceBinding = mKeysSequenceBindingsStorage.getItem(keysSequence);

        if(keysSequenceBinding != null) {
            dispatch(keysSequenceBinding.getTargetName(), mContext);
        }
    }

    @Override
    public void handleSingleKey(int keyCode) {

    }

    private final Context mContext;
    private final KeysSequenceBindingsStorage mKeysSequenceBindingsStorage;
}
