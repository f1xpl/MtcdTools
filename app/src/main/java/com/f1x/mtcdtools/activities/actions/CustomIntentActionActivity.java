package com.f1x.mtcdtools.activities.actions;

import android.widget.EditText;
import android.widget.Toast;

import com.f1x.mtcdtools.R;
import com.f1x.mtcdtools.actions.Action;
import com.f1x.mtcdtools.actions.CustomIntentAction;
import com.f1x.mtcdtools.activities.NamedObjectActivity;
import com.f1x.mtcdtools.storage.NamedObject;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by COMPUTER on 2017-01-24.
 */

public abstract class CustomIntentActionActivity extends NamedObjectActivity {
    CustomIntentActionActivity(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void initControls() {
        super.initControls();

        mIntentActionEditText = (EditText)this.findViewById(R.id.editTextIntentAction);
        mIntentCategoryEditText = (EditText)this.findViewById(R.id.editTextIntentCategory);
        mIntentDataEditText = (EditText)this.findViewById(R.id.editTextIntentData);
        mIntentPackageEditText = (EditText)this.findViewById(R.id.editTextIntentPackage);
        mIntentTypeEditText = (EditText)this.findViewById(R.id.editTextIntentType);
        mIntentExtrasEditText = (EditText)this.findViewById(R.id.editTextIntentExtras);
    }

    @Override
    protected NamedObject createNamedObject(String namedObjectName) {
        try {
            String intentExtrasJsonString = mIntentExtrasEditText.getEditableText().toString();
            JSONObject intentExtrasJson = intentExtrasJsonString.isEmpty() ? new JSONObject() : new JSONObject(intentExtrasJsonString);
            return createAction(namedObjectName, intentExtrasJson);
        }
        catch(JSONException e) {
            e.printStackTrace();
            Toast.makeText(CustomIntentActionActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        return null;
    }

    @Override
    protected void fillControls(NamedObject namedObject) {
        super.fillControls(namedObject);

        CustomIntentAction customIntentAction = (CustomIntentAction)namedObject;

        if(customIntentAction == null) {
            Toast.makeText(this, this.getText(R.string.UnknownObjectType), Toast.LENGTH_LONG).show();
            finish();
        } else {
            try {
                mIntentActionEditText.setText(customIntentAction.getIntentAction());
                mIntentExtrasEditText.setText(customIntentAction.getIntentExtras().toString());
                mIntentCategoryEditText.setText(customIntentAction.getIntentCategory());
                mIntentDataEditText.setText(customIntentAction.getIntentData());
                mIntentPackageEditText.setText(customIntentAction.getIntentPackage());
                mIntentTypeEditText.setText(customIntentAction.getIntentType());
            } catch(JSONException e) {
                e.printStackTrace();
                Toast.makeText(CustomIntentActionActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    protected abstract Action createAction(String actionName, JSONObject intentExtrasJson) throws JSONException;

    protected EditText mIntentActionEditText;
    protected EditText mIntentCategoryEditText;
    protected EditText mIntentDataEditText;
    protected EditText mIntentPackageEditText;
    protected EditText mIntentTypeEditText;
    protected EditText mIntentExtrasEditText;
}
