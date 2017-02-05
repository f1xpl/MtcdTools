package com.f1x.mtcdtools.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.Toast;

import com.f1x.mtcdtools.ActionsList;
import com.f1x.mtcdtools.R;
import com.f1x.mtcdtools.adapters.KeysSequenceArrayAdapter;
import com.f1x.mtcdtools.adapters.NamesArrayAdapter;
import com.f1x.mtcdtools.input.KeysSequenceConverter;
import com.f1x.mtcdtools.storage.NamedObject;

import java.util.List;

/**
 * Created by COMPUTER on 2017-01-31.
 */

public class ActionsListActivity extends NamedObjectActivity {
    public ActionsListActivity() {
        super(R.layout.activity_actions_list_details);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -----------------------------------------------------------------------------------------
        TabHost tabHost = (TabHost)findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec addActionsTab = tabHost.newTabSpec(this.getString(R.string.Objects));
        addActionsTab.setContent(R.id.tabAddNamedObjects);
        addActionsTab.setIndicator(this.getString(R.string.Objects));
        tabHost.addTab(addActionsTab);

        TabHost.TabSpec obtainKeysSequenceUpTab = tabHost.newTabSpec(this.getString(R.string.KeysSequenceUp));
        obtainKeysSequenceUpTab.setContent(R.id.tabObtainKeysSequenceUp);
        obtainKeysSequenceUpTab.setIndicator(this.getString(R.string.KeysSequenceUp));
        tabHost.addTab(obtainKeysSequenceUpTab);

        TabHost.TabSpec obtainKeysSequenceDownTab = tabHost.newTabSpec(this.getString(R.string.KeysSequenceDown));
        obtainKeysSequenceDownTab.setContent(R.id.tabObtainKeysSequenceDown);
        obtainKeysSequenceDownTab.setIndicator(this.getString(R.string.KeysSequenceDown));
        tabHost.addTab(obtainKeysSequenceDownTab);

        TabHost.TabSpec storeActionsListTab = tabHost.newTabSpec(this.getString(R.string.Save));
        storeActionsListTab.setContent(R.id.tabSave);
        storeActionsListTab.setIndicator(this.getString(R.string.Save));
        tabHost.addTab(storeActionsListTab);
        // -----------------------------------------------------------------------------------------
    }

    @Override
    protected void initControls() {
        super.initControls();

        // -----------------------------------------------------------------------------------------
        mKeysSequenceUpArrayAdapter = new KeysSequenceArrayAdapter(this);
        ListView keysSequenceUpListView = (ListView)this.findViewById(R.id.listViewKeysSequenceUp);
        keysSequenceUpListView.setAdapter(mKeysSequenceUpArrayAdapter);

        Button obtainKeysSequenceUpButton = (Button)this.findViewById(R.id.buttonObtainKeysSequenceUp);
        obtainKeysSequenceUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(ActionsListActivity.this, ObtainKeysSequenceActivity.class), REQUEST_CODE_KEYS_SEQUENCE_UP);
            }
        });
        // -----------------------------------------------------------------------------------------

        // -----------------------------------------------------------------------------------------
        mKeysSequenceDownArrayAdapter = new KeysSequenceArrayAdapter(this);
        ListView keysSequenceDownListView = (ListView)this.findViewById(R.id.listViewKeysSequenceDown);
        keysSequenceDownListView.setAdapter(mKeysSequenceDownArrayAdapter);

        Button obtainKeysSequenceDownButton = (Button)this.findViewById(R.id.buttonObtainKeysSequenceDown);
        obtainKeysSequenceDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(ActionsListActivity.this, ObtainKeysSequenceActivity.class), REQUEST_CODE_KEYS_SEQUENCE_DOWN);
            }
        });
        // -----------------------------------------------------------------------------------------

        // -----------------------------------------------------------------------------------------
        mNamesArrayAdapter = new NamesArrayAdapter(this);
        final Spinner actionsSpinner = (Spinner)this.findViewById(R.id.spinnerNamedObjects);
        actionsSpinner.setAdapter(mNamesArrayAdapter);

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

        Button addActionButton = (Button)this.findViewById(R.id.buttonAddNamedObject);
        addActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String actionName = (String)actionsSpinner.getSelectedItem();

                if(!mAddedNamesArrayAdapter.containsItem(actionName)) {
                    mAddedNamesArrayAdapter.add(actionName);
                } else {
                    Toast.makeText(ActionsListActivity.this, ActionsListActivity.this.getText(R.string.ObjectAlreadyAdded), Toast.LENGTH_LONG).show();
                }
            }
        });
        // -----------------------------------------------------------------------------------------
    }

    @Override
    protected void fillControls(NamedObject namedObject) throws ClassCastException {
        super.fillControls(namedObject);

        ActionsList actionsList = (ActionsList)namedObject;

        mAddedNamesArrayAdapter.reset(actionsList.getActionNames());
        mKeysSequenceDownArrayAdapter.reset(actionsList.getKeysSequenceDown());
        mKeysSequenceUpArrayAdapter.reset(actionsList.getKeysSequenceUp());
    }

    @Override
    protected NamedObject createNamedObject(String namedObjectName) {
        return new ActionsList(namedObjectName,
                mKeysSequenceUpArrayAdapter.getItems(),
                mKeysSequenceDownArrayAdapter.getItems(),
                mAddedNamesArrayAdapter.getItems());
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        mNamesArrayAdapter.reset(mServiceBinder.getNamedObjectsStorage().getItems().keySet());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == ObtainKeysSequenceActivity.RESULT_CANCELED) {
            return;
        }

        List<Integer> keysSequence = KeysSequenceConverter.fromArray(data.getIntArrayExtra(ObtainKeysSequenceActivity.RESULT_NAME));

        if(requestCode == REQUEST_CODE_KEYS_SEQUENCE_UP) {
            mKeysSequenceUpArrayAdapter.reset(keysSequence);
        } else if(requestCode == REQUEST_CODE_KEYS_SEQUENCE_DOWN) {
            mKeysSequenceDownArrayAdapter.reset(keysSequence);
        }
    }

    private KeysSequenceArrayAdapter mKeysSequenceUpArrayAdapter;
    private KeysSequenceArrayAdapter mKeysSequenceDownArrayAdapter;
    private NamesArrayAdapter mNamesArrayAdapter;
    private NamesArrayAdapter mAddedNamesArrayAdapter;

    private static final int REQUEST_CODE_KEYS_SEQUENCE_UP = 100;
    private static final int REQUEST_CODE_KEYS_SEQUENCE_DOWN = 101;
}
