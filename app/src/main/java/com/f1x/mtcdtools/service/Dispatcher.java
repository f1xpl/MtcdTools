package com.f1x.mtcdtools.service;

import android.content.Context;

import com.f1x.mtcdtools.ActionsList;
import com.f1x.mtcdtools.actions.Action;
import com.f1x.mtcdtools.input.KeysSequenceBinding;
import com.f1x.mtcdtools.input.KeysSequenceListener;
import com.f1x.mtcdtools.storage.ActionsListStorage;
import com.f1x.mtcdtools.storage.ActionsStorage;
import com.f1x.mtcdtools.storage.KeysSequenceBindingsStorage;

import java.util.List;

/**
 * Created by COMPUTER on 2017-01-28.
 */

public class Dispatcher implements KeysSequenceListener {
    public Dispatcher(Context context, ActionsStorage actionsStorage, ActionsListStorage actionsListStorage, KeysSequenceBindingsStorage keysSequenceBindingsStorage) {
        mContext = context;
        mActionsStorage = actionsStorage;
        mActionsListStorage = actionsListStorage;
        mKeysSequenceBindingsStorage = keysSequenceBindingsStorage;
    }

    @Override
    public void handleKeysSequence(List<Integer> keysSequence) {
        KeysSequenceBinding keysSequenceBinding = mKeysSequenceBindingsStorage.getKeysSequenceBinding(keysSequence);

        if(keysSequenceBinding == null) {
            return;
        }

        if(keysSequenceBinding.getTargetType().equalsIgnoreCase(KeysSequenceBinding.TARGET_TYPE_ACTION)) {
            Action action = mActionsStorage.getAction(keysSequenceBinding.getTargetName());

            if(action != null) {
                action.evaluate(mContext);
            }
        } else if(keysSequenceBinding.getTargetType().equalsIgnoreCase(KeysSequenceBinding.TARGET_TYPE_ACTIONS_LIST)
                  && mActionsListStorage.hasActionsList(keysSequenceBinding.getTargetName())) {

        }
    }

    @Override
    public void handleSingleKey(int keyCode) {

    }

    private final Context mContext;
    private final ActionsStorage mActionsStorage;
    private final ActionsListStorage mActionsListStorage;
    private final KeysSequenceBindingsStorage mKeysSequenceBindingsStorage;
}
