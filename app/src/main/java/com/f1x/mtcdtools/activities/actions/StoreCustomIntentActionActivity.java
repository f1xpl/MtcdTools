package com.f1x.mtcdtools.activities.actions;

import android.widget.EditText;
import android.widget.Toast;

import com.f1x.mtcdtools.R;
import com.f1x.mtcdtools.actions.Action;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by COMPUTER on 2017-01-24.
 */

public abstract class StoreCustomIntentActionActivity extends StoreActionActivity {
    @Override
    protected void initControls() {
        super.initControls();

        mActionNameEditText = (EditText)this.findViewById(R.id.editTextActionName);
        mIntentCategoryEditText = (EditText)this.findViewById(R.id.editTextIntentCategory);
        mIntentDataEditText = (EditText)this.findViewById(R.id.editTextIntentData);
        mIntentPackageEditText = (EditText)this.findViewById(R.id.editTextIntentPackage);
        mIntentTypeEditText = (EditText)this.findViewById(R.id.editTextIntentType);
        mIntentExtrasEditText = (EditText)this.findViewById(R.id.editTextIntentExtras);
    }

    @Override
    protected Action createAction(String actionName) {
        try {
            String intentExtrasJsonString = mIntentExtrasEditText.getEditableText().toString();
            JSONObject intentExtrasJson = intentExtrasJsonString.isEmpty() ? new JSONObject() : new JSONObject(intentExtrasJsonString);
            return createAction(actionName, intentExtrasJson);
        }
        catch(JSONException e) {
            e.printStackTrace();
            Toast.makeText(StoreCustomIntentActionActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        return null;
    }

    protected abstract Action createAction(String actionName, JSONObject intentExtrasJson) throws JSONException;

    protected EditText mActionNameEditText;
    protected EditText mIntentCategoryEditText;
    protected EditText mIntentDataEditText;
    protected EditText mIntentPackageEditText;
    protected EditText mIntentTypeEditText;
    protected EditText mIntentExtrasEditText;
}
