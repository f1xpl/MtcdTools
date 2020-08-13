package com.microntek.f1x.mtcdtools.activities.named.objects.actions;

import android.widget.EditText;

import com.microntek.f1x.mtcdtools.R;
import com.microntek.f1x.mtcdtools.named.NamedObject;
import com.microntek.f1x.mtcdtools.named.NamedObjectId;
import com.microntek.f1x.mtcdtools.named.objects.actions.Action;
import com.microntek.f1x.mtcdtools.named.objects.actions.BroadcastIntentAction;

import org.json.JSONException;
import org.json.JSONObject;

public class BroadcastIntentActionActivity extends CustomIntentActionActivity {
    public BroadcastIntentActionActivity() {
        super(R.layout.activity_broadcast_intent_action_details);
    }

    @Override
    protected void initControls() {
        super.initControls();
        mBroadcastPermissionEditText = (EditText)this.findViewById(R.id.editTextBroadcastPermissions);
    }

    @Override
    protected Action createAction(NamedObjectId actionId, JSONObject intentExtrasJson) throws JSONException {
        return new BroadcastIntentAction(actionId,
                                         mIntentPackageEditText.getEditableText().toString(),
                                         mIntentActionEditText.getEditableText().toString(),
                                         mIntentCategoryEditText.getEditableText().toString(),
                                         mIntentDataEditText.getEditableText().toString(),
                                         mIntentTypeEditText.getEditableText().toString(),
                                         intentExtrasJson,
                                         mBroadcastPermissionEditText.getEditableText().toString());
    }

    @Override
    protected void fillControls(NamedObject namedObject) throws ClassCastException {
        super.fillControls(namedObject);

        BroadcastIntentAction broadcastIntentAction = (BroadcastIntentAction)namedObject;
        mBroadcastPermissionEditText.setText(broadcastIntentAction.getPermissions());
    }


    private EditText mBroadcastPermissionEditText;
}
