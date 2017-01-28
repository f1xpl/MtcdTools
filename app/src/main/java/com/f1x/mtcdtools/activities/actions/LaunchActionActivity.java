package com.f1x.mtcdtools.activities.actions;

import android.widget.Spinner;
import android.widget.Toast;

import com.f1x.mtcdtools.R;
import com.f1x.mtcdtools.actions.Action;
import com.f1x.mtcdtools.actions.LaunchAction;
import com.f1x.mtcdtools.adapters.InstalledPackagesArrayAdapter;
import com.f1x.mtcdtools.adapters.PackageEntry;

public class LaunchActionActivity extends ActionActivity {
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
    protected Action createAction(String actionName) {
        PackageEntry packageEntry = (PackageEntry)mInstalledPackagesSpinner.getSelectedItem();
        return new LaunchAction(actionName, packageEntry.getName());
    }

    @Override
    protected void fillControls(Action action) {
        super.fillControls(action);

        LaunchAction launchAction = (LaunchAction)action;

        if(launchAction == null) {
            Toast.makeText(this, this.getText(R.string.UnknownActionType), Toast.LENGTH_LONG).show();
            finish();
        } else {
            int packagePosition = mInstalledPackagesArrayAdapter.getPosition(launchAction.getPackageName());
            mInstalledPackagesSpinner.setSelection(packagePosition);
        }
    }

    private Spinner mInstalledPackagesSpinner;
    private InstalledPackagesArrayAdapter mInstalledPackagesArrayAdapter;
}
