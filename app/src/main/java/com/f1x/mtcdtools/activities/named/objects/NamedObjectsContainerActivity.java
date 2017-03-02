package com.f1x.mtcdtools.activities.named.objects;

import android.widget.Spinner;

import com.f1x.mtcdtools.R;
import com.f1x.mtcdtools.adapters.NamedObjectIdsArrayAdapter;

/**
 * Created by f1x on 2017-02-05.
 */

public abstract class NamedObjectsContainerActivity extends NamedObjectActivity {
    public NamedObjectsContainerActivity(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void initControls() {
        super.initControls();

        mNamedObjectIdsArrayAdapter = new NamedObjectIdsArrayAdapter(this);
        mNamedObjectsSpinner = (Spinner)this.findViewById(R.id.spinnerNamedObjects);
        mNamedObjectsSpinner.setAdapter(mNamedObjectIdsArrayAdapter);
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        mNamedObjectIdsArrayAdapter.reset(mServiceBinder.getNamedObjectsStorage().getItems());
    }

    NamedObjectIdsArrayAdapter mNamedObjectIdsArrayAdapter;
    Spinner mNamedObjectsSpinner;
}
