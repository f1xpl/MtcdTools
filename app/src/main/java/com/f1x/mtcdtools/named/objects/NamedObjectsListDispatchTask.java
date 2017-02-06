package com.f1x.mtcdtools.named.objects;

import android.content.Context;
import android.os.AsyncTask;

import com.f1x.mtcdtools.named.objects.actions.Action;
import com.f1x.mtcdtools.named.objects.actions.BroadcastIntentAction;
import com.f1x.mtcdtools.named.objects.actions.KeyAction;
import com.f1x.mtcdtools.named.objects.actions.LaunchAction;
import com.f1x.mtcdtools.named.objects.actions.StartActivityAction;
import com.f1x.mtcdtools.storage.NamedObjectsStorage;

import java.util.List;

/**
 * Created by f1x on 2017-02-05.
 */

class NamedObjectsListDispatchTask extends AsyncTask<Integer, String, Void> {
    NamedObjectsListDispatchTask(List<String> actionNames, Context context, NamedObjectsStorage namedObjectsStorage) {
        mActionNames = actionNames;
        mContext = context;
        mNamedObjectsStorage = namedObjectsStorage;
    }

    @Override
    protected Void doInBackground(Integer... integers) {
        for(String actionName : mActionNames) {
            try {
                publishProgress(actionName);
                Thread.sleep(integers[0]);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    protected void onProgressUpdate(String... progress) {
        NamedObject namedObject = mNamedObjectsStorage.getItem(progress[0]);
        String namedObjectType = namedObject.getObjectType();

        if(namedObjectType.equals(KeyAction.OBJECT_TYPE) ||
                namedObjectType.equals(LaunchAction.OBJECT_TYPE) ||
                namedObjectType.equals(BroadcastIntentAction.OBJECT_TYPE) ||
                namedObjectType.equals(StartActivityAction.OBJECT_TYPE)) {

            Action action = (Action)namedObject;
            action.evaluate(mContext);
        }
    }

    private final List<String> mActionNames;
    private final Context mContext;
    private final NamedObjectsStorage mNamedObjectsStorage;
}
