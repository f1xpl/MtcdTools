package android.microntek.f1x.mtcdtools.service.dispatching;

import android.content.Context;

import android.microntek.f1x.mtcdtools.service.input.KeysSequenceBinding;
import android.microntek.f1x.mtcdtools.service.input.KeysSequenceListener;
import android.microntek.f1x.mtcdtools.service.storage.KeysSequenceBindingsStorage;

import java.util.List;

/**
 * Created by f1x on 2017-01-28.
 */

public class KeysSequenceDispatcher implements KeysSequenceListener {
    public KeysSequenceDispatcher(Context context, KeysSequenceBindingsStorage keysSequenceBindingsStorage, NamedObjectDispatcher namedObjectDispatcher, DispatchingIndicationPlayer dispatchingIndicationPlayer) {
        mContext = context;
        mKeysSequenceBindingsStorage = keysSequenceBindingsStorage;
        mNamedObjectDispatcher = namedObjectDispatcher;
        mDispatchingIndicationPlayer = dispatchingIndicationPlayer;
    }

    @Override
    public void handleKeysSequence(List<Integer> keysSequence) {
        KeysSequenceBinding keysSequenceBinding = mKeysSequenceBindingsStorage.getItem(keysSequence);

        if(keysSequenceBinding != null) {
            mNamedObjectDispatcher.dispatch(keysSequenceBinding.getTargetId(), mContext);

            if(keysSequenceBinding.playIndication()) {
                mDispatchingIndicationPlayer.play();
            }
        }
    }

    @Override
    public void handleSingleKey(int keyCode) {

    }

    private final Context mContext;
    private final KeysSequenceBindingsStorage mKeysSequenceBindingsStorage;
    private final NamedObjectDispatcher mNamedObjectDispatcher;
    private final DispatchingIndicationPlayer mDispatchingIndicationPlayer;
}
