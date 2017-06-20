package android.microntek.f1x.mtcdtools.activities.named.objects.actions;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import android.microntek.f1x.mtcdtools.R;
import android.microntek.f1x.mtcdtools.activities.named.objects.NamedObjectActivity;
import android.microntek.f1x.mtcdtools.adapters.InstalledPackagesArrayAdapter;
import android.microntek.f1x.mtcdtools.adapters.entries.PackageEntry;
import android.microntek.f1x.mtcdtools.named.NamedObject;
import android.microntek.f1x.mtcdtools.named.NamedObjectId;
import android.microntek.f1x.mtcdtools.named.objects.actions.Action;
import android.microntek.f1x.mtcdtools.named.objects.actions.LaunchAction;

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

        mInstalledPackagesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                PackageEntry packageEntry = mInstalledPackagesArrayAdapter.getItem(position);
                mNameEditText.setText(packageEntry.getLabel());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mNameEditText.setText("");
            }
        });
    }

    @Override
    protected Action createNamedObject(NamedObjectId namedObjectId) {
        PackageEntry packageEntry = (PackageEntry)mInstalledPackagesSpinner.getSelectedItem();
        return new LaunchAction(namedObjectId, packageEntry.getName());
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
