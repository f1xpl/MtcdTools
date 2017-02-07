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

public class ActionsDispatchTask extends AsyncTask<Action, Action, Void> {
    ActionsDispatchTask(int actionExecutionDelay, Context context) {
        mActionExecutionDelay = actionExecutionDelay;
        mContext = context;
    }

    @Override
    protected Void doInBackground(Action... actions) {
        for(Action action : actions) {
            try {
                publishProgress(action);
                Thread.sleep(mActionExecutionDelay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    protected void onProgressUpdate(Action... progress) {
        progress[0].evaluate(mContext);
    }

    private final int mActionExecutionDelay;
    private final Context mContext;
}
