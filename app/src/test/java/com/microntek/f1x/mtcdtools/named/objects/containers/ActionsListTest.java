package com.microntek.f1x.mtcdtools.named.objects.containers;

import com.microntek.f1x.mtcdtools.named.NamedObjectId;
import com.microntek.f1x.mtcdtools.utils.KeysSequenceConverter;

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
 * Created by f1x on 2017-01-16.
 */

public class ActionsListTest {
    @Before
    public void init() throws JSONException {
        mListJson = new JSONObject();
        mListId = new NamedObjectId("sequence1");
        mListJson.put(ActionsList.NAME_PROPERTY, mListId.toString());
        mListJson.put(ActionsList.OBJECT_TYPE_PROPERTY, ActionsList.OBJECT_TYPE);

        mActionsArray = new JSONArray();
        mActionsArray.put("action1");
        mActionsArray.put("action2");
        mActionsArray.put("action3");
        mListJson.put(ActionsList.ACTIONS_PROPERTY, mActionsArray);

        mKeysSequenceUpArray = new JSONArray();
        mKeysSequenceUpArray.put(1);
        mKeysSequenceUpArray.put(100);
        mKeysSequenceUpArray.put(1000);
        mListJson.put(ActionsList.KEYS_SEQUENCE_UP_PROPERTY, mKeysSequenceUpArray);

        mKeysSequenceDownArray = new JSONArray();
        mKeysSequenceDownArray.put(2);
        mKeysSequenceDownArray.put(300);
        mKeysSequenceDownArray.put(4000);
        mKeysSequenceDownArray.put(50000);
        mListJson.put(ActionsList.KEYS_SEQUENCE_DOWN_PROPERTY, mKeysSequenceDownArray);
    }

    @Test
    public void test_Construct() throws JSONException {
        ActionsList actionsList = new ActionsList(mListJson);
        assertEquals(mListId, actionsList.getId());

        List<NamedObjectId> actionIds = actionsList.getActionIds();
        assertEquals(mActionsArray.length(), actionIds.size());
        for(int i = 0; i < mActionsArray.length(); ++i) {
            assertEquals(new NamedObjectId(mActionsArray.getString(i)), actionIds.get(i));
        }

        List<Integer> keysSequenceUp = actionsList.getKeysSequenceUp();
        assertEquals(mKeysSequenceUpArray.length(), keysSequenceUp.size());
        for(int i = 0; i < mKeysSequenceUpArray.length(); ++i) {
            assertEquals(mKeysSequenceUpArray.get(i), keysSequenceUp.get(i));
        }

        List<Integer> keysSequenceDown = actionsList.getKeysSequenceDown();
        assertEquals(mKeysSequenceDownArray.length(), keysSequenceDown.size());
        for(int i = 0; i < mKeysSequenceDownArray.length(); ++i) {
            assertEquals(mKeysSequenceDownArray.get(i), keysSequenceDown.get(i));
        }
    }

    @Test
    public void test_toJSON() throws JSONException {
        ActionsList actionsList = new ActionsList(mListJson);
        assertEquals(mListJson.toString(), actionsList.toJson().toString());
    }

    @Test
    public void test_ConstructFromParameters() throws JSONException {
        List<NamedObjectId> expectedActionIds = new ArrayList<>();
        for(int i = 0; i < mActionsArray.length(); ++i) {
            expectedActionIds.add(new NamedObjectId(mActionsArray.getString(i)));
        }

        ActionsList actionsList = new ActionsList(mListId,
                                                  KeysSequenceConverter.fromJsonArray(mKeysSequenceUpArray),
                                                  KeysSequenceConverter.fromJsonArray(mKeysSequenceDownArray),
                                                  expectedActionIds);

        assertEquals(mListId, actionsList.getId());

        List<NamedObjectId> actualActionIds = actionsList.getActionIds();
        assertEquals(expectedActionIds.size(), actualActionIds.size());
        assertEquals(expectedActionIds, actualActionIds);

        List<Integer> keysSequenceUp = actionsList.getKeysSequenceUp();
        assertEquals(mKeysSequenceUpArray.length(), keysSequenceUp.size());
        for(int i = 0; i < mKeysSequenceUpArray.length(); ++i) {
            assertEquals(mKeysSequenceUpArray.get(i), keysSequenceUp.get(i));
        }

        List<Integer> keysSequenceDown = actionsList.getKeysSequenceDown();
        assertEquals(mKeysSequenceDownArray.length(), keysSequenceDown.size());
        for(int i = 0; i < mKeysSequenceDownArray.length(); ++i) {
            assertEquals(mKeysSequenceDownArray.get(i), keysSequenceDown.get(i));
        }
    }

    @Test
    public void test_RemoveDependency() throws JSONException {
        ActionsList actionsList = new ActionsList(mListJson);
        actionsList.removeDependency(new NamedObjectId(mActionsArray.getString(1)));

        assertFalse(actionsList.getActionIds().contains(new NamedObjectId(mActionsArray.getString(1))));
        assertTrue(actionsList.getActionIds().contains(new NamedObjectId(mActionsArray.getString(0))));
        assertTrue(actionsList.getActionIds().contains(new NamedObjectId(mActionsArray.getString(2))));
    }

    @Test
    public void test_ReplaceDependency_SameName() throws JSONException {
        ActionsList actionsList = new ActionsList(mListJson);
        actionsList.replaceDependency(new NamedObjectId(mActionsArray.getString(1)), new NamedObjectId(mActionsArray.getString(1)));
        assertTrue(actionsList.getActionIds().contains(new NamedObjectId(mActionsArray.getString(1))));
    }


    @Test
    public void test_ReplaceDependency_NewName() throws JSONException {
        ActionsList actionsList = new ActionsList(mListJson);

        String newActionName = "actionNewName";
        actionsList.replaceDependency(new NamedObjectId(mActionsArray.getString(1)), new NamedObjectId(newActionName));

        assertFalse(actionsList.getActionIds().contains(new NamedObjectId(mActionsArray.getString(1))));
        assertTrue(actionsList.getActionIds().contains(new NamedObjectId(newActionName)));
    }

    @Test
    public void test_ReplaceDependency_NonExistent() throws JSONException {
        ActionsList actionsList = new ActionsList(mListJson);

        String nonExistentActionName = "nonExistentAction";
        String newActionName = "actionNewName";
        actionsList.replaceDependency(new NamedObjectId(nonExistentActionName), new NamedObjectId(newActionName));

        assertFalse(actionsList.getActionIds().contains(new NamedObjectId(newActionName)));
    }

    private NamedObjectId mListId;
    private JSONObject mListJson;
    private JSONArray mActionsArray;
    private JSONArray mKeysSequenceUpArray;
    private JSONArray mKeysSequenceDownArray;
}
