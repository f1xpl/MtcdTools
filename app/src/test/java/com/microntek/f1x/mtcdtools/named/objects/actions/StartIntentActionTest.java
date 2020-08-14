package com.microntek.f1x.mtcdtools.named.objects.actions;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.microntek.f1x.mtcdtools.named.NamedObjectId;

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
 * Created by f1x on 2017-01-13.
 */

@PowerMockIgnore({ "org.mockito.*", "org.robolectric.*", "android.*" })
@PrepareForTest({StartIntentAction.class, UriParser.class, ExtrasParser.class})
@RunWith(PowerMockRunner.class)

public class StartIntentActionTest {
    @Before
    public void init() throws Exception {
        initMocks(this);
        PowerMockito.mockStatic(ExtrasParser.class);

        mActionId = new NamedObjectId("TestStartActivityAction");

        mActionJson = new JSONObject();
        mActionJson.put(StartIntentAction.NAME_PROPERTY, mActionId.toString());
        mActionJson.put(StartIntentAction.OBJECT_TYPE_PROPERTY, StartIntentAction.OBJECT_TYPE);
        mActionJson.put(StartIntentAction.INTENT_PACKAGE_PROPERTY, "com.test.package");
        mActionJson.put(StartIntentAction.INTENT_CATEGORY_PROPERTY, "intentCategory");
        mActionJson.put(StartIntentAction.INTENT_TYPE_PROPERTY, "intentType");
        mActionJson.put(StartIntentAction.INTENT_DATA_PROPERTY, "intentData");
        mActionJson.put(StartIntentAction.INTENT_ACTION_PROPERTY, "intentAction");
        mActionJson.put(StartIntentAction.INTENT_EXTRAS_PROPERTY, new JSONObject());
        mActionJson.put(StartIntentAction.CLASS_NAME_PROPERTY, "className");
        mActionJson.put(StartIntentAction.FLAGS_PROPERTY, 554);
        mActionJson.put(StartIntentAction.TARGET_PROPERTY, StartIntentAction.TARGET_ACTIVITY);
    }

    @Test
    public void test_construct() throws JSONException {
        PowerMockito.when(ExtrasParser.fromJSON(any(JSONObject.class))).thenReturn(mMockBundle);
        PowerMockito.when(ExtrasParser.toJSON(mMockBundle)).thenReturn(new JSONObject());
        JSONObject intentExtras = new JSONObject();

        StartIntentAction startIntentAction = new StartIntentAction(mActionId,
                                                                          mActionJson.getString(StartIntentAction.INTENT_PACKAGE_PROPERTY),
                                                                          mActionJson.getString(StartIntentAction.INTENT_ACTION_PROPERTY),
                                                                          mActionJson.getString(StartIntentAction.INTENT_CATEGORY_PROPERTY),
                                                                          mActionJson.getString(StartIntentAction.INTENT_DATA_PROPERTY),
                                                                          mActionJson.getString(StartIntentAction.INTENT_TYPE_PROPERTY),
                                                                          intentExtras,
                                                                          mActionJson.getString(StartIntentAction.CLASS_NAME_PROPERTY),
                                                                          mActionJson.getInt(StartIntentAction.FLAGS_PROPERTY),
                                                                          mActionJson.getInt(StartIntentAction.TARGET_PROPERTY));

        assertEquals(mActionJson.toString(), startIntentAction.toJson().toString());
        assertEquals(mActionId, startIntentAction.getId());
        assertEquals(mActionJson.getString(StartIntentAction.OBJECT_TYPE_PROPERTY), startIntentAction.getObjectType());
        assertEquals(mActionJson.getString(StartIntentAction.INTENT_PACKAGE_PROPERTY), startIntentAction.getIntentPackage());
        assertEquals(mActionJson.getString(StartIntentAction.INTENT_ACTION_PROPERTY), startIntentAction.getIntentAction());
        assertEquals(mActionJson.getString(StartIntentAction.INTENT_CATEGORY_PROPERTY), startIntentAction.getIntentCategory());
        assertEquals(mActionJson.getString(StartIntentAction.INTENT_DATA_PROPERTY), startIntentAction.getIntentData());
        assertEquals(mActionJson.getString(StartIntentAction.INTENT_TYPE_PROPERTY), startIntentAction.getIntentType());
        assertEquals(intentExtras.toString(), startIntentAction.getIntentExtras().toString());
        assertEquals(mActionJson.getString(StartIntentAction.CLASS_NAME_PROPERTY), startIntentAction.getClassName());
        assertEquals(mActionJson.getInt(StartIntentAction.FLAGS_PROPERTY), startIntentAction.getFlags());
        assertEquals(mActionJson.getInt(StartIntentAction.TARGET_PROPERTY), startIntentAction.getTarget());
    }

    @Test
    public void test_evaluate_target_activity() throws Exception {
        PowerMockito.whenNew(Intent.class).withAnyArguments().thenReturn(mMockStartActivityIntent);

        PowerMockito.mockStatic(UriParser.class);
        PowerMockito.when(UriParser.fromString(mActionJson.getString(StartIntentAction.INTENT_DATA_PROPERTY))).thenReturn(mMockUri);
        PowerMockito.when(ExtrasParser.fromJSON(any(JSONObject.class))).thenReturn(mMockBundle);

        StartIntentAction startIntentAction = new StartIntentAction(mActionJson);
        startIntentAction.evaluate(mMockContext);

        verify(mMockContext).startActivity(mMockStartActivityIntent);
        verify(mMockStartActivityIntent).setType(mActionJson.getString(StartIntentAction.INTENT_TYPE_PROPERTY));
        verify(mMockStartActivityIntent).setData(mMockUri);
        verify(mMockStartActivityIntent).addCategory(mActionJson.getString(StartIntentAction.INTENT_CATEGORY_PROPERTY));
        verify(mMockStartActivityIntent).setAction(mActionJson.getString(StartIntentAction.INTENT_ACTION_PROPERTY));
        verify(mMockStartActivityIntent).setPackage(mActionJson.getString(StartIntentAction.INTENT_PACKAGE_PROPERTY));
        verify(mMockStartActivityIntent).putExtras(mMockBundle);
        verify(mMockStartActivityIntent).setClassName(mActionJson.getString(StartIntentAction.INTENT_PACKAGE_PROPERTY), mActionJson.getString(StartIntentAction.CLASS_NAME_PROPERTY));
        verify(mMockStartActivityIntent).setFlags(mActionJson.getInt(StartIntentAction.FLAGS_PROPERTY));
    }

    @Test
    public void test_evaluate_target_service() throws Exception {
        PowerMockito.whenNew(Intent.class).withAnyArguments().thenReturn(mMockStartActivityIntent);

        PowerMockito.mockStatic(UriParser.class);
        PowerMockito.when(UriParser.fromString(mActionJson.getString(StartIntentAction.INTENT_DATA_PROPERTY))).thenReturn(mMockUri);
        PowerMockito.when(ExtrasParser.fromJSON(any(JSONObject.class))).thenReturn(mMockBundle);

        mActionJson.remove(StartIntentAction.TARGET_PROPERTY);
        mActionJson.put(StartIntentAction.TARGET_PROPERTY, StartIntentAction.TARGET_SERVICE);

        StartIntentAction startIntentAction = new StartIntentAction(mActionJson);
        startIntentAction.evaluate(mMockContext);

        verify(mMockContext).startService(mMockStartActivityIntent);
        verify(mMockStartActivityIntent).setType(mActionJson.getString(StartIntentAction.INTENT_TYPE_PROPERTY));
        verify(mMockStartActivityIntent).setData(mMockUri);
        verify(mMockStartActivityIntent).addCategory(mActionJson.getString(StartIntentAction.INTENT_CATEGORY_PROPERTY));
        verify(mMockStartActivityIntent).setAction(mActionJson.getString(StartIntentAction.INTENT_ACTION_PROPERTY));
        verify(mMockStartActivityIntent).setPackage(mActionJson.getString(StartIntentAction.INTENT_PACKAGE_PROPERTY));
        verify(mMockStartActivityIntent).putExtras(mMockBundle);
        verify(mMockStartActivityIntent).setClassName(mActionJson.getString(StartIntentAction.INTENT_PACKAGE_PROPERTY), mActionJson.getString(StartIntentAction.CLASS_NAME_PROPERTY));
        verify(mMockStartActivityIntent).setFlags(mActionJson.getInt(StartIntentAction.FLAGS_PROPERTY));
    }

    @Test
    public void test_toJson() throws JSONException {
        PowerMockito.when(ExtrasParser.fromJSON(any(JSONObject.class))).thenReturn(mMockBundle);
        JSONObject intentExtras = new JSONObject();
        PowerMockito.when(ExtrasParser.toJSON(mMockBundle)).thenReturn(intentExtras);

        StartIntentAction startIntentAction = new StartIntentAction(mActionJson);
        assertEquals(mActionJson.toString(), startIntentAction.toJson().toString());
        assertEquals(mActionId, startIntentAction.getId());
        assertEquals(mActionJson.getString(StartIntentAction.OBJECT_TYPE_PROPERTY), startIntentAction.getObjectType());
        assertEquals(mActionJson.getString(StartIntentAction.INTENT_PACKAGE_PROPERTY), startIntentAction.getIntentPackage());
        assertEquals(mActionJson.getString(StartIntentAction.INTENT_ACTION_PROPERTY), startIntentAction.getIntentAction());
        assertEquals(mActionJson.getString(StartIntentAction.INTENT_CATEGORY_PROPERTY), startIntentAction.getIntentCategory());
        assertEquals(mActionJson.getString(StartIntentAction.INTENT_DATA_PROPERTY), startIntentAction.getIntentData());
        assertEquals(mActionJson.getString(StartIntentAction.INTENT_TYPE_PROPERTY), startIntentAction.getIntentType());
        assertEquals(intentExtras.toString(), startIntentAction.getIntentExtras().toString());
        assertEquals(mActionJson.getString(StartIntentAction.CLASS_NAME_PROPERTY), startIntentAction.getClassName());
        assertEquals(mActionJson.getInt(StartIntentAction.FLAGS_PROPERTY), startIntentAction.getFlags());
    }

    @Test
    public void test_evaluate_empty_parameters() throws Exception {
        PowerMockito.whenNew(Intent.class).withAnyArguments().thenReturn(mMockStartActivityIntent);
        PowerMockito.when(ExtrasParser.fromJSON(any(JSONObject.class))).thenReturn(mMockBundle);
        PowerMockito.when(mMockBundle.isEmpty()).thenReturn(true);

        StartIntentAction startIntentAction = new StartIntentAction(mActionId, "", "", "", "", "", new JSONObject(), "", 5, StartIntentAction.TARGET_ACTIVITY);
        startIntentAction.evaluate(mMockContext);

        verify(mMockStartActivityIntent, times(0)).setType(any(String.class));
        verify(mMockStartActivityIntent, times(0)).setData(any(Uri.class));
        verify(mMockStartActivityIntent, times(0)).addCategory(any(String.class));
        verify(mMockStartActivityIntent, times(0)).setPackage(any(String.class));
        verify(mMockStartActivityIntent, times(0)).setAction(any(String.class));
        verify(mMockStartActivityIntent, times(0)).putExtras(any(Bundle.class));
        verify(mMockStartActivityIntent, times(0)).setClassName(any(String.class), any(String.class));
        verify(mMockStartActivityIntent, times(1)).setFlags(5);
    }

    private NamedObjectId mActionId;

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
