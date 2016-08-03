package com.f1x.mtcdtools;

import com.f1x.mtcdtools.keyinputs.KeyInputsReaderInterface;
import com.f1x.mtcdtools.keyinputs.KeyInputsStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by COMPUTER on 2016-08-01.
 */
class MockKeyInputsReader implements KeyInputsReaderInterface {
    public MockKeyInputsReader() throws JSONException {
        JSONObject emptyJson = new JSONObject();
        emptyJson.put(KeyInputsStorage.INPUTS_ARRAY_NAME, new JSONArray());
        mInput = emptyJson.toString();
    }

    @Override
    public String read() throws IOException {
        return mInput;
    }

    public void setInput(String input) {
        mInput = input;
    }

    private String mInput;
}
