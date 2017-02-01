package com.f1x.mtcdtools.activities.actions;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.f1x.mtcdtools.R;
import com.f1x.mtcdtools.actions.Action;
import com.f1x.mtcdtools.activities.ServiceActivity;
import com.f1x.mtcdtools.storage.exceptions.DuplicatedEntryException;

import org.json.JSONException;

import java.io.IOException;

/**
 * Created by COMPUTER on 2017-01-23.
 */

public abstract class ActionActivity extends ServiceActivity {
    ActionActivity(int layoutResId) {
        mLayoutResId = layoutResId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(mLayoutResId);

        mEditActionName = this.getIntent().getStringExtra(ACTION_NAME_PARAMETER);
        mEditMode = mEditActionName != null;

        initControls();
    }

    protected void initControls() {
        Button cancelButton = (Button)this.findViewById(R.id.buttonCancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button saveButton = (Button)this.findViewById(R.id.buttonSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String actionName = mActionNameEditText.getText().toString();

                if(actionName.isEmpty()) {
                    Toast.makeText(ActionActivity.this, ActionActivity.this.getText(R.string.EmptyNameError), Toast.LENGTH_LONG).show();
                } else {
                    storeAction(actionName);
                }
            }
        });

        mActionNameEditText = (EditText)this.findViewById(R.id.editTextActionName);
    }

    protected void storeAction(String actionName) {
        try {
            Action action = createAction(actionName);
            if(action != null) {
                if(mEditMode) {
                    mServiceBinder.getActionsStorage().replace(mEditActionName, actionName, action);
                    mServiceBinder.getActionsListsStorage().replaceActionName(mEditActionName, actionName);
                    mServiceBinder.getKeysSequenceBindingsStorage().replaceActionName(mEditActionName, actionName);
                } else {
                    mServiceBinder.getActionsStorage().insert(actionName, action);
                }

                finish();
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (DuplicatedEntryException e) {
            e.printStackTrace();
            Toast.makeText(this, this.getText(R.string.ObjectAlreadyAdded), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onServiceConnected() {
        if(mEditMode) {
            Action action = mServiceBinder.getActionsStorage().getItem(mEditActionName);

            if (action != null) {
                fillControls(action);
            } else {
                Toast.makeText(this, this.getText(R.string.ObjectNotFound), Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    protected void fillControls(Action action) {
        mActionNameEditText.setText(mEditMode ? action.getName() : "");
    }

    protected abstract Action createAction(String actionName);

    protected EditText mActionNameEditText;
    protected String mEditActionName;
    protected boolean mEditMode;
    private final int mLayoutResId;

    public static final String ACTION_NAME_PARAMETER = "actionName";
}
