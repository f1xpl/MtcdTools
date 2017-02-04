package com.f1x.mtcdtools.activities.actions;

import android.widget.EditText;
import android.widget.Toast;

import com.f1x.mtcdtools.R;
import com.f1x.mtcdtools.actions.Action;
import com.f1x.mtcdtools.actions.BroadcastIntentAction;
import com.f1x.mtcdtools.actions.StartActivityAction;

import org.json.JSONException;
import org.json.JSONObject;

public class StartActivityActionActivity extends CustomIntentActionActivity {
    public StartActivityActionActivity() {
        super(R.layout.activity_start_activity_action_details);
    }

    @Override
    protected void initControls() {
        super.initControls();
        mClassNameEditText = (EditText)this.findViewById(R.id.editTextClassName);
    }

    @Override
    protected void fillControls(Action action) {
        super.fillControls(action);

        StartActivityAction startActivityAction = (StartActivityAction)action;

        if(startActivityAction == null) {
            Toast.makeText(this, this.getText(R.string.UnknownObjectType), Toast.LENGTH_LONG).show();
            finish();
        } else {
            mClassNameEditText.setText(startActivityAction.getClassName());
        }
    }

    @Override
    protected Action createAction(String actionName, JSONObject intentExtrasJson) throws JSONException {
        return new StartActivityAction(actionName,
                mIntentPackageEditText.getEditableText().toString(),
                mIntentActionEditText.getEditableText().toString(),
                mIntentCategoryEditText.getEditableText().toString(),
                mIntentDataEditText.getEditableText().toString(),
                mIntentTypeEditText.getEditableText().toString(),
                intentExtrasJson,
                mClassNameEditText.getEditableText().toString());
    }

    private EditText mClassNameEditText;
}
