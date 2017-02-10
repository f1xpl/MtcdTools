package com.f1x.mtcdtools.named.objects;

import android.content.Context;
import android.os.AsyncTask;

import com.f1x.mtcdtools.named.objects.actions.Action;
import com.f1x.mtcdtools.storage.NamedObjectsStorage;

import java.util.List;

/**
 * Created by f1x on 2017-02-05.
 */

class NamedObjectsDispatchTask extends AsyncTask<String, Action, Void> {
    NamedObjectsDispatchTask(NamedObjectsStorage namedObjectsStorage, Context context) {
        mNamedObjectsStorage = namedObjectsStorage;
        mContext = context;
    }

    @Override
    protected Void doInBackground(String... names) {
        for(String name : names) {
            try {
                NamedObject namedObject = mNamedObjectsStorage.getItem(name);

                if(namedObject == null) {
                    continue;
                } else if(Action.isAction(namedObject.getObjectType())) {
                    dispatchAction((Action)namedObject, 0);
                } else if(namedObject.getObjectType().equals(ActionsSequence.OBJECT_TYPE)) {
                    dispatchActionsSequence((ActionsSequence)namedObject);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    private void dispatchAction(Action action, int delay) throws InterruptedException {
        Thread.sleep(delay);
        publishProgress(action);
    }

    private void dispatchActionsSequence(ActionsSequence actionsSequence) throws InterruptedException {
        List<String> actionNames = actionsSequence.getActionsNames();

        for(int i = 0; i < actionNames.size(); ++i) {
            NamedObject namedObject = mNamedObjectsStorage.getItem(actionNames.get(i));

            if(Action.isAction(namedObject.getObjectType())) {
                dispatchAction((Action)namedObject, actionsSequence.getDelayForAction(i));
            }
        }
    }

    protected void onProgressUpdate(Action... progress) {
        progress[0].evaluate(mContext);
    }

    private final NamedObjectsStorage mNamedObjectsStorage;
    private final Context mContext;
}
