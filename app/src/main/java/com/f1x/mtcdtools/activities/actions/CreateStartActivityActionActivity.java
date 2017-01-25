package com.f1x.mtcdtools.activities.actions;

import android.os.Bundle;

import com.f1x.mtcdtools.R;
import com.f1x.mtcdtools.actions.Action;
import com.f1x.mtcdtools.actions.StartActivityAction;

import org.json.JSONException;
import org.json.JSONObject;

public class CreateStartActivityActionActivity extends StoreCustomIntentActionActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_activity_action_details);
        initControls();
    }

    @Override
    protected void onServiceConnected() {

    }

    @Override
    public Action createAction(String actionName, JSONObject intentExtrasJson) throws JSONException {
        return new StartActivityAction(actionName,
                mIntentPackageEditText.getEditableText().toString(),
                mActionNameEditText.getEditableText().toString(),
                mIntentCategoryEditText.getEditableText().toString(),
                mIntentDataEditText.getEditableText().toString(),
                mIntentTypeEditText.getEditableText().toString(),
                intentExtrasJson);
    }
}
