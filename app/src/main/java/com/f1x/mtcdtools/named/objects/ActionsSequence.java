package com.f1x.mtcdtools.named.objects;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by COMPUTER on 2017-02-05.
 */

public class ActionsSequence extends NamedObjectsContainer {
    public ActionsSequence(JSONObject json) throws JSONException {
        super(json);
    }

    public ActionsSequence(String name, List<String> actionsNames) {
        super(name, OBJECT_TYPE, actionsNames);
    }

    static public final String OBJECT_TYPE = "ActionsSequence";
}
