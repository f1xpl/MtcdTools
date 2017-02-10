package com.f1x.mtcdtools.activities;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.f1x.mtcdtools.R;
import com.f1x.mtcdtools.adapters.ActionsInSequenceArrayAdapter;
import com.f1x.mtcdtools.adapters.entries.ActionInSequenceEntry;
import com.f1x.mtcdtools.named.objects.ActionsSequence;
import com.f1x.mtcdtools.named.objects.NamedObject;
import com.f1x.mtcdtools.named.objects.actions.BroadcastIntentAction;
import com.f1x.mtcdtools.named.objects.actions.KeyAction;
import com.f1x.mtcdtools.named.objects.actions.LaunchAction;
import com.f1x.mtcdtools.named.objects.actions.StartActivityAction;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

/**
 * Created by f1x on 2017-02-05.
 */

public class ActionsSequenceActivity extends NamedObjectsContainerActivity {
    public ActionsSequenceActivity() {
        super(R.layout.activity_actions_sequence_details);
    }

    @Override
    protected void initControls() {
        super.initControls();
        mNamedObjectsArrayAdapter.setObjectTypeFilters(new TreeSet<>(Arrays.asList(KeyAction.OBJECT_TYPE, LaunchAction.OBJECT_TYPE, BroadcastIntentAction.OBJECT_TYPE, StartActivityAction.OBJECT_TYPE)));
        mActionsInSequenceArrayAdapter = new ActionsInSequenceArrayAdapter(this);

        ListView addedActionsListView = (ListView)this.findViewById(R.id.listViewAddedNamedObjects);
        addedActionsListView.setAdapter(mActionsInSequenceArrayAdapter);
        addedActionsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                mActionsInSequenceArrayAdapter.removeAt(position);

                return true;
            }
        });

        Button addNamedObjectButton = (Button)this.findViewById(R.id.buttonAddNamedObject);
        addNamedObjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String actionName = (String)mNamedObjectsSpinner.getSelectedItem();
                ActionInSequenceEntry actionInSequenceEntry = new ActionInSequenceEntry(actionName, 0);
                mActionsInSequenceArrayAdapter.add(actionInSequenceEntry);
            }
        });
    }

    @Override
    protected void fillControls(NamedObject namedObject) {
        super.fillControls(namedObject);
        if(namedObject.getObjectType().equals(ActionsSequence.OBJECT_TYPE)) {
            ActionsSequence actionsSequence = (ActionsSequence)namedObject;
            mActionsInSequenceArrayAdapter.reset(actionsSequence.getActionDelays());
        }
    }

     @Override
    protected NamedObject createNamedObject(String namedObjectName) {
         List<String> actionNames = new ArrayList<>();
         List<Map.Entry<String, Integer>> actionDelays = new ArrayList<>();

         for(int i = 0; i < mActionsInSequenceArrayAdapter.getCount(); ++i) {
             ActionInSequenceEntry actionInSequenceEntry = mActionsInSequenceArrayAdapter.getItem(i);
             actionNames.add(actionInSequenceEntry.getActionName());
             actionDelays.add(new AbstractMap.SimpleEntry<>(actionInSequenceEntry.getActionName(), actionInSequenceEntry.getDelay()));
         }

        return new ActionsSequence(namedObjectName, actionNames, actionDelays);
    }

    ActionsInSequenceArrayAdapter mActionsInSequenceArrayAdapter;
}
