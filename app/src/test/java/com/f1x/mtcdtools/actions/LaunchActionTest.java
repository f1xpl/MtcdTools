package com.f1x.mtcdtools.actions;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by COMPUTER on 2017-01-13.
 */

@PrepareForTest(LaunchAction.class)
@RunWith(PowerMockRunner.class)
public class LaunchActionTest {

    @Before
    public void init() throws JSONException {
        initMocks(this);

        mActionJson = new JSONObject();
        mActionJson.put(LaunchAction.NAME_PROPERTY, "TestLaunchAction");
        mActionJson.put(LaunchAction.TYPE_PROPERTY, LaunchAction.ACTION_TYPE);
        mActionJson.put(LaunchAction.PACKAGE_NAME_PROPERTY, "com.test.package.to.launch");
    }

    @Test
    public void test_evaluate() throws JSONException {
        Mockito.when(mMockContext.getPackageManager()).thenReturn(mMockPackageManager);
        Mockito.when(mMockPackageManager.getLaunchIntentForPackage(mActionJson.getString(LaunchAction.PACKAGE_NAME_PROPERTY))).thenReturn(mMockLaunchIntent);

        LaunchAction launchAction = new LaunchAction(mActionJson, mMockContext);
        launchAction.evaluate();

        Mockito.verify(mMockLaunchIntent, times(1)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Mockito.verify(mMockContext, times(1)).startActivity(mMockLaunchIntent);
    }

    @Test
    public void test_evaluate_nullIntent() throws JSONException {
        Mockito.when(mMockContext.getPackageManager()).thenReturn(mMockPackageManager);
        Mockito.when(mMockPackageManager.getLaunchIntentForPackage(mActionJson.getString(LaunchAction.PACKAGE_NAME_PROPERTY))).thenReturn(null);

        LaunchAction launchAction = new LaunchAction(mActionJson, mMockContext);
        launchAction.evaluate();

        Mockito.verify(mMockContext, never()).startActivity(null);
    }

    @Test
    public void test_toJson() throws JSONException {
        LaunchAction launchAction = new LaunchAction(mActionJson, mMockContext);
        assertEquals(launchAction.toJson().toString(), mActionJson.toString());
    }

    @Mock
    Context mMockContext;

    @Mock
    PackageManager mMockPackageManager;

    @Mock
    Intent mMockLaunchIntent;

    JSONObject mActionJson;
}
