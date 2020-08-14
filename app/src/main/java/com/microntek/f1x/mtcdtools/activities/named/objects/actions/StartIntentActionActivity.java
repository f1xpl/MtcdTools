package com.microntek.f1x.mtcdtools.activities.named.objects.actions;

import android.widget.EditText;
import android.widget.RadioButton;

import com.microntek.f1x.mtcdtools.R;
import com.microntek.f1x.mtcdtools.named.NamedObject;
import com.microntek.f1x.mtcdtools.named.NamedObjectId;
import com.microntek.f1x.mtcdtools.named.objects.actions.Action;
import com.microntek.f1x.mtcdtools.named.objects.actions.StartIntentAction;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class StartIntentActionActivity extends CustomIntentActionActivity {
    public StartIntentActionActivity() {
        super(R.layout.activity_start_intent_action_details);
    }

    @Override
    protected void initControls() {
        super.initControls();
        mClassNameEditText = (EditText)this.findViewById(R.id.editTextClassName);
        mFlagsNameEditText = (EditText)this.findViewById(R.id.editTextFlags);
        mTargetActivityRadioButton = (RadioButton)this.findViewById(R.id.radioButtonActivity);
        mTargetServiceRadioButton = (RadioButton)this.findViewById(R.id.radioButtonService);

        mTargetActivityRadioButton.setChecked(true);
    }

    @Override
    protected void fillControls(NamedObject namedObject) throws ClassCastException {
        super.fillControls(namedObject);

        StartIntentAction startIntentAction = (StartIntentAction)namedObject;
        mClassNameEditText.setText(startIntentAction.getClassName());
        mFlagsNameEditText.setText(String.format(Locale.getDefault(), "%d", startIntentAction.getFlags()));

        final int target = startIntentAction.getTarget();
        mTargetActivityRadioButton.setChecked(target == StartIntentAction.TARGET_ACTIVITY);
        mTargetServiceRadioButton.setChecked(target == StartIntentAction.TARGET_SERVICE);
    }

    @Override
    protected Action createAction(NamedObjectId actionId, JSONObject intentExtrasJson) throws JSONException {
        String flagsString = mFlagsNameEditText.getText().toString();
        int flags = flagsString.isEmpty() ? 0 : Integer.parseInt(flagsString);

        final int target = mTargetServiceRadioButton.isChecked() ? StartIntentAction.TARGET_SERVICE : StartIntentAction.TARGET_ACTIVITY;

        return new StartIntentAction(actionId,
                mIntentPackageEditText.getEditableText().toString(),
                mIntentActionEditText.getEditableText().toString(),
                mIntentCategoryEditText.getEditableText().toString(),
                mIntentDataEditText.getEditableText().toString(),
                mIntentTypeEditText.getEditableText().toString(),
                intentExtrasJson,
                mClassNameEditText.getEditableText().toString(),
                flags,
                target);
    }

    private EditText mClassNameEditText;
    private EditText mFlagsNameEditText;
    private RadioButton mTargetActivityRadioButton;
    private RadioButton mTargetServiceRadioButton;
}
