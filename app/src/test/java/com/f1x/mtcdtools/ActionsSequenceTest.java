package com.f1x.mtcdtools;

import com.f1x.mtcdtools.ActionsSequence;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * Created by COMPUTER on 2017-01-16.
 */

public class ActionsSequenceTest {
    @Before
    public void init() throws JSONException {
        mSequenceJson = new JSONObject();
        mSequenceName = "sequence1";
        mSequenceJson.put(ActionsSequence.NAME_PROPERTY, mSequenceName);

        mActionsArray = new JSONArray();
        mActionsArray.put("action1");
        mActionsArray.put("action2");
        mActionsArray.put("action3");
        mSequenceJson.put(ActionsSequence.ACTIONS_PROPERTY, mActionsArray);

        mKeysSequenceUpArray = new JSONArray();
        mKeysSequenceUpArray.put(1);
        mKeysSequenceUpArray.put(100);
        mKeysSequenceUpArray.put(1000);
        mSequenceJson.put(ActionsSequence.KEYS_SEQUENCE_UP_PROPERTY, mKeysSequenceUpArray);

        mKeysSequenceDownArray = new JSONArray();
        mKeysSequenceDownArray.put(2);
        mKeysSequenceDownArray.put(300);
        mKeysSequenceDownArray.put(4000);
        mKeysSequenceDownArray.put(50000);
        mSequenceJson.put(ActionsSequence.KEYS_SEQUENCE_DOWN_PROPERTY, mKeysSequenceDownArray);
    }

    @Test
    public void test_Construct() throws JSONException {
        ActionsSequence actionsSequence = new ActionsSequence(mSequenceJson);

        assertEquals(mSequenceName, actionsSequence.getName());

        List<String> actionNames = actionsSequence.getActionNames();
        assertEquals(mActionsArray.length(), actionNames.size());
        for(int i = 0; i < mActionsArray.length(); ++i) {
            assertEquals(mActionsArray.get(i), actionNames.get(i));
        }

        List<Integer> keysSequenceUp = actionsSequence.getKeysSequenceUp();
        assertEquals(mKeysSequenceUpArray.length(), keysSequenceUp.size());
        for(int i = 0; i < mKeysSequenceUpArray.length(); ++i) {
            assertEquals(mKeysSequenceUpArray.get(i), keysSequenceUp.get(i));
        }

        List<Integer> keysSequenceDown = actionsSequence.getKeysSequenceDown();
        assertEquals(mKeysSequenceDownArray.length(), keysSequenceDown.size());
        for(int i = 0; i < mKeysSequenceDownArray.length(); ++i) {
            assertEquals(mKeysSequenceDownArray.get(i), keysSequenceDown.get(i));
        }
    }

    @Test
    public void test_toJSON() throws JSONException {
        ActionsSequence actionsSequence = new ActionsSequence(mSequenceJson);
        assertEquals(mSequenceJson.toString(), actionsSequence.toJson().toString());
    }

    String mSequenceName;
    JSONObject mSequenceJson;
    JSONArray mActionsArray;
    JSONArray mKeysSequenceUpArray;
    JSONArray mKeysSequenceDownArray;
}
