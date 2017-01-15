package com.f1x.mtcdtools.actions;

import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.mockito.Mock;

import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static junit.framework.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by COMPUTER on 2017-01-13.
 */

@PowerMockIgnore({ "org.mockito.*", "org.robolectric.*", "android.*" })
@PrepareForTest(ExtrasParser.class)
@RunWith(PowerMockRunner.class)

public class ExtrasParserTest {
    @Before
    public void init() throws JSONException{
        initMocks(this);
        mTestJson = new JSONObject();
        mTestJson.put(JSON_INTEGER_KEY, 1);
        mTestJson.put(JSON_LONG_KEY, Long.valueOf(2));
        mTestJson.put(JSON_STRING_KEY, "3");
        mTestJson.put(JSON_BOOLEAN_KEY, true);
        mTestJson.put(JSON_DOUBLE_KEY, 7.89);
    }

    @Test
    public void test_conversion_fromJson() throws Exception {
        PowerMockito.whenNew(Bundle.class).withAnyArguments().thenReturn(mMockBundle);

        ExtrasParser.fromJSON(mTestJson);
        Mockito.verify(mMockBundle, Mockito.times(1)).putInt(JSON_INTEGER_KEY, mTestJson.getInt(JSON_INTEGER_KEY));
        Mockito.verify(mMockBundle, Mockito.times(1)).putLong(JSON_LONG_KEY, mTestJson.getLong(JSON_LONG_KEY));
        Mockito.verify(mMockBundle, Mockito.times(1)).putString(JSON_STRING_KEY, mTestJson.getString(JSON_STRING_KEY));
        Mockito.verify(mMockBundle, Mockito.times(1)).putBoolean(JSON_BOOLEAN_KEY, mTestJson.getBoolean(JSON_BOOLEAN_KEY));
        Mockito.verify(mMockBundle, Mockito.times(1)).putDouble(JSON_DOUBLE_KEY, mTestJson.getDouble(JSON_DOUBLE_KEY));
    }

    @Test
    public void test_conversion_fromBundle() throws JSONException {
        Set<String> keys = new HashSet<>(Arrays.asList(JSON_INTEGER_KEY, JSON_LONG_KEY, JSON_STRING_KEY, JSON_BOOLEAN_KEY, JSON_DOUBLE_KEY));
        Mockito.doReturn(keys).when(mMockBundle).keySet();

        Mockito.doReturn(mTestJson.getInt(JSON_INTEGER_KEY)).when(mMockBundle).get(JSON_INTEGER_KEY);
        Mockito.doReturn(mTestJson.getLong(JSON_LONG_KEY)).when(mMockBundle).get(JSON_LONG_KEY);
        Mockito.doReturn(mTestJson.getString(JSON_STRING_KEY)).when(mMockBundle).get(JSON_STRING_KEY);
        Mockito.doReturn(mTestJson.getBoolean(JSON_BOOLEAN_KEY)).when(mMockBundle).get(JSON_BOOLEAN_KEY);
        Mockito.doReturn(mTestJson.getDouble(JSON_DOUBLE_KEY)).when(mMockBundle).get(JSON_DOUBLE_KEY);

        JSONObject json = ExtrasParser.toJSON(mMockBundle);
        assertEquals(mTestJson.toString(), json.toString());
    }

    @Mock
    Bundle mMockBundle;
    JSONObject mTestJson;

    private static final String JSON_INTEGER_KEY = "integer1";
    private static final String JSON_LONG_KEY = "long2";
    private static final String JSON_STRING_KEY = "string3";
    private static final String JSON_BOOLEAN_KEY = "boolean4";
    private static final String JSON_DOUBLE_KEY = "double5";
}
