package com.f1x.mtcdtools.activities;

import com.f1x.mtcdtools.R;
import com.f1x.mtcdtools.named.objects.ActionsSequence;
import com.f1x.mtcdtools.named.objects.NamedObject;

/**
 * Created by COMPUTER on 2017-02-05.
 */

public class ActionsSequenceActivity extends NamedObjectsContainerActivity {
    public ActionsSequenceActivity() {
        super(R.layout.activity_actions_sequence_details);
    }

     @Override
    protected NamedObject createNamedObject(String namedObjectName) {
        return new ActionsSequence(namedObjectName, mAddedNamesArrayAdapter.getItems());
    }
}
