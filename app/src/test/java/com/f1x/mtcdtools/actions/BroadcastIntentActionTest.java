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
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by COMPUTER on 2017-01-13.
 */

@PowerMockIgnore({ "org.mockito.*", "org.robolectric.*", "android.*" })
@PrepareForTest({BroadcastIntentAction.class, UriParser.class, ExtrasParser.class})
@RunWith(PowerMockRunner.class)

public class BroadcastIntentActionTest {
    @Before
    public void init() throws Exception {
        initMocks(this);
        PowerMockito.mockStatic(ExtrasParser.class);

        mActionJson = new JSONObject();
        mActionJson.put(BroadcastIntentAction.NAME_PROPERTY, "TestBroadcastIntentAction");
        mActionJson.put(BroadcastIntentAction.TYPE_PROPERTY, BroadcastIntentAction.ACTION_TYPE);
        mActionJson.put(BroadcastIntentAction.INTENT_PACKAGE_PROPERTY, "com.test.package");
        mActionJson.put(BroadcastIntentAction.INTENT_CATEGORY_PROPERTY, "intentCategory");
        mActionJson.put(BroadcastIntentAction.INTENT_TYPE_PROPERTY, "intentType");
        mActionJson.put(BroadcastIntentAction.INTENT_DATA_PROPERTY, "intentData");
        mActionJson.put(BroadcastIntentAction.INTENT_ACTION_PROPERTY, "intentAction");
        mActionJson.put(BroadcastIntentAction.PERMISSIONS_PROPERTY, "permissionsProp");
        mActionJson.put(BroadcastIntentAction.INTENT_EXTRAS_PROPERTY, new JSONObject());
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
        verify(mBroadcastIntent).setDataAndType(mMockUri, mActionJson.getString(BroadcastIntentAction.INTENT_TYPE_PROPERTY));
        verify(mBroadcastIntent).addCategory(mActionJson.getString(BroadcastIntentAction.INTENT_CATEGORY_PROPERTY));
        verify(mBroadcastIntent).setPackage(mActionJson.getString(BroadcastIntentAction.INTENT_PACKAGE_PROPERTY));
        verify(mBroadcastIntent).setAction(mActionJson.getString(BroadcastIntentAction.INTENT_ACTION_PROPERTY));
        verify(mBroadcastIntent).putExtras(mMockBundle);
    }

    @Test
    public void test_toJson() throws JSONException {
        PowerMockito.when(ExtrasParser.fromJSON(any(JSONObject.class))).thenReturn(mMockBundle);
        PowerMockito.when(ExtrasParser.toJSON(mMockBundle)).thenReturn(new JSONObject());

        BroadcastIntentAction broadcastIntentAction = new BroadcastIntentAction(mActionJson);
        assertEquals(mActionJson.toString(), broadcastIntentAction.toJson().toString());
    }

    @Mock
    Context mMockContext;

    @Mock
    Intent mBroadcastIntent;

    @Mock
    Uri mMockUri;

    @Mock
    Bundle mMockBundle;

    JSONObject mActionJson;
}
