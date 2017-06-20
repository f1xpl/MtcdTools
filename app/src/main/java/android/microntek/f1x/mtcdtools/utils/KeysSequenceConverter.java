package android.microntek.f1x.mtcdtools.utils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by f1x on 2017-01-23.
 */

public class KeysSequenceConverter {
    public static JSONArray toJsonArray(List<Integer> keysSequence) {
        JSONArray jsonArray = new JSONArray();

        for(int i = 0; i < keysSequence.size(); ++i) {
            jsonArray.put(keysSequence.get(i));
        }

        return jsonArray;
    }

    public static List<Integer> fromJsonArray(JSONArray array) throws JSONException {
        List<Integer> keysSequence = new ArrayList<>();

        for(int i = 0; i < array.length(); ++i) {
            keysSequence.add(array.getInt(i));
        }

        return keysSequence;
    }

    public static List<Integer> fromArray(int array[]) {
        List<Integer> keysSequence = new ArrayList<>();

        for(Integer value : array) {
            keysSequence.add(value);
        }

        return keysSequence;
    }
}
