package com.f1x.mtcdtools.named.objects;

import com.f1x.mtcdtools.named.objects.actions.Action;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Created by COMPUTER on 2017-02-09.
 */

public class ActionsSequenceTest {
    @Before
    public void init() throws JSONException {
        mSequenceJson = new JSONObject();
        mSequenceName = "sequence1";
        mSequenceJson.put(ActionsSequence.NAME_PROPERTY, mSequenceName);
        mSequenceJson.put(ActionsSequence.OBJECT_TYPE_PROPERTY, ActionsSequence.OBJECT_TYPE);

        mActionsArray = new JSONArray();
        mActionsArray.put("action1");
        mActionsArray.put("action2");
        mActionsArray.put("action3");
        mSequenceJson.put(ActionsSequence.ACTIONS_PROPERTY, mActionsArray);

        mDelaysArray = new JSONArray();

        JSONObject action1Delay = new JSONObject();
        action1Delay.put(Action.NAME_PROPERTY, "action1");
        action1Delay.put(ActionsSequence.ACTION_DELAY_PROPERTY, 30);
        mDelaysArray.put(action1Delay);

        JSONObject action2Delay = new JSONObject();
        action2Delay.put(Action.NAME_PROPERTY, "action2");
        action2Delay.put(ActionsSequence.ACTION_DELAY_PROPERTY, 90);
        mDelaysArray.put(action2Delay);

        JSONObject action3Delay = new JSONObject();
        action3Delay.put(Action.NAME_PROPERTY, "action3");
        action3Delay.put(ActionsSequence.ACTION_DELAY_PROPERTY, 0);
        mDelaysArray.put(action3Delay);

        mSequenceJson.put(ActionsSequence.ACTION_DELAYS_PROPERTY, mDelaysArray);
    }

    @Test
    public void test_Construct() throws JSONException {
        ActionsSequence actionsSequence = new ActionsSequence(mSequenceJson);
        assertEquals(mSequenceName, actionsSequence.getName());

        List<String> actionNames = new ArrayList<>(actionsSequence.getActionsNames());

        assertEquals(mActionsArray.length(), actionNames.size());
        for (int i = 0; i < mActionsArray.length(); ++i) {
            assertEquals(mActionsArray.get(i), actionNames.get(i));
        }

        assertEquals(30, actionsSequence.getDelayForAction("action1"));
        assertEquals(90, actionsSequence.getDelayForAction("action2"));
        assertEquals(0, actionsSequence.getDelayForAction("action3"));
    }

    @Test
    public void test_Construct_Backward_Compatibility() throws JSONException {
        mSequenceJson.remove(ActionsSequence.ACTION_DELAYS_PROPERTY);

        ActionsSequence actionsSequence = new ActionsSequence(mSequenceJson);
        assertEquals(mSequenceName, actionsSequence.getName());

        List<String> actionNames = new ArrayList<>(actionsSequence.getActionsNames());

        assertEquals(mActionsArray.length(), actionNames.size());
        for (int i = 0; i < mActionsArray.length(); ++i) {
            assertEquals(mActionsArray.get(i), actionNames.get(i));
        }

        assertEquals(0, actionsSequence.getDelayForAction("action1"));
        assertEquals(0, actionsSequence.getDelayForAction("action2"));
        assertEquals(0, actionsSequence.getDelayForAction("action3"));
    }

    @Test
    public void test_toJSON() throws JSONException {
        ActionsSequence actionsSequence = new ActionsSequence(mSequenceJson);
        assertEquals(mSequenceJson.toString(), actionsSequence.toJson().toString());
    }

    @Test
    public void test_RemoveActionName() throws JSONException {
        ActionsSequence actionsSequence = new ActionsSequence(mSequenceJson);
        actionsSequence.removeDependency(mActionsArray.getString(1));

        assertFalse(actionsSequence.getActionsNames().contains(mActionsArray.getString(1)));
        assertTrue(actionsSequence.getActionsNames().contains(mActionsArray.getString(0)));
        assertTrue(actionsSequence.getActionsNames().contains(mActionsArray.getString(2)));

        assertEquals(0, actionsSequence.getDelayForAction(mActionsArray.getString(1)));
    }

    @Test
    public void test_ReplaceActionName_SameName() throws JSONException {
        ActionsSequence actionsSequence = new ActionsSequence(mSequenceJson);

        actionsSequence.replaceDependency(mActionsArray.getString(1), mActionsArray.getString(1));
        assertTrue(actionsSequence.getActionsNames().contains(mActionsArray.getString(1)));
        assertEquals(90, actionsSequence.getDelayForAction(mActionsArray.getString(1)));
    }

    @Test
    public void test_ReplaceActionName_NewName() throws JSONException {
        ActionsSequence actionsSequence = new ActionsSequence(mSequenceJson);

        String newActionName = "actionNewName";
        actionsSequence.replaceDependency(mActionsArray.getString(1), newActionName);

        assertFalse(actionsSequence.getActionsNames().contains(mActionsArray.getString(1)));
        assertTrue(actionsSequence.getActionsNames().contains(newActionName));

        assertEquals(0, actionsSequence.getDelayForAction(mActionsArray.getString(1)));
        assertEquals(90, actionsSequence.getDelayForAction(newActionName));
    }

    @Test
    public void test_ReplaceActionName_NonExistent() throws JSONException {
        ActionsSequence actionsSequence = new ActionsSequence(mSequenceJson);

        String nonExistentActionName = "nonExistentAction";
        String newActionName = "actionNewName";
        actionsSequence.replaceDependency(nonExistentActionName, newActionName);

        assertFalse(actionsSequence.getActionsNames().contains(newActionName));
        assertEquals(0, actionsSequence.getDelayForAction(newActionName));
    }

    private String mSequenceName;
    private JSONObject mSequenceJson;
    private JSONArray mActionsArray;
    private JSONArray mDelaysArray;
}
