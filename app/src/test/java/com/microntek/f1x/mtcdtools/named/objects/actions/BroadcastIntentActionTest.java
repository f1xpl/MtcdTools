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
@PrepareForTest({BroadcastIntentAction.class, UriParser.class, ExtrasParser.class})
@RunWith(PowerMockRunner.class)

public class BroadcastIntentActionTest {
    @Before
    public void init() throws Exception {
        initMocks(this);
        PowerMockito.mockStatic(ExtrasParser.class);

        mActionId = new NamedObjectId("TestBroadcastIntentAction");

        mActionJson = new JSONObject();
        mActionJson.put(BroadcastIntentAction.NAME_PROPERTY, mActionId.toString());
        mActionJson.put(BroadcastIntentAction.OBJECT_TYPE_PROPERTY, BroadcastIntentAction.OBJECT_TYPE);
        mActionJson.put(BroadcastIntentAction.INTENT_PACKAGE_PROPERTY, "com.test.package");
        mActionJson.put(BroadcastIntentAction.INTENT_CATEGORY_PROPERTY, "intentCategory");
        mActionJson.put(BroadcastIntentAction.INTENT_TYPE_PROPERTY, "intentType");
        mActionJson.put(BroadcastIntentAction.INTENT_DATA_PROPERTY, "intentData");
        mActionJson.put(BroadcastIntentAction.INTENT_ACTION_PROPERTY, "intentAction");
        mActionJson.put(BroadcastIntentAction.PERMISSIONS_PROPERTY, "permissionsProp");
        mActionJson.put(BroadcastIntentAction.INTENT_EXTRAS_PROPERTY, new JSONObject());
    }

    @Test
    public void test_construct() throws JSONException {
        PowerMockito.when(ExtrasParser.fromJSON(any(JSONObject.class))).thenReturn(mMockBundle);
        JSONObject intentExtras = new JSONObject();
        PowerMockito.when(ExtrasParser.toJSON(mMockBundle)).thenReturn(intentExtras);

        BroadcastIntentAction broadcastIntentAction = new BroadcastIntentAction(mActionId,
                                                                                mActionJson.getString(BroadcastIntentAction.INTENT_PACKAGE_PROPERTY),
                                                                                mActionJson.getString(BroadcastIntentAction.INTENT_ACTION_PROPERTY),
                                                                                mActionJson.getString(BroadcastIntentAction.INTENT_CATEGORY_PROPERTY),
                                                                                mActionJson.getString(BroadcastIntentAction.INTENT_DATA_PROPERTY),
                                                                                mActionJson.getString(BroadcastIntentAction.INTENT_TYPE_PROPERTY),
                                                                                intentExtras,
                                                                                mActionJson.getString(BroadcastIntentAction.PERMISSIONS_PROPERTY));

        assertEquals(mActionJson.toString(), broadcastIntentAction.toJson().toString());
        assertEquals(mActionId, broadcastIntentAction.getId());
        assertEquals(mActionJson.getString(BroadcastIntentAction.OBJECT_TYPE_PROPERTY), broadcastIntentAction.getObjectType());
        assertEquals(mActionJson.getString(BroadcastIntentAction.INTENT_PACKAGE_PROPERTY), broadcastIntentAction.getIntentPackage());
        assertEquals(mActionJson.getString(BroadcastIntentAction.INTENT_ACTION_PROPERTY), broadcastIntentAction.getIntentAction());
        assertEquals(mActionJson.getString(BroadcastIntentAction.INTENT_CATEGORY_PROPERTY), broadcastIntentAction.getIntentCategory());
        assertEquals(mActionJson.getString(BroadcastIntentAction.INTENT_DATA_PROPERTY), broadcastIntentAction.getIntentData());
        assertEquals(mActionJson.getString(BroadcastIntentAction.INTENT_TYPE_PROPERTY), broadcastIntentAction.getIntentType());
        assertEquals(mActionJson.getString(BroadcastIntentAction.PERMISSIONS_PROPERTY), broadcastIntentAction.getPermissions());
        assertEquals(intentExtras.toString(), broadcastIntentAction.getIntentExtras().toString());
    }

    @Test
    public void test_evaluate() throws Exception {
        PowerMockito.whenNew(Intent.class).withAnyArguments().thenReturn(mBroadcastIntent);

        PowerMockito.mockStatic(UriParser.class);
        PowerMockito.when(UriParser.fromString(mActionJson.getString(BroadcastIntentAction.INTENT_DATA_PROPERTY))).thenReturn(mMockUri);
        PowerMockito.when(ExtrasParser.fromJSON(any(JSONObject.class))).thenReturn(mMockBundle);

        BroadcastIntentAction broadcastIntentAction = new BroadcastIntentAction(mActionJson);
        broadcastIntentAction.evaluate(mMockContext);

        verify(mMockContext).sendOrderedBroadcast(mBroadcastIntent, mActionJson.getString(BroadcastIntentAction.PERMISSIONS_PROPERTY));
        verify(mBroadcastIntent).setType(mActionJson.getString(BroadcastIntentAction.INTENT_TYPE_PROPERTY));
        verify(mBroadcastIntent).setData(mMockUri);
        verify(mBroadcastIntent).addCategory(mActionJson.getString(BroadcastIntentAction.INTENT_CATEGORY_PROPERTY));
        verify(mBroadcastIntent).setPackage(mActionJson.getString(BroadcastIntentAction.INTENT_PACKAGE_PROPERTY));
        verify(mBroadcastIntent).setAction(mActionJson.getString(BroadcastIntentAction.INTENT_ACTION_PROPERTY));
        verify(mBroadcastIntent).putExtras(mMockBundle);
    }

    @Test
    public void test_evaluate_empty_permissions() throws Exception {
        PowerMockito.whenNew(Intent.class).withAnyArguments().thenReturn(mBroadcastIntent);

        PowerMockito.mockStatic(UriParser.class);
        PowerMockito.when(UriParser.fromString(any(String.class))).thenReturn(mMockUri);
        PowerMockito.when(ExtrasParser.fromJSON(any(JSONObject.class))).thenReturn(mMockBundle);

        BroadcastIntentAction broadcastIntentAction = new BroadcastIntentAction(mActionId, "com.package", "intentAction", "intentCategory", "intentData", "intentType", new JSONObject(), "");
        broadcastIntentAction.evaluate(mMockContext);

        verify(mMockContext).sendBroadcast(mBroadcastIntent);
    }

    @Test
    public void test_evaluate_empty_parameters() throws Exception {
        PowerMockito.when(ExtrasParser.fromJSON(any(JSONObject.class))).thenReturn(mMockBundle);
        PowerMockito.when(mMockBundle.isEmpty()).thenReturn(true);

        BroadcastIntentAction broadcastIntentAction = new BroadcastIntentAction(mActionId, "", "", "", "", "", new JSONObject(), "");
        broadcastIntentAction.evaluate(mMockContext);

        verify(mBroadcastIntent, times(0)).setType(any(String.class));
        verify(mBroadcastIntent, times(0)).setData(any(Uri.class));
        verify(mBroadcastIntent, times(0)).addCategory(any(String.class));
        verify(mBroadcastIntent, times(0)).setPackage(any(String.class));
        verify(mBroadcastIntent, times(0)).setAction(any(String.class));
        verify(mBroadcastIntent, times(0)).putExtras(any(Bundle.class));
    }

    @Test
    public void test_toJson() throws JSONException {
        PowerMockito.when(ExtrasParser.fromJSON(any(JSONObject.class))).thenReturn(mMockBundle);
        JSONObject intentExtras = new JSONObject();
        PowerMockito.when(ExtrasParser.toJSON(mMockBundle)).thenReturn(intentExtras);

        BroadcastIntentAction broadcastIntentAction = new BroadcastIntentAction(mActionJson);
        assertEquals(mActionJson.toString(), broadcastIntentAction.toJson().toString());
        assertEquals(mActionId, broadcastIntentAction.getId());
        assertEquals(mActionJson.getString(BroadcastIntentAction.OBJECT_TYPE_PROPERTY), broadcastIntentAction.getObjectType());
        assertEquals(mActionJson.getString(BroadcastIntentAction.INTENT_PACKAGE_PROPERTY), broadcastIntentAction.getIntentPackage());
        assertEquals(mActionJson.getString(BroadcastIntentAction.INTENT_ACTION_PROPERTY), broadcastIntentAction.getIntentAction());
        assertEquals(mActionJson.getString(BroadcastIntentAction.INTENT_CATEGORY_PROPERTY), broadcastIntentAction.getIntentCategory());
        assertEquals(mActionJson.getString(BroadcastIntentAction.INTENT_DATA_PROPERTY), broadcastIntentAction.getIntentData());
        assertEquals(mActionJson.getString(BroadcastIntentAction.INTENT_TYPE_PROPERTY), broadcastIntentAction.getIntentType());
        assertEquals(mActionJson.getString(BroadcastIntentAction.PERMISSIONS_PROPERTY), broadcastIntentAction.getPermissions());
        assertEquals(intentExtras.toString(), broadcastIntentAction.getIntentExtras().toString());
    }

    private NamedObjectId mActionId;

    @Mock
    Context mMockContext;

    @Mock
    Intent mBroadcastIntent;

    @Mock
    Uri mMockUri;

    @Mock
    Bundle mMockBundle;

    private JSONObject mActionJson;
}
