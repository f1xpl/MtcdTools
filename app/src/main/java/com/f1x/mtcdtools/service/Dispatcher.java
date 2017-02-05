package com.f1x.mtcdtools.service;

import android.content.Context;
import android.content.Intent;

import com.f1x.mtcdtools.ActionsList;
import com.f1x.mtcdtools.actions.Action;
import com.f1x.mtcdtools.activities.SelectActionActivity;
import com.f1x.mtcdtools.input.KeysSequenceBinding;
import com.f1x.mtcdtools.input.KeysSequenceListener;
import com.f1x.mtcdtools.storage.ActionsListsStorage;
import com.f1x.mtcdtools.storage.ActionsStorage;
import com.f1x.mtcdtools.storage.KeysSequenceBindingsStorage;

import java.util.List;

/**
 * Created by COMPUTER on 2017-01-28.
 */

class Dispatcher implements KeysSequenceListener {
    public Dispatcher(Context context, ActionsStorage actionsStorage, ActionsListsStorage actionsListsStorage, KeysSequenceBindingsStorage keysSequenceBindingsStorage) {
        mContext = context;
        mActionsStorage = actionsStorage;
        mActionsListsStorage = actionsListsStorage;
        mKeysSequenceBindingsStorage = keysSequenceBindingsStorage;
    }

    @Override
    public void handleKeysSequence(List<Integer> keysSequence) {
        KeysSequenceBinding keysSequenceBinding = mKeysSequenceBindingsStorage.getItem(keysSequence);

        if(keysSequenceBinding == null) {
            return;
        }

        if(keysSequenceBinding.getTargetType().equalsIgnoreCase(KeysSequenceBinding.TARGET_TYPE_ACTION)) {
            Action action = mActionsStorage.getItem(keysSequenceBinding.getTargetName());

            if(action != null) {
                action.evaluate(mContext);
            }
        } else if(keysSequenceBinding.getTargetType().equalsIgnoreCase(KeysSequenceBinding.TARGET_TYPE_ACTIONS_LIST)) {
            ActionsList actionsList = mActionsListsStorage.getItem(keysSequenceBinding.getTargetName());

            if(actionsList != null) {
                Intent intent = new Intent(mContext, SelectActionActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS | Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_FROM_BACKGROUND);
                intent.putExtra(SelectActionActivity.ACTIONS_LIST_NAME_PARAMETER, actionsList.getName());
                mContext.startActivity(intent);
            }
        }
    }

    @Override
    public void handleSingleKey(int keyCode) {

    }

    private final Context mContext;
    private final ActionsStorage mActionsStorage;
    private final ActionsListsStorage mActionsListsStorage;
    private final KeysSequenceBindingsStorage mKeysSequenceBindingsStorage;
}
