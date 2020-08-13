package com.microntek.f1x.mtcdtools.service.dispatching;

import android.content.Context;
import android.content.Intent;
import com.microntek.f1x.mtcdtools.service.dispatching.activities.ActionsListDispatchActivity;
import android.os.AsyncTask;

import com.microntek.f1x.mtcdtools.named.objects.containers.ActionsList;
import com.microntek.f1x.mtcdtools.named.objects.containers.ActionsSequence;
import com.microntek.f1x.mtcdtools.named.objects.containers.ModeList;
import com.microntek.f1x.mtcdtools.named.NamedObject;
import com.microntek.f1x.mtcdtools.named.NamedObjectId;
import com.microntek.f1x.mtcdtools.named.objects.actions.Action;
import com.microntek.f1x.mtcdtools.service.storage.NamedObjectsStorage;

import java.util.List;

/**
 * Created by f1x on 2017-02-05.
 */

public class NamedObjectDispatcher {
    public NamedObjectDispatcher(NamedObjectsStorage namedObjectsStorage) {
        mNamedObjectsStorage = namedObjectsStorage;
    }

    public void dispatch(List<NamedObjectId> namedObjectIds, Context context) {
        tryCancelDispatchingTask();
        executeDispatchingTask(context, namedObjectIds.toArray(new NamedObjectId[namedObjectIds.size()]));
    }

    public void dispatch(NamedObjectId namedObjectId, Context context) {
        tryCancelDispatchingTask();

        NamedObject namedObject = mNamedObjectsStorage.getItem(namedObjectId);

        if(namedObject == null) {
            return;
        }

        final String objectType = namedObject.getObjectType();

        switch(objectType) {
            case ActionsList.OBJECT_TYPE:
                this.dispatchActionsList((ActionsList)namedObject, context);
                break;

            case ActionsSequence.OBJECT_TYPE:
                ActionsSequence actionsSequence = (ActionsSequence)namedObject;
                executeDispatchingTask(context, actionsSequence.getId());
                break;

            case ModeList.OBJECT_TYPE:
                ModeList modeList = (ModeList)namedObject;
                dispatch(modeList.evaluate(), context);
                break;

            default:
                Action action = (Action)namedObject;
                action.evaluate(context);
        }
    }

    private void dispatchActionsList(ActionsList actionsList, Context context) {
        Intent intent = new Intent(context, ActionsListDispatchActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS | Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_FROM_BACKGROUND);
        intent.putExtra(ActionsListDispatchActivity.ACTIONS_LIST_ID_PARAMETER, actionsList.getId());
        context.startActivity(intent);
    }

    private void executeDispatchingTask(Context context, NamedObjectId... ids) {
        mNamedObjectsDispatchingTask = new NamedObjectsDispatchTask(mNamedObjectsStorage, context);
        mNamedObjectsDispatchingTask.execute(ids);
    }

    private void tryCancelDispatchingTask() {
        if(mNamedObjectsDispatchingTask != null && mNamedObjectsDispatchingTask.getStatus() == AsyncTask.Status.RUNNING) {
            mNamedObjectsDispatchingTask.cancel(true);
        }
    }

    private final NamedObjectsStorage mNamedObjectsStorage;
    private NamedObjectsDispatchTask mNamedObjectsDispatchingTask;
}
