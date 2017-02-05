package com.f1x.mtcdtools.named.objects;

import android.content.Context;
import android.os.AsyncTask;

import com.f1x.mtcdtools.named.objects.actions.Action;
import com.f1x.mtcdtools.storage.NamedObjectsStorage;

import java.util.List;

/**
 * Created by COMPUTER on 2017-02-05.
 */

public class ActionsSequenceDispatchTask extends AsyncTask<Integer, String, Void> {
    ActionsSequenceDispatchTask(List<String> actionNames, Context context, NamedObjectsStorage namedObjectsStorage) {
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
        String actionName = progress[0];

        if(actionName != null) {
            Action action = (Action)mNamedObjectsStorage.getItem(actionName);

            if (action != null) {
                action.evaluate(mContext);
            }
        }
    }

    private final List<String> mActionNames;
    private final Context mContext;
    private final NamedObjectsStorage mNamedObjectsStorage;
}
