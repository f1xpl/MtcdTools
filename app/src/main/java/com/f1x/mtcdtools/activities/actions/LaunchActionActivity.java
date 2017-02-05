package com.f1x.mtcdtools.activities.actions;

import android.widget.Spinner;
import android.widget.Toast;

import com.f1x.mtcdtools.R;
import com.f1x.mtcdtools.actions.Action;
import com.f1x.mtcdtools.actions.LaunchAction;
import com.f1x.mtcdtools.activities.NamedObjectActivity;
import com.f1x.mtcdtools.adapters.InstalledPackagesArrayAdapter;
import com.f1x.mtcdtools.adapters.PackageEntry;
import com.f1x.mtcdtools.storage.NamedObject;

public class LaunchActionActivity extends NamedObjectActivity {
    public LaunchActionActivity() {
        super(R.layout.activity_launch_action_details);
    }

    @Override
    protected void initControls() {
        super.initControls();

        mInstalledPackagesSpinner = (Spinner)this.findViewById(R.id.spinnerApplications);
        mInstalledPackagesArrayAdapter = new InstalledPackagesArrayAdapter(this);
        mInstalledPackagesSpinner.setAdapter(mInstalledPackagesArrayAdapter);
    }

    @Override
    protected Action createNamedObject(String namedObjectName) {
        PackageEntry packageEntry = (PackageEntry)mInstalledPackagesSpinner.getSelectedItem();
        return new LaunchAction(namedObjectName, packageEntry.getName());
    }

    @Override
    protected void fillControls(NamedObject namedObject) throws ClassCastException {
        super.fillControls(namedObject);

        LaunchAction launchAction = (LaunchAction) namedObject;
        int packagePosition = mInstalledPackagesArrayAdapter.getPosition(launchAction.getPackageName());
        mInstalledPackagesSpinner.setSelection(packagePosition);
    }

    private Spinner mInstalledPackagesSpinner;
    private InstalledPackagesArrayAdapter mInstalledPackagesArrayAdapter;
}
