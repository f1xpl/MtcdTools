package com.f1x.mtcdtools.named.objects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * Created by COMPUTER on 2017-02-23.
 */

public class ModeListTest {
    @Before
    public void init() throws JSONException {
        mModeListJson = new JSONObject();
        mListId = new NamedObjectId("sequence1");
        mModeListJson.put(ModeList.NAME_PROPERTY, mListId.toString());
        mModeListJson.put(ModeList.OBJECT_TYPE_PROPERTY, ModeList.OBJECT_TYPE);

        mActionsArray = new JSONArray();
        mActionsArray.put("action1");
        mActionsArray.put("action2");
        mActionsArray.put("action3");
        mModeListJson.put(ModeList.ACTIONS_PROPERTY, mActionsArray);
    }

    @Test
    public void test_Construct() throws JSONException {
        ModeList modeList = new ModeList(mModeListJson);
        assertEquals(mListId, modeList.getId());

        List<NamedObjectId> actionIds = modeList.getActionIds();
        assertEquals(mActionsArray.length(), actionIds.size());
        for(int i = 0; i < mActionsArray.length(); ++i) {
            assertEquals(new NamedObjectId(mActionsArray.getString(i)), actionIds.get(i));
        }
    }

    @Test
    public void test_toJSON() throws JSONException {
        ModeList modeList = new ModeList(mModeListJson);
        assertEquals(mModeListJson.toString(), modeList.toJson().toString());
    }

    @Test
    public void test_ConstructFromParameters() throws JSONException {
        List<NamedObjectId> expectedActionIds = new ArrayList<>();
        for(int i = 0; i < mActionsArray.length(); ++i) {
            expectedActionIds.add(new NamedObjectId(mActionsArray.getString(i)));
        }

        ModeList modeList = new ModeList(mListId, expectedActionIds);

        assertEquals(mListId, modeList.getId());

        List<NamedObjectId> actualActionIds = modeList.getActionIds();
        assertEquals(expectedActionIds.size(), actualActionIds.size());
        assertEquals(expectedActionIds, actualActionIds);
    }

    @Test
    public void test_Evalute() throws JSONException {
        ModeList modeList = new ModeList(mModeListJson);

        for(int i = 0; i < mActionsArray.length() * 4; ++i) {
            final int index = i % mActionsArray.length();
            assertEquals(new NamedObjectId(mActionsArray.getString(index)), modeList.evaluate());
        }
    }

    @Test
    public void test_Evalute_EmptyList() throws JSONException {
        JSONObject modeListJson = new JSONObject();
        modeListJson.put(ModeList.NAME_PROPERTY, mListId.toString());
        modeListJson.put(ModeList.OBJECT_TYPE_PROPERTY, ModeList.OBJECT_TYPE);
        modeListJson.put(ModeList.ACTIONS_PROPERTY, new JSONArray());

        ModeList modeList = new ModeList(modeListJson);

        assertEquals(null, modeList.evaluate());
        assertEquals(null, modeList.evaluate());
        assertEquals(null, modeList.evaluate());
    }

    @Test
    public void test_Evaluate_ResetModeIndex_WhenObjectHasBeenRemoved() throws JSONException {
        ModeList modeList = new ModeList(mModeListJson);

        assertEquals(new NamedObjectId(mActionsArray.getString(0)), modeList.evaluate());
        assertEquals(new NamedObjectId(mActionsArray.getString(1)), modeList.evaluate());

        modeList.removeDependency(new NamedObjectId(mActionsArray.getString(2)));

        assertEquals(new NamedObjectId(mActionsArray.getString(0)), modeList.evaluate());
        assertEquals(new NamedObjectId(mActionsArray.getString(1)), modeList.evaluate());
        assertEquals(new NamedObjectId(mActionsArray.getString(0)), modeList.evaluate());
        assertEquals(new NamedObjectId(mActionsArray.getString(1)), modeList.evaluate());
    }

    private NamedObjectId mListId;
    private JSONObject mModeListJson;
    private JSONArray mActionsArray;
}
