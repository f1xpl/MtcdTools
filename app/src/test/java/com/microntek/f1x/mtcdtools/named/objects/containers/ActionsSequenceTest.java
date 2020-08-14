package com.microntek.f1x.mtcdtools.named.objects.containers;

import com.microntek.f1x.mtcdtools.named.NamedObjectId;
import com.microntek.f1x.mtcdtools.named.objects.actions.Action;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by COMPUTER on 2017-02-09.
 */

public class ActionsSequenceTest {
    @Before
    public void init() throws JSONException {
        mSequenceJson = new JSONObject();
        mSequenceId = new NamedObjectId("sequence1");
        mSequenceJson.put(ActionsSequence.NAME_PROPERTY, mSequenceId.toString());
        mSequenceJson.put(ActionsSequence.OBJECT_TYPE_PROPERTY, ActionsSequence.OBJECT_TYPE);

        mActionsArray = new JSONArray();
        mActionsArray.put("action1");
        mActionsArray.put("action2");
        mActionsArray.put("action3");
        mSequenceJson.put(ActionsSequence.ACTIONS_PROPERTY, mActionsArray);

        JSONArray delaysArray = new JSONArray();

        JSONObject action1Delay = new JSONObject();
        action1Delay.put(Action.NAME_PROPERTY, "action1");
        action1Delay.put(ActionsSequence.ACTION_DELAY_PROPERTY, 30);
        delaysArray.put(action1Delay);

        JSONObject action2Delay = new JSONObject();
        action2Delay.put(Action.NAME_PROPERTY, "action2");
        action2Delay.put(ActionsSequence.ACTION_DELAY_PROPERTY, 60);
        delaysArray.put(action2Delay);

        JSONObject action3Delay = new JSONObject();
        action3Delay.put(Action.NAME_PROPERTY, "action3");
        action3Delay.put(ActionsSequence.ACTION_DELAY_PROPERTY, 90);
        delaysArray.put(action3Delay);

        mSequenceJson.put(ActionsSequence.ACTION_DELAYS_PROPERTY, delaysArray);
    }

    @Test
    public void test_Construct() throws JSONException {
        ActionsSequence actionsSequence = new ActionsSequence(mSequenceJson);
        assertEquals(mSequenceId, actionsSequence.getId());

        List<NamedObjectId> actionIds = new ArrayList<>(actionsSequence.getActionIds());

        assertEquals(mActionsArray.length(), actionIds.size());
        for (int i = 0; i < mActionsArray.length(); ++i) {
            assertEquals(new NamedObjectId(mActionsArray.getString(i)), actionIds.get(i));
        }

        assertEquals(30, actionsSequence.getDelayForAction(0));
        assertEquals(new NamedObjectId("action1"), actionsSequence.getActionDelays().get(0).getKey());

        assertEquals(60, actionsSequence.getDelayForAction(1));
        assertEquals(new NamedObjectId("action2"), actionsSequence.getActionDelays().get(1).getKey());

        assertEquals(90, actionsSequence.getDelayForAction(2));
        assertEquals(new NamedObjectId("action3"), actionsSequence.getActionDelays().get(2).getKey());
    }

    @Test
    public void test_toJSON() throws JSONException {
        ActionsSequence actionsSequence = new ActionsSequence(mSequenceJson);
        assertEquals(mSequenceJson.toString(), actionsSequence.toJson().toString());
    }

    @Test
    public void test_RemoveDependency() throws JSONException {
        ActionsSequence actionsSequence = new ActionsSequence(mSequenceJson);
        actionsSequence.removeDependency(new NamedObjectId(mActionsArray.getString(1)));

        assertFalse(actionsSequence.getActionIds().contains(new NamedObjectId(mActionsArray.getString(1))));
        assertTrue(actionsSequence.getActionIds().contains(new NamedObjectId(mActionsArray.getString(0))));
        assertTrue(actionsSequence.getActionIds().contains(new NamedObjectId(mActionsArray.getString(2))));

        assertEquals(2, actionsSequence.getActionDelays().size());

        assertEquals(30, actionsSequence.getDelayForAction(0));
        assertEquals(new NamedObjectId("action1"), actionsSequence.getActionDelays().get(0).getKey());

        assertEquals(90, actionsSequence.getDelayForAction(1));
        assertEquals(new NamedObjectId("action3"), actionsSequence.getActionDelays().get(1).getKey());
    }

    @Test
    public void test_ReplaceDependency_SameName() throws JSONException {
        ActionsSequence actionsSequence = new ActionsSequence(mSequenceJson);

        actionsSequence.replaceDependency(new NamedObjectId(mActionsArray.getString(1)), new NamedObjectId(mActionsArray.getString(1)));
        assertTrue(actionsSequence.getActionIds().contains(new NamedObjectId(mActionsArray.getString(1))));
        assertEquals(60, actionsSequence.getDelayForAction(1));
        assertEquals(new NamedObjectId(mActionsArray.getString(1)), actionsSequence.getActionDelays().get(1).getKey());
    }

    @Test
    public void test_ReplaceDependency_NewName() throws JSONException {
        ActionsSequence actionsSequence = new ActionsSequence(mSequenceJson);

        NamedObjectId newActionId = new NamedObjectId("actionNewName");
        actionsSequence.replaceDependency(new NamedObjectId(mActionsArray.getString(1)), newActionId);

        assertFalse(actionsSequence.getActionIds().contains(new NamedObjectId(mActionsArray.getString(1))));
        assertTrue(actionsSequence.getActionIds().contains(newActionId));

        assertEquals(60, actionsSequence.getDelayForAction(1));
        assertEquals(newActionId, actionsSequence.getActionDelays().get(1).getKey());

        assertEquals(3, actionsSequence.getActionDelays().size());
        for(Map.Entry<NamedObjectId, Integer> entry : actionsSequence.getActionDelays()) {
            assertNotEquals(entry.getKey(), new NamedObjectId(mActionsArray.getString(1)));
        }
    }

    @Test
    public void test_ReplaceDependency_NonExistent() throws JSONException {
        ActionsSequence actionsSequence = new ActionsSequence(mSequenceJson);

        NamedObjectId nonExistentActionId = new NamedObjectId("nonExistentAction");
        NamedObjectId newActionId = new NamedObjectId("actionNewName");
        actionsSequence.replaceDependency(nonExistentActionId, newActionId);

        assertFalse(actionsSequence.getActionIds().contains(newActionId));

        assertEquals(3, actionsSequence.getActionDelays().size());
        for(Map.Entry<NamedObjectId, Integer> entry : actionsSequence.getActionDelays()) {
            assertNotEquals(entry.getKey(), nonExistentActionId);
            assertNotEquals(entry.getKey(), newActionId);
        }
    }

    private NamedObjectId mSequenceId;
    private JSONObject mSequenceJson;
    private JSONArray mActionsArray;
}
