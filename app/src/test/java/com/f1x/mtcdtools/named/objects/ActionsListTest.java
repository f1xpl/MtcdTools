package com.f1x.mtcdtools.named.objects;

import com.f1x.mtcdtools.input.KeysSequenceConverter;

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
        mListName = "sequence1";
        mListJson.put(ActionsList.NAME_PROPERTY, mListName);
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

        assertEquals(mListName, actionsList.getName());

        List<String> actionNames = new ArrayList<>(actionsList.getActionsNames());

        assertEquals(mActionsArray.length(), actionNames.size());
        for(int i = 0; i < mActionsArray.length(); ++i) {
            assertEquals(mActionsArray.get(i), actionNames.get(i));
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
        List<String> expectedActionsNames = new ArrayList<>();
        for(int i = 0; i < mActionsArray.length(); ++i) {
            expectedActionsNames.add(mActionsArray.getString(i));
        }

        ActionsList actionsList = new ActionsList(mListName,
                                                  KeysSequenceConverter.fromJsonArray(mKeysSequenceUpArray),
                                                  KeysSequenceConverter.fromJsonArray(mKeysSequenceDownArray),
                                                  expectedActionsNames);

        assertEquals(mListName, actionsList.getName());

        List<String> actualActionNames = actionsList.getActionsNames();
        assertEquals(expectedActionsNames.size(), actualActionNames.size());
        assertEquals(expectedActionsNames, actualActionNames);

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
    public void test_RemoveActionName() throws JSONException {
        ActionsList actionsList = new ActionsList(mListJson);
        actionsList.removeDependency(mActionsArray.getString(1));

        assertFalse(actionsList.getActionsNames().contains(mActionsArray.getString(1)));
        assertTrue(actionsList.getActionsNames().contains(mActionsArray.getString(0)));
        assertTrue(actionsList.getActionsNames().contains(mActionsArray.getString(2)));
    }

    @Test
    public void test_ReplaceActionName_SameName() throws JSONException {
        ActionsList actionsList = new ActionsList(mListJson);
        actionsList.replaceDependency(mActionsArray.getString(1), mActionsArray.getString(1));
        assertTrue(actionsList.getActionsNames().contains(mActionsArray.getString(1)));
    }


    @Test
    public void test_ReplaceActionName_NewName() throws JSONException {
        ActionsList actionsList = new ActionsList(mListJson);

        String newActionName = "actionNewName";
        actionsList.replaceDependency(mActionsArray.getString(1), newActionName);

        assertFalse(actionsList.getActionsNames().contains(mActionsArray.getString(1)));
        assertTrue(actionsList.getActionsNames().contains(newActionName));
    }

    @Test
    public void test_ReplaceActionName_NonExistent() throws JSONException {
        ActionsList actionsList = new ActionsList(mListJson);

        String nonExistentActionName = "nonExistentAction";
        String newActionName = "actionNewName";
        actionsList.replaceDependency(nonExistentActionName, newActionName);

        assertFalse(actionsList.getActionsNames().contains(newActionName));
    }

    private String mListName;
    private JSONObject mListJson;
    private JSONArray mActionsArray;
    private JSONArray mKeysSequenceUpArray;
    private JSONArray mKeysSequenceDownArray;
}
