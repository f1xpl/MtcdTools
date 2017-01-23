package com.f1x.mtcdtools.input;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by COMPUTER on 2017-01-23.
 */

public class KeysSequenceConverter {
    public static JSONArray toJsonArray(List<Integer> keysSequence) {
        JSONArray jsonArray = new JSONArray();

        for(int i = 0; i < keysSequence.size(); ++i) {
            jsonArray.put(keysSequence.get(i));
        }

        return jsonArray;
    }

    public static List<Integer> fromJson(JSONArray array) throws JSONException {
        List<Integer> keysSequence = new ArrayList<>();

        for(int i = 0; i < array.length(); ++i) {
            keysSequence.add(array.getInt(i));
        }

        return keysSequence;
    }
}
