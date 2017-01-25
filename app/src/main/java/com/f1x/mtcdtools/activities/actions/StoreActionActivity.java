package com.f1x.mtcdtools.activities.actions;

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

public abstract class StoreActionActivity extends ServiceActivity {
    protected void initControls() {
        Button cancelButton = (Button)this.findViewById(R.id.buttonCancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mActionNameEditText = (EditText)this.findViewById(R.id.editTextActionName);
        Button saveButton = (Button)this.findViewById(R.id.buttonSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String actionName = mActionNameEditText.getText().toString();
                if(actionName.isEmpty()) {
                    Toast.makeText(StoreActionActivity.this, StoreActionActivity.this.getText(R.string.EmptyActionNameError), Toast.LENGTH_LONG).show();
                } else {
                    onSaveAction(actionName);
                }
            }
        });
    }

    protected void onSaveAction(String actionName) {
        Action action = createAction(actionName);

        if(action != null) {
            storeAction(action);
        }
    }

    protected abstract Action createAction(String actionName);

    protected void storeAction(Action action) {
        try {
            mServiceBinder.getActionsStorage().insert(action);
            finish();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (DuplicatedEntryException e) {
            e.printStackTrace();
            Toast.makeText(this, this.getText(R.string.DuplicatedActionNameError), Toast.LENGTH_LONG).show();
        }
    }

    protected EditText mActionNameEditText;
}
