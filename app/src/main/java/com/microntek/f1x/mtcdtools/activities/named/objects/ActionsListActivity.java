package com.microntek.f1x.mtcdtools.activities.named.objects;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;

import com.microntek.f1x.mtcdtools.R;
import com.microntek.f1x.mtcdtools.activities.input.ObtainKeysSequenceActivity;
import com.microntek.f1x.mtcdtools.adapters.KeysSequenceArrayAdapter;
import com.microntek.f1x.mtcdtools.adapters.NamedObjectIdsArrayAdapter;
import com.microntek.f1x.mtcdtools.utils.KeysSequenceConverter;
import com.microntek.f1x.mtcdtools.named.objects.containers.ActionsList;
import com.microntek.f1x.mtcdtools.named.objects.containers.ActionsSequence;
import com.microntek.f1x.mtcdtools.named.NamedObject;
import com.microntek.f1x.mtcdtools.named.NamedObjectId;
import com.microntek.f1x.mtcdtools.named.objects.actions.BroadcastIntentAction;
import com.microntek.f1x.mtcdtools.named.objects.actions.KeyAction;
import com.microntek.f1x.mtcdtools.named.objects.actions.LaunchAction;
import com.microntek.f1x.mtcdtools.named.objects.actions.StartIntentAction;

import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by f1x on 2017-01-31.
 */

public class ActionsListActivity extends NamedObjectsContainerActivity {
    public ActionsListActivity() {
        super(R.layout.activity_actions_list_details);
    }

    @Override
    protected void initControls() {
        super.initControls();

        mNamedObjectIdsArrayAdapter.setObjectTypeFilters(new TreeSet<>(Arrays.asList(ActionsSequence.OBJECT_TYPE, KeyAction.OBJECT_TYPE, LaunchAction.OBJECT_TYPE, BroadcastIntentAction.OBJECT_TYPE, StartIntentAction.OBJECT_TYPE)));

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
        ListView addedActionsListView = (ListView)this.findViewById(R.id.listViewAddedNamedObjects);
        mAddedNamedObjectIdsArrayAdapter = new NamedObjectIdsArrayAdapter(this);
        addedActionsListView.setAdapter(mAddedNamedObjectIdsArrayAdapter);
        addedActionsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                mAddedNamedObjectIdsArrayAdapter.removeAt(position);

                return true;
            }
        });

        Button addNamedObjectButton = (Button)this.findViewById(R.id.buttonAddNamedObject);
        addNamedObjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAddedNamedObjectIdsArrayAdapter.add((NamedObjectId)mNamedObjectsSpinner.getSelectedItem());
            }
        });
        // -----------------------------------------------------------------------------------------
    }

    @Override
    protected void fillControls(NamedObject namedObject) throws ClassCastException {
        super.fillControls(namedObject);

        if(namedObject.getObjectType().equals(ActionsList.OBJECT_TYPE)) {
            ActionsList actionsList = (ActionsList)namedObject;

            mKeysSequenceDownArrayAdapter.reset(actionsList.getKeysSequenceDown());
            mKeysSequenceUpArrayAdapter.reset(actionsList.getKeysSequenceUp());

            mAddedNamedObjectIdsArrayAdapter.reset(actionsList.getActionIds());
        }
    }

    @Override
    protected NamedObject createNamedObject(NamedObjectId namedObjectId) {
        return new ActionsList(namedObjectId,
                mKeysSequenceUpArrayAdapter.getItems(),
                mKeysSequenceDownArrayAdapter.getItems(),
                mAddedNamedObjectIdsArrayAdapter.getItems());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_CANCELED) {
            return;
        }

        List<Integer> keysSequence = KeysSequenceConverter.fromArray(data.getIntArrayExtra(ObtainKeysSequenceActivity.RESULT_NAME));

        if(requestCode == REQUEST_CODE_KEYS_SEQUENCE_UP) {
            mKeysSequenceUpArrayAdapter.reset(keysSequence);
        } else if(requestCode == REQUEST_CODE_KEYS_SEQUENCE_DOWN) {
            mKeysSequenceDownArrayAdapter.reset(keysSequence);
        }
    }

    private NamedObjectIdsArrayAdapter mAddedNamedObjectIdsArrayAdapter;
    private KeysSequenceArrayAdapter mKeysSequenceUpArrayAdapter;
    private KeysSequenceArrayAdapter mKeysSequenceDownArrayAdapter;

    private static final int REQUEST_CODE_KEYS_SEQUENCE_UP = 100;
    private static final int REQUEST_CODE_KEYS_SEQUENCE_DOWN = 101;
}
