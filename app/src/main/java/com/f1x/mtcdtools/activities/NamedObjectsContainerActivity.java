package com.f1x.mtcdtools.activities;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.f1x.mtcdtools.R;
import com.f1x.mtcdtools.adapters.NamedObjectsArrayAdapter;
import com.f1x.mtcdtools.adapters.NamesArrayAdapter;
import com.f1x.mtcdtools.named.objects.NamedObject;
import com.f1x.mtcdtools.named.objects.NamedObjectsContainer;

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
        final Spinner actionsSpinner = (Spinner)this.findViewById(R.id.spinnerNamedObjects);
        actionsSpinner.setAdapter(mNamedObjectsArrayAdapter);

        ListView addedActionsListView = (ListView)this.findViewById(R.id.listViewAddedNamedObjects);
        mAddedNamesArrayAdapter = new NamesArrayAdapter(this);
        addedActionsListView.setAdapter(mAddedNamesArrayAdapter);
        addedActionsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                String actionName = mAddedNamesArrayAdapter.getItem(position);
                mAddedNamesArrayAdapter.remove(actionName);

                return true;
            }
        });

        Button addNamedObjectButton = (Button)this.findViewById(R.id.buttonAddNamedObject);
        addNamedObjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String actionName = (String)actionsSpinner.getSelectedItem();
                mAddedNamesArrayAdapter.add(actionName);
            }
        });
    }

    @Override
    protected void fillControls(NamedObject namedObject) throws ClassCastException {
        super.fillControls(namedObject);

        NamedObjectsContainer namedObjectsContainer = (NamedObjectsContainer)namedObject;
        mAddedNamesArrayAdapter.clear();
        mAddedNamesArrayAdapter.addAll(namedObjectsContainer.getActionNames());
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        mNamedObjectsArrayAdapter.reset(mServiceBinder.getNamedObjectsStorage().getItems());
    }

    NamedObjectsArrayAdapter mNamedObjectsArrayAdapter;
    NamesArrayAdapter mAddedNamesArrayAdapter;
}
