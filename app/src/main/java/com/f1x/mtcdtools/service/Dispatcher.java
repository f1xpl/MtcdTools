package com.f1x.mtcdtools.service;

import android.content.Context;
import android.content.Intent;

import com.f1x.mtcdtools.ActionsList;
import com.f1x.mtcdtools.actions.Action;
import com.f1x.mtcdtools.actions.BroadcastIntentAction;
import com.f1x.mtcdtools.actions.KeyAction;
import com.f1x.mtcdtools.actions.LaunchAction;
import com.f1x.mtcdtools.actions.StartActivityAction;
import com.f1x.mtcdtools.activities.SelectActionActivity;
import com.f1x.mtcdtools.input.KeysSequenceBinding;
import com.f1x.mtcdtools.input.KeysSequenceListener;
import com.f1x.mtcdtools.storage.KeysSequenceBindingsStorage;
import com.f1x.mtcdtools.storage.NamedObject;
import com.f1x.mtcdtools.storage.NamedObjectsStorage;

import java.util.List;

/**
 * Created by COMPUTER on 2017-01-28.
 */

class Dispatcher implements KeysSequenceListener {
    public Dispatcher(Context context, NamedObjectsStorage namedObjectsStorage, KeysSequenceBindingsStorage keysSequenceBindingsStorage) {
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
                if (namedObject.getObjectType().equals(ActionsList.OBJECT_TYPE)) {
                    ActionsList actionsList = (ActionsList) namedObject;

                    Intent intent = new Intent(mContext, SelectActionActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS | Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_FROM_BACKGROUND);
                    intent.putExtra(SelectActionActivity.ACTIONS_LIST_NAME_PARAMETER, actionsList.getName());
                    mContext.startActivity(intent);
                } else {
                    Action action = (Action) namedObject;
                    action.evaluate(mContext);
                }
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
