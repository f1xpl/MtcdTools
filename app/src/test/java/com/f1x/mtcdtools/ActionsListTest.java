package com.f1x.mtcdtools;

import com.f1x.mtcdtools.input.KeysSequenceConverter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Created by COMPUTER on 2017-01-16.
 */

public class ActionsListTest {
    @Before
    public void init() throws JSONException {
        mListJson = new JSONObject();
        mListName = "sequence1";
        mListJson.put(ActionsList.NAME_PROPERTY, mListName);

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

        List<String> actionNames = new ArrayList<>(actionsList.getActionNames());

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
        Set<String> expectedActionsNames = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        for(int i = 0; i < mActionsArray.length(); ++i) {
            expectedActionsNames.add(mActionsArray.getString(i));
        }

        ActionsList actionsList = new ActionsList(mListName,
                                                  KeysSequenceConverter.fromJsonArray(mKeysSequenceUpArray),
                                                  KeysSequenceConverter.fromJsonArray(mKeysSequenceDownArray),
                                                  expectedActionsNames);

        assertEquals(mListName, actionsList.getName());

        Set<String> actualActionNames = actionsList.getActionNames();
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
    public void test_RemoveAction() throws JSONException {
        ActionsList actionsList = new ActionsList(mListJson);
        actionsList.removeAction(mActionsArray.getString(1));

        assertFalse(actionsList.getActionNames().contains(mActionsArray.getString(1)));
        assertTrue(actionsList.getActionNames().contains(mActionsArray.getString(0)));
        assertTrue(actionsList.getActionNames().contains(mActionsArray.getString(2)));
    }

    private String mListName;
    private JSONObject mListJson;
    private JSONArray mActionsArray;
    private JSONArray mKeysSequenceUpArray;
    private JSONArray mKeysSequenceDownArray;
}
