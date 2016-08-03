package com.f1x.mtcdtools;

import com.f1x.mtcdtools.keys.input.KeyInput;
import com.f1x.mtcdtools.keys.input.KeyInputType;

import org.json.JSONObject;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by COMPUTER on 2016-08-01.
 */
public class KeyInputTest {
    public KeyInputTest() {
        mKeyCode = 123;
        mInputType = KeyInputType.LAUNCH;
        mCommand = "com.android.test";
    }

    @Test
    public void toJson_isCorrect() throws Exception {
        KeyInput input = new KeyInput(mKeyCode, mInputType, mCommand);

        JSONObject expectedJson = new JSONObject(String.format(Locale.ENGLISH,
                                                 "{\"%s\":%d, \"%s\":\"%s\",\"%s\":\"%s\"}",
                                                 KeyInput.KEY_CODE_NAME, mKeyCode,
                                                 KeyInput.TYPE_NAME, KeyInputType.toString(mInputType),
                                                 KeyInput.PARAMETER_NAME, mCommand));
        assertEquals(input.toJson().toString(), expectedJson.toString());
    }

    @Test
    public void fromJson_isCorrect() throws Exception {
        JSONObject inputJson = new JSONObject(String.format(Locale.ENGLISH,
                                                "{\"%s\":%d, \"%s\":\"%s\",\"%s\":\"%s\"}",
                                                KeyInput.KEY_CODE_NAME, mKeyCode,
                                                KeyInput.TYPE_NAME, KeyInputType.toString(mInputType),
                                                KeyInput.PARAMETER_NAME, mCommand));

        KeyInput keyInput = new KeyInput(inputJson);
        assertEquals(keyInput.getKeyCode(), mKeyCode);
        assertEquals(keyInput.getType(), mInputType);
        assertEquals(keyInput.getParameter(), mCommand);
    }

    @Test
    public void testEquals() {
        KeyInput input = new KeyInput(mKeyCode, mInputType, mCommand);
        assertEquals(input, input);

        KeyInput input2 = new KeyInput(mKeyCode, mInputType, mCommand);
        assertEquals(input, input2);

        KeyInput input3 = new KeyInput(mKeyCode + 1, mInputType, mCommand);
        assertNotEquals(input, input3);

        KeyInput input4 = new KeyInput(mKeyCode, KeyInputType.NEXT, mCommand);
        assertNotEquals(input, input4);

        KeyInput input5 = new KeyInput(mKeyCode, mInputType, "com.test.notequal");
        assertNotEquals(input, input5);
    }

    private final int mKeyCode;
    private final KeyInputType mInputType;
    private final String mCommand;
}
