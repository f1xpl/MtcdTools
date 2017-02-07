package com.f1x.mtcdtools.named.objects;

import com.f1x.mtcdtools.named.objects.actions.Action;
import com.f1x.mtcdtools.storage.NamedObjectsStorage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by COMPUTER on 2017-02-07.
 */

class ActionsExtractor {
    static List<Action> extract(List<String> namedObjectsNames, NamedObjectsStorage namedObjectsStorage) {
        List<Action> extractedActions = new ArrayList<>();
        extract(extractedActions, namedObjectsNames, namedObjectsStorage);

        return extractedActions;
    }

    private static void extract(List<Action> output, List<String> namedObjectsNames, NamedObjectsStorage namedObjectsStorage) {
        for (String namedObjectName : namedObjectsNames) {
            NamedObject namedObject = namedObjectsStorage.getItem(namedObjectName);

            if (namedObject != null) {
                if (namedObject.getObjectType().equals(ActionsSequence.OBJECT_TYPE)) {
                    ActionsSequence actionsSequence = (ActionsSequence) namedObject;
                    extract(output, actionsSequence.getActionsNames(), namedObjectsStorage);
                } else if (Action.isAction(namedObject.getObjectType())) {
                    output.add((Action) namedObject);
                }
            }
        }
    }
}
