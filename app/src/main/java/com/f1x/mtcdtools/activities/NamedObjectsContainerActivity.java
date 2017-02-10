package com.f1x.mtcdtools.activities;

import android.widget.Spinner;

import com.f1x.mtcdtools.R;
import com.f1x.mtcdtools.adapters.NamedObjectsArrayAdapter;

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

        mNamedObjectsArrayAdapter = new NamedObjectsArrayAdapter(this);
        mNamedObjectsSpinner = (Spinner)this.findViewById(R.id.spinnerNamedObjects);
        mNamedObjectsSpinner.setAdapter(mNamedObjectsArrayAdapter);
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        mNamedObjectsArrayAdapter.reset(mServiceBinder.getNamedObjectsStorage().getItems());
    }

    NamedObjectsArrayAdapter mNamedObjectsArrayAdapter;
    Spinner mNamedObjectsSpinner;
}
