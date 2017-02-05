package com.f1x.mtcdtools.actions;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by COMPUTER on 2017-01-13.
 */

@PowerMockIgnore({ "org.mockito.*", "org.robolectric.*", "android.*" })
@PrepareForTest({StartActivityAction.class, UriParser.class, ExtrasParser.class})
@RunWith(PowerMockRunner.class)

public class StartActivityActionTest {
    @Before
    public void init() throws Exception {
        initMocks(this);
        PowerMockito.mockStatic(ExtrasParser.class);

        mActionJson = new JSONObject();
        mActionJson.put(StartActivityAction.NAME_PROPERTY, "TestStartActivityAction");
        mActionJson.put(StartActivityAction.OBJECT_TYPE_PROPERTY, StartActivityAction.OBJECT_TYPE);
        mActionJson.put(StartActivityAction.INTENT_PACKAGE_PROPERTY, "com.test.package");
        mActionJson.put(StartActivityAction.INTENT_CATEGORY_PROPERTY, "intentCategory");
        mActionJson.put(StartActivityAction.INTENT_TYPE_PROPERTY, "intentType");
        mActionJson.put(StartActivityAction.INTENT_DATA_PROPERTY, "intentData");
        mActionJson.put(StartActivityAction.INTENT_ACTION_PROPERTY, "intentAction");
        mActionJson.put(StartActivityAction.INTENT_EXTRAS_PROPERTY, new JSONObject());
        mActionJson.put(StartActivityAction.CLASS_NAME_PROPERTY, "className");
        mActionJson.put(StartActivityAction.FLAGS_PROPERTY, 554);
    }

    @Test
    public void test_construct() throws JSONException {
        PowerMockito.when(ExtrasParser.fromJSON(any(JSONObject.class))).thenReturn(mMockBundle);
        PowerMockito.when(ExtrasParser.toJSON(mMockBundle)).thenReturn(new JSONObject());
        JSONObject intentExtras = new JSONObject();

        StartActivityAction startActivityAction = new StartActivityAction(mActionJson.getString(StartActivityAction.NAME_PROPERTY),
                                                                          mActionJson.getString(StartActivityAction.INTENT_PACKAGE_PROPERTY),
                                                                          mActionJson.getString(StartActivityAction.INTENT_ACTION_PROPERTY),
                                                                          mActionJson.getString(StartActivityAction.INTENT_CATEGORY_PROPERTY),
                                                                          mActionJson.getString(StartActivityAction.INTENT_DATA_PROPERTY),
                                                                          mActionJson.getString(StartActivityAction.INTENT_TYPE_PROPERTY),
                                                                          intentExtras,
                                                                          mActionJson.getString(StartActivityAction.CLASS_NAME_PROPERTY),
                                                                          mActionJson.getInt(StartActivityAction.FLAGS_PROPERTY));

        assertEquals(mActionJson.toString(), startActivityAction.toJson().toString());
        assertEquals(mActionJson.getString(StartActivityAction.NAME_PROPERTY), startActivityAction.getName());
        assertEquals(mActionJson.getString(StartActivityAction.OBJECT_TYPE_PROPERTY), startActivityAction.getObjectType());
        assertEquals(mActionJson.getString(StartActivityAction.INTENT_PACKAGE_PROPERTY), startActivityAction.getIntentPackage());
        assertEquals(mActionJson.getString(StartActivityAction.INTENT_ACTION_PROPERTY), startActivityAction.getIntentAction());
        assertEquals(mActionJson.getString(StartActivityAction.INTENT_CATEGORY_PROPERTY), startActivityAction.getIntentCategory());
        assertEquals(mActionJson.getString(StartActivityAction.INTENT_DATA_PROPERTY), startActivityAction.getIntentData());
        assertEquals(mActionJson.getString(StartActivityAction.INTENT_TYPE_PROPERTY), startActivityAction.getIntentType());
        assertEquals(intentExtras.toString(), startActivityAction.getIntentExtras().toString());
        assertEquals(mActionJson.getString(StartActivityAction.CLASS_NAME_PROPERTY), startActivityAction.getClassName());
        assertEquals(mActionJson.getInt(StartActivityAction.FLAGS_PROPERTY), startActivityAction.getFlags());
    }

    @Test
    public void test_evaluate() throws Exception {
        PowerMockito.whenNew(Intent.class).withAnyArguments().thenReturn(mMockStartActivityIntent);

        PowerMockito.mockStatic(UriParser.class);
        PowerMockito.when(UriParser.fromString(mActionJson.getString(StartActivityAction.INTENT_DATA_PROPERTY))).thenReturn(mMockUri);
        PowerMockito.when(ExtrasParser.fromJSON(any(JSONObject.class))).thenReturn(mMockBundle);

        StartActivityAction startActivityAction = new StartActivityAction(mActionJson);
        startActivityAction.evaluate(mMockContext);

        verify(mMockContext).startActivity(mMockStartActivityIntent);
        verify(mMockStartActivityIntent).setType(mActionJson.getString(StartActivityAction.INTENT_TYPE_PROPERTY));
        verify(mMockStartActivityIntent).setData(mMockUri);
        verify(mMockStartActivityIntent).addCategory(mActionJson.getString(StartActivityAction.INTENT_CATEGORY_PROPERTY));
        verify(mMockStartActivityIntent).setAction(mActionJson.getString(StartActivityAction.INTENT_ACTION_PROPERTY));
        verify(mMockStartActivityIntent).setPackage(mActionJson.getString(StartActivityAction.INTENT_PACKAGE_PROPERTY));
        verify(mMockStartActivityIntent).putExtras(mMockBundle);
        verify(mMockStartActivityIntent).setClassName(mActionJson.getString(StartActivityAction.INTENT_PACKAGE_PROPERTY), mActionJson.getString(StartActivityAction.CLASS_NAME_PROPERTY));
        verify(mMockStartActivityIntent).setFlags(mActionJson.getInt(StartActivityAction.FLAGS_PROPERTY));
    }

    @Test
    public void test_toJson() throws JSONException {
        PowerMockito.when(ExtrasParser.fromJSON(any(JSONObject.class))).thenReturn(mMockBundle);
        JSONObject intentExtras = new JSONObject();
        PowerMockito.when(ExtrasParser.toJSON(mMockBundle)).thenReturn(intentExtras);

        StartActivityAction startActivityAction = new StartActivityAction(mActionJson);
        assertEquals(mActionJson.toString(), startActivityAction.toJson().toString());
        assertEquals(mActionJson.getString(StartActivityAction.NAME_PROPERTY), startActivityAction.getName());
        assertEquals(mActionJson.getString(StartActivityAction.OBJECT_TYPE_PROPERTY), startActivityAction.getObjectType());
        assertEquals(mActionJson.getString(StartActivityAction.INTENT_PACKAGE_PROPERTY), startActivityAction.getIntentPackage());
        assertEquals(mActionJson.getString(StartActivityAction.INTENT_ACTION_PROPERTY), startActivityAction.getIntentAction());
        assertEquals(mActionJson.getString(StartActivityAction.INTENT_CATEGORY_PROPERTY), startActivityAction.getIntentCategory());
        assertEquals(mActionJson.getString(StartActivityAction.INTENT_DATA_PROPERTY), startActivityAction.getIntentData());
        assertEquals(mActionJson.getString(StartActivityAction.INTENT_TYPE_PROPERTY), startActivityAction.getIntentType());
        assertEquals(intentExtras.toString(), startActivityAction.getIntentExtras().toString());
        assertEquals(mActionJson.getString(StartActivityAction.CLASS_NAME_PROPERTY), startActivityAction.getClassName());
        assertEquals(mActionJson.getInt(StartActivityAction.FLAGS_PROPERTY), startActivityAction.getFlags());
    }

    @Test
    public void test_evaluate_empty_parameters() throws Exception {
        PowerMockito.whenNew(Intent.class).withAnyArguments().thenReturn(mMockStartActivityIntent);
        PowerMockito.when(ExtrasParser.fromJSON(any(JSONObject.class))).thenReturn(mMockBundle);
        PowerMockito.when(mMockBundle.isEmpty()).thenReturn(true);

        StartActivityAction startActivityAction = new StartActivityAction("testAction", "", "", "", "", "", new JSONObject(), "", 5);
        startActivityAction.evaluate(mMockContext);

        verify(mMockStartActivityIntent, times(0)).setType(any(String.class));
        verify(mMockStartActivityIntent, times(0)).setData(any(Uri.class));
        verify(mMockStartActivityIntent, times(0)).addCategory(any(String.class));
        verify(mMockStartActivityIntent, times(0)).setPackage(any(String.class));
        verify(mMockStartActivityIntent, times(0)).setAction(any(String.class));
        verify(mMockStartActivityIntent, times(0)).putExtras(any(Bundle.class));
        verify(mMockStartActivityIntent, times(0)).setClassName(any(String.class), any(String.class));
        verify(mMockStartActivityIntent, times(1)).setFlags(5);
    }

    @Mock
    Context mMockContext;

    @Mock
    Intent mMockStartActivityIntent;

    @Mock
    Uri mMockUri;

    @Mock
    Bundle mMockBundle;

    private JSONObject mActionJson;
}
