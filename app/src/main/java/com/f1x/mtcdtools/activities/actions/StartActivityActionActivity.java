package com.f1x.mtcdtools.activities.actions;

import com.f1x.mtcdtools.R;
import com.f1x.mtcdtools.actions.Action;
import com.f1x.mtcdtools.actions.StartActivityAction;

import org.json.JSONException;
import org.json.JSONObject;

public class StartActivityActionActivity extends CustomIntentActionActivity {
    public StartActivityActionActivity() {
        super(R.layout.activity_start_activity_action_details);
    }

    @Override
    protected Action createAction(String actionName, JSONObject intentExtrasJson) throws JSONException {
        return new StartActivityAction(actionName,
                mIntentPackageEditText.getEditableText().toString(),
                mIntentActionEditText.getEditableText().toString(),
                mIntentCategoryEditText.getEditableText().toString(),
                mIntentDataEditText.getEditableText().toString(),
                mIntentTypeEditText.getEditableText().toString(),
                intentExtrasJson);
    }
}
