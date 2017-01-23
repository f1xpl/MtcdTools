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
@PrepareForTest({StartActivityAction.class, UriParser.class, ExtrasParser.class})
@RunWith(PowerMockRunner.class)

public class StartActivityActionTest {
    @Before
    public void init() throws Exception {
        initMocks(this);
        PowerMockito.mockStatic(ExtrasParser.class);

        mActionJson = new JSONObject();
        mActionJson.put(StartActivityAction.NAME_PROPERTY, "TestStartActivityAction");
        mActionJson.put(StartActivityAction.TYPE_PROPERTY, StartActivityAction.ACTION_TYPE);
        mActionJson.put(BroadcastIntentAction.INTENT_PACKAGE_PROPERTY, "com.test.package");
        mActionJson.put(StartActivityAction.INTENT_CATEGORY_PROPERTY, "intentCategory");
        mActionJson.put(StartActivityAction.INTENT_TYPE_PROPERTY, "intentType");
        mActionJson.put(StartActivityAction.INTENT_DATA_PROPERTY, "intentData");
        mActionJson.put(StartActivityAction.INTENT_ACTION_PROPERTY, "intentAction");
        mActionJson.put(StartActivityAction.INTENT_EXTRAS_PROPERTY, new JSONObject());
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
        verify(mMockStartActivityIntent).setDataAndType(mMockUri, mActionJson.getString(StartActivityAction.INTENT_TYPE_PROPERTY));
        verify(mMockStartActivityIntent).addCategory(mActionJson.getString(StartActivityAction.INTENT_CATEGORY_PROPERTY));
        verify(mMockStartActivityIntent).setAction(mActionJson.getString(StartActivityAction.INTENT_ACTION_PROPERTY));
        verify(mMockStartActivityIntent).setPackage(mActionJson.getString(BroadcastIntentAction.INTENT_PACKAGE_PROPERTY));
        verify(mMockStartActivityIntent).putExtras(mMockBundle);
    }

    @Test
    public void test_toJson() throws JSONException {
        PowerMockito.when(ExtrasParser.fromJSON(any(JSONObject.class))).thenReturn(mMockBundle);
        PowerMockito.when(ExtrasParser.toJSON(mMockBundle)).thenReturn(new JSONObject());

        StartActivityAction startActivityAction = new StartActivityAction(mActionJson);
        assertEquals(mActionJson.toString(), startActivityAction.toJson().toString());
    }

    @Mock
    Context mMockContext;

    @Mock
    Intent mMockStartActivityIntent;

    @Mock
    Uri mMockUri;

    @Mock
    Bundle mMockBundle;

    JSONObject mActionJson;
}
