package com.f1x.mtcdtools.activities;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.f1x.mtcdtools.R;
import com.f1x.mtcdtools.adapters.NamedObjectIdsArrayAdapter;
import com.f1x.mtcdtools.named.objects.ActionsSequence;
import com.f1x.mtcdtools.named.objects.ModeList;
import com.f1x.mtcdtools.named.objects.NamedObject;
import com.f1x.mtcdtools.named.objects.NamedObjectId;
import com.f1x.mtcdtools.named.objects.actions.BroadcastIntentAction;
import com.f1x.mtcdtools.named.objects.actions.KeyAction;
import com.f1x.mtcdtools.named.objects.actions.LaunchAction;
import com.f1x.mtcdtools.named.objects.actions.StartActivityAction;

import java.util.Arrays;
import java.util.TreeSet;

/**
 * Created by COMPUTER on 2017-02-23.
 */

public class ModeListActivity extends NamedObjectsContainerActivity {
    public ModeListActivity() {
        super(R.layout.activity_mode_list_details);
    }

    @Override
    protected void initControls() {
        super.initControls();
        mNamedObjectIdsArrayAdapter.setObjectTypeFilters(new TreeSet<>(Arrays.asList(KeyAction.OBJECT_TYPE, LaunchAction.OBJECT_TYPE, BroadcastIntentAction.OBJECT_TYPE, StartActivityAction.OBJECT_TYPE, ActionsSequence.OBJECT_TYPE)));
        mModeListArrayAdapter = new NamedObjectIdsArrayAdapter(this);

        ListView addedActionsListView = (ListView) this.findViewById(R.id.listViewAddedNamedObjects);
        addedActionsListView.setAdapter(mModeListArrayAdapter);
        addedActionsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                mModeListArrayAdapter.removeAt(position);
                return true;
            }
        });

        Button addNamedObjectButton = (Button) this.findViewById(R.id.buttonAddNamedObject);
        addNamedObjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NamedObjectId actionId = (NamedObjectId) mNamedObjectsSpinner.getSelectedItem();
                mModeListArrayAdapter.add(actionId);
            }
        });
    }

    @Override
    protected void fillControls(NamedObject namedObject) {
        super.fillControls(namedObject);
        if(namedObject.getObjectType().equals(ModeList.OBJECT_TYPE)) {
            ModeList modeList = (ModeList)namedObject;
            mModeListArrayAdapter.reset(modeList.getActionIds());
        }
    }

    @Override
    protected NamedObject createNamedObject(NamedObjectId namedObjectId) {
        return new ModeList(namedObjectId, mModeListArrayAdapter.getItems());
    }

    private NamedObjectIdsArrayAdapter mModeListArrayAdapter;
}
