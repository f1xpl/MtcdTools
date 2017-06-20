package android.microntek.f1x.mtcdtools.named.objects.actions;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.microntek.f1x.mtcdtools.named.NamedObjectId;

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
 * Created by f1x on 2017-01-13.
 */

@PrepareForTest(LaunchAction.class)
@RunWith(PowerMockRunner.class)
public class LaunchActionTest {

    @Before
    public void init() throws JSONException {
        initMocks(this);

        mActionId = new NamedObjectId("TestLaunchAction");

        mActionJson = new JSONObject();
        mActionJson.put(LaunchAction.NAME_PROPERTY, mActionId.toString());
        mActionJson.put(LaunchAction.OBJECT_TYPE_PROPERTY, LaunchAction.OBJECT_TYPE);
        mActionJson.put(LaunchAction.PACKAGE_NAME_PROPERTY, "com.test.package.to.launch");
    }

    @Test
    public void test_construct() throws JSONException {
        LaunchAction launchAction = new LaunchAction(mActionId, mActionJson.getString(LaunchAction.PACKAGE_NAME_PROPERTY));
        assertEquals(mActionJson.toString(), launchAction.toJson().toString());
        assertEquals(mActionId, launchAction.getId());
        assertEquals(mActionJson.getString(LaunchAction.OBJECT_TYPE_PROPERTY), launchAction.getObjectType());
        assertEquals(mActionJson.getString(LaunchAction.PACKAGE_NAME_PROPERTY), launchAction.getPackageName());
    }

    @Test
    public void test_evaluate() throws JSONException {
        Mockito.when(mMockContext.getPackageManager()).thenReturn(mMockPackageManager);
        Mockito.when(mMockPackageManager.getLaunchIntentForPackage(mActionJson.getString(LaunchAction.PACKAGE_NAME_PROPERTY))).thenReturn(mMockLaunchIntent);

        LaunchAction launchAction = new LaunchAction(mActionJson);
        launchAction.evaluate(mMockContext);

        Mockito.verify(mMockLaunchIntent, times(1)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Mockito.verify(mMockContext, times(1)).startActivity(mMockLaunchIntent);
    }

    @Test
    public void test_evaluate_nullIntent() throws JSONException {
        Mockito.when(mMockContext.getPackageManager()).thenReturn(mMockPackageManager);
        Mockito.when(mMockPackageManager.getLaunchIntentForPackage(mActionJson.getString(LaunchAction.PACKAGE_NAME_PROPERTY))).thenReturn(null);

        LaunchAction launchAction = new LaunchAction(mActionJson);
        launchAction.evaluate(mMockContext);

        Mockito.verify(mMockContext, never()).startActivity(null);
    }

    @Test
    public void test_toJson() throws JSONException {
        LaunchAction launchAction = new LaunchAction(mActionJson);
        assertEquals(launchAction.toJson().toString(), mActionJson.toString());
        assertEquals(mActionId, launchAction.getId());
        assertEquals(mActionJson.getString(LaunchAction.OBJECT_TYPE_PROPERTY), launchAction.getObjectType());
        assertEquals(mActionJson.getString(LaunchAction.PACKAGE_NAME_PROPERTY), launchAction.getPackageName());
    }

    private NamedObjectId mActionId;

    @Mock
    Context mMockContext;

    @Mock
    PackageManager mMockPackageManager;

    @Mock
    Intent mMockLaunchIntent;

    private JSONObject mActionJson;
}
