package com.f1x.mtcdtools.named.objects;

import android.content.Context;
import android.content.Intent;

import com.f1x.mtcdtools.activities.SelectNamedObjectActivity;
import com.f1x.mtcdtools.configuration.Configuration;
import com.f1x.mtcdtools.named.objects.actions.Action;
import com.f1x.mtcdtools.storage.NamedObjectsStorage;

/**
 * Created by COMPUTER on 2017-02-05.
 */

public class NamedObjectDispatcher {
    public NamedObjectDispatcher(NamedObjectsStorage namedObjectsStorage, Configuration configuration) {
        mNamedObjectsStorage = namedObjectsStorage;
        mConfiguration = configuration;
    }

    public void dispatch(String namedObjectName, Context context) {
        NamedObject namedObject = mNamedObjectsStorage.getItem(namedObjectName);

        if(namedObject == null) {
            return;
        }

        String objectType = namedObject.getObjectType();

        try {
            if (objectType.equals(ActionsList.OBJECT_TYPE)) {
                this.dispatchActionsList(namedObject, context);
            } else if (objectType.equals(ActionsSequence.OBJECT_TYPE)) {
                this.dispatchActionsSequence(namedObject, context);
            } else {
                this.dispatchAction(namedObject, context);
            }
        } catch(ClassCastException e) {
            e.printStackTrace();
        }
    }

    private void dispatchActionsList(NamedObject namedObject, Context context) {
        ActionsList actionsList = (ActionsList) namedObject;

        Intent intent = new Intent(context, SelectNamedObjectActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS | Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_FROM_BACKGROUND);
        intent.putExtra(SelectNamedObjectActivity.ACTIONS_LIST_NAME_PARAMETER, actionsList.getName());
        context.startActivity(intent);
    }

    private void dispatchActionsSequence(NamedObject namedObject, Context context) {
        ActionsSequence actionsSequence = (ActionsSequence)namedObject;
        new ActionsSequenceDispatchTask(actionsSequence.getActionNames(), context, mNamedObjectsStorage).execute(mConfiguration.getActionsSequenceDelay());
    }

    private void dispatchAction(NamedObject namedObject, Context context) {
        Action action = (Action)namedObject;
        action.evaluate(context);
    }

    private final NamedObjectsStorage mNamedObjectsStorage;
    private final Configuration mConfiguration;
}
