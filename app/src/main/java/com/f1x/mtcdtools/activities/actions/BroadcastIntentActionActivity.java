package com.f1x.mtcdtools.activities.actions;

import android.widget.EditText;
import android.widget.Toast;

import com.f1x.mtcdtools.R;
import com.f1x.mtcdtools.actions.Action;
import com.f1x.mtcdtools.actions.BroadcastIntentAction;

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
    protected void fillControls(Action action) {
        super.fillControls(action);

        BroadcastIntentAction broadcastIntentAction = (BroadcastIntentAction)action;

        if(broadcastIntentAction == null) {
            Toast.makeText(this, this.getText(R.string.UnknownObjectType), Toast.LENGTH_LONG).show();
            finish();
        } else {
            mBroadcastPermissionEditText.setText(broadcastIntentAction.getPermissions());
        }
    }


    private EditText mBroadcastPermissionEditText;
}
