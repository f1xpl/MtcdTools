package com.f1x.mtcdtools.activities.actions;

import android.widget.EditText;
import android.widget.Toast;

import com.f1x.mtcdtools.R;
import com.f1x.mtcdtools.actions.Action;
import com.f1x.mtcdtools.actions.BroadcastIntentAction;
import com.f1x.mtcdtools.storage.NamedObject;

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
    protected Action createAction(String actionName, JSONObject intentExtrasJson) throws JSONException {
        return new BroadcastIntentAction(actionName,
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
