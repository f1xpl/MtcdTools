package com.f1x.mtcdtools;

import android.content.Context;
import android.content.Intent;

import com.f1x.mtcdtools.actions.Action;
import com.f1x.mtcdtools.activities.SelectNamedObjectActivity;
import com.f1x.mtcdtools.storage.NamedObject;

/**
 * Created by COMPUTER on 2017-02-05.
 */

public class NamedObjectDispatcher {
    public void dispatch(NamedObject namedObject, Context context) {
        String objectType = namedObject.getObjectType();

        if (objectType.equals(ActionsList.OBJECT_TYPE)) {
            ActionsList actionsList = (ActionsList) namedObject;

            Intent intent = new Intent(context, SelectNamedObjectActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS | Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_FROM_BACKGROUND);
            intent.putExtra(SelectNamedObjectActivity.ACTIONS_LIST_NAME_PARAMETER, actionsList.getName());
            context.startActivity(intent);
        } else {
            Action action = (Action) namedObject;
            action.evaluate(context);
        }
    }
}
