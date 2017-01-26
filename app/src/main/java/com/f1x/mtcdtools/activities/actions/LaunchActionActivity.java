package com.f1x.mtcdtools.activities.actions;

import android.os.Bundle;
import android.widget.Spinner;
import android.widget.Toast;

import com.f1x.mtcdtools.R;
import com.f1x.mtcdtools.actions.Action;
import com.f1x.mtcdtools.actions.LaunchAction;
import com.f1x.mtcdtools.adapters.InstalledPackagesArrayAdapter;
import com.f1x.mtcdtools.adapters.PackageEntry;

public class LaunchActionActivity extends ActionActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_action_details);
        initControls();

        mInstalledPackagesSpinner = (Spinner)this.findViewById(R.id.spinnerApplications);
        mInstalledPackagesArrayAdapter = new InstalledPackagesArrayAdapter(this);
        mInstalledPackagesSpinner.setAdapter(mInstalledPackagesArrayAdapter);
    }

    @Override
    public Action createAction(String actionName) {
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
