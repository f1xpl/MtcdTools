package com.microntek.f1x.mtcdtools.activities.named.objects.actions;

import android.widget.EditText;
import android.widget.Toast;

import com.microntek.f1x.mtcdtools.R;
import com.microntek.f1x.mtcdtools.activities.named.objects.NamedObjectActivity;
import com.microntek.f1x.mtcdtools.named.NamedObject;
import com.microntek.f1x.mtcdtools.named.NamedObjectId;
import com.microntek.f1x.mtcdtools.named.objects.actions.Action;
import com.microntek.f1x.mtcdtools.named.objects.actions.CustomIntentAction;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by f1x on 2017-01-24.
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
    protected NamedObject createNamedObject(NamedObjectId namedObjectId) {
        try {
            String intentExtrasJsonString = mIntentExtrasEditText.getEditableText().toString();
            JSONObject intentExtrasJson = intentExtrasJsonString.isEmpty() ? new JSONObject() : new JSONObject(intentExtrasJsonString);
            return createAction(namedObjectId, intentExtrasJson);
        }
        catch(JSONException e) {
            e.printStackTrace();
            Toast.makeText(CustomIntentActionActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }

        return null;
    }

    @Override
    protected void fillControls(NamedObject namedObject) throws ClassCastException {
        super.fillControls(namedObject);

        try {
            CustomIntentAction customIntentAction = (CustomIntentAction)namedObject;

            mIntentActionEditText.setText(customIntentAction.getIntentAction());
            mIntentExtrasEditText.setText(customIntentAction.getIntentExtras().toString());
            mIntentCategoryEditText.setText(customIntentAction.getIntentCategory());
            mIntentDataEditText.setText(customIntentAction.getIntentData());
            mIntentPackageEditText.setText(customIntentAction.getIntentPackage());
            mIntentTypeEditText.setText(customIntentAction.getIntentType());
        } catch(JSONException e) {
            e.printStackTrace();
            Toast.makeText(CustomIntentActionActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    protected abstract Action createAction(NamedObjectId actionId, JSONObject intentExtrasJson) throws JSONException;

    EditText mIntentActionEditText;
    EditText mIntentCategoryEditText;
    EditText mIntentDataEditText;
    EditText mIntentPackageEditText;
    EditText mIntentTypeEditText;
    private EditText mIntentExtrasEditText;
}
