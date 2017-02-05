package com.f1x.mtcdtools.activities;

import com.f1x.mtcdtools.R;
import com.f1x.mtcdtools.named.objects.ActionsSequence;
import com.f1x.mtcdtools.named.objects.NamedObject;
import com.f1x.mtcdtools.named.objects.actions.BroadcastIntentAction;
import com.f1x.mtcdtools.named.objects.actions.KeyAction;
import com.f1x.mtcdtools.named.objects.actions.LaunchAction;
import com.f1x.mtcdtools.named.objects.actions.StartActivityAction;

import java.util.Arrays;
import java.util.TreeSet;

/**
 * Created by COMPUTER on 2017-02-05.
 */

public class ActionsSequenceActivity extends NamedObjectsContainerActivity {
    public ActionsSequenceActivity() {
        super(R.layout.activity_actions_sequence_details);
    }

    @Override
    protected void initControls() {
        super.initControls();
        mNamedObjectsArrayAdapter.setObjectTypeFilters(new TreeSet<>(Arrays.asList(KeyAction.OBJECT_TYPE, LaunchAction.OBJECT_TYPE, BroadcastIntentAction.OBJECT_TYPE, StartActivityAction.OBJECT_TYPE)));
    }

     @Override
    protected NamedObject createNamedObject(String namedObjectName) {
        return new ActionsSequence(namedObjectName, mAddedNamesArrayAdapter.getItems());
    }
}
