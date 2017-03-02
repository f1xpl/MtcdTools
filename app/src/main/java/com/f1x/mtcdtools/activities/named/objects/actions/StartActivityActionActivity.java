package com.f1x.mtcdtools.activities.named.objects.actions;

import android.widget.EditText;

import com.f1x.mtcdtools.R;
import com.f1x.mtcdtools.named.objects.NamedObject;
import com.f1x.mtcdtools.named.objects.NamedObjectId;
import com.f1x.mtcdtools.named.objects.actions.Action;
import com.f1x.mtcdtools.named.objects.actions.StartActivityAction;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class StartActivityActionActivity extends CustomIntentActionActivity {
    public StartActivityActionActivity() {
        super(R.layout.activity_start_activity_action_details);
    }

    @Override
    protected void initControls() {
        super.initControls();
        mClassNameEditText = (EditText)this.findViewById(R.id.editTextClassName);
        mFlagsNameEditText = (EditText)this.findViewById(R.id.editTextFlags);
    }

    @Override
    protected void fillControls(NamedObject namedObject) throws ClassCastException {
        super.fillControls(namedObject);

        StartActivityAction startActivityAction = (StartActivityAction)namedObject;
        mClassNameEditText.setText(startActivityAction.getClassName());
        mFlagsNameEditText.setText(String.format(Locale.getDefault(), "%d", startActivityAction.getFlags()));
    }

    @Override
    protected Action createAction(NamedObjectId actionId, JSONObject intentExtrasJson) throws JSONException {
        String flagsString = mFlagsNameEditText.getText().toString();
        int flags = flagsString.isEmpty() ? 0 : Integer.parseInt(flagsString);

        return new StartActivityAction(actionId,
                mIntentPackageEditText.getEditableText().toString(),
                mIntentActionEditText.getEditableText().toString(),
                mIntentCategoryEditText.getEditableText().toString(),
                mIntentDataEditText.getEditableText().toString(),
                mIntentTypeEditText.getEditableText().toString(),
                intentExtrasJson,
                mClassNameEditText.getEditableText().toString(),
                flags);
    }

    private EditText mClassNameEditText;
    private EditText mFlagsNameEditText;
}
