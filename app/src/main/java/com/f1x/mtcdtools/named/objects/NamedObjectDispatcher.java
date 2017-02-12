package com.f1x.mtcdtools.named.objects;

import android.content.Context;
import android.content.Intent;

import com.f1x.mtcdtools.activities.SelectNamedObjectActivity;
import com.f1x.mtcdtools.named.objects.actions.Action;
import com.f1x.mtcdtools.storage.NamedObjectsStorage;

import java.util.List;

/**
 * Created by f1x on 2017-02-05.
 */

public class NamedObjectDispatcher {
    public NamedObjectDispatcher(NamedObjectsStorage namedObjectsStorage) {
        mNamedObjectsStorage = namedObjectsStorage;
    }

    public void dispatchNamedObjects(List<NamedObjectId> namedObjectIds, Context context) {
        new NamedObjectsDispatchTask(mNamedObjectsStorage, context).execute(namedObjectIds.toArray(new NamedObjectId[namedObjectIds.size()]));
    }

    public void dispatch(NamedObjectId namedObjectId, Context context) {
        NamedObject namedObject = mNamedObjectsStorage.getItem(namedObjectId);

        if(namedObject == null) {
            return;
        }

        String objectType = namedObject.getObjectType();

        switch(objectType) {
            case ActionsList.OBJECT_TYPE:
                this.dispatchActionsList((ActionsList)namedObject, context);
                break;

            case ActionsSequence.OBJECT_TYPE:
                ActionsSequence actionsSequence = (ActionsSequence)namedObject;
                new NamedObjectsDispatchTask(mNamedObjectsStorage, context).execute(actionsSequence.getId());
                break;

            default:
                Action action = ((Action)namedObject);
                action.evaluate(context);
        }
    }

    private void dispatchActionsList(ActionsList actionsList, Context context) {
        Intent intent = new Intent(context, SelectNamedObjectActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS | Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_FROM_BACKGROUND);
        intent.putExtra(SelectNamedObjectActivity.ACTIONS_LIST_ID_PARAMETER, actionsList.getId());
        context.startActivity(intent);
    }

    private final NamedObjectsStorage mNamedObjectsStorage;
}
