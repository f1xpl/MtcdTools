package com.microntek.f1x.mtcdtools.named;

import com.microntek.f1x.mtcdtools.named.objects.actions.Action;
import com.microntek.f1x.mtcdtools.named.objects.actions.BroadcastIntentAction;
import com.microntek.f1x.mtcdtools.named.objects.actions.KeyAction;
import com.microntek.f1x.mtcdtools.named.objects.actions.LaunchAction;
import com.microntek.f1x.mtcdtools.named.objects.actions.StartIntentAction;
import com.microntek.f1x.mtcdtools.named.objects.containers.ActionsList;
import com.microntek.f1x.mtcdtools.named.objects.containers.ActionsSequence;
import com.microntek.f1x.mtcdtools.named.objects.containers.ModeList;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by f1x on 2017-02-05.
 */

public class NamedObjectsFactory {
    public static NamedObject createNamedObject(JSONObject json) throws JSONException {
        String actionType = json.getString(Action.OBJECT_TYPE_PROPERTY);

        switch(actionType) {
            case KeyAction.OBJECT_TYPE:
                return new KeyAction(json);
            case LaunchAction.OBJECT_TYPE:
                return new LaunchAction(json);
            case StartIntentAction.OBJECT_TYPE:
                return new StartIntentAction(json);
            case BroadcastIntentAction.OBJECT_TYPE:
                return new BroadcastIntentAction(json);
            case ActionsList.OBJECT_TYPE:
                return new ActionsList(json);
            case ActionsSequence.OBJECT_TYPE:
                return new ActionsSequence(json);
            case ModeList.OBJECT_TYPE:
                return new ModeList(json);
            default:
                return null;
        }
    }
}
