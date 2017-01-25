package com.f1x.mtcdtools.activities.actions;

import android.os.Bundle;
import android.widget.EditText;

import com.f1x.mtcdtools.R;
import com.f1x.mtcdtools.actions.Action;
import com.f1x.mtcdtools.actions.BroadcastIntentAction;

import org.json.JSONException;
import org.json.JSONObject;

public class CreateBroadcastIntentActionActivity extends StoreCustomIntentActionActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast_intent_action_details);
        initControls();

        mBroadcastPermissionEditText = (EditText)this.findViewById(R.id.editTextBroadcastPermissions);
    }

    @Override
    protected void onServiceConnected() {

    }

    @Override
    public Action createAction(String actionName, JSONObject intentExtrasJson) throws JSONException {
        return new BroadcastIntentAction(actionName,
                                         mIntentPackageEditText.getEditableText().toString(),
                                         mActionNameEditText.getEditableText().toString(),
                                         mIntentCategoryEditText.getEditableText().toString(),
                                         mIntentDataEditText.getEditableText().toString(),
                                         mIntentTypeEditText.getEditableText().toString(),
                                         intentExtrasJson,
                                         mBroadcastPermissionEditText.getEditableText().toString());
    }

    private EditText mBroadcastPermissionEditText;
}
