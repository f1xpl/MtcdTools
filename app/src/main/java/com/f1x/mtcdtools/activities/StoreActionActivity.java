package com.f1x.mtcdtools.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.f1x.mtcdtools.R;
import com.f1x.mtcdtools.actions.Action;
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

        final EditText actionNameEditText = (EditText)this.findViewById(R.id.editTextActionName);
        Button saveButton = (Button)this.findViewById(R.id.buttonSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String actionName = actionNameEditText.getText().toString();
                if(actionName.isEmpty()) {
                    Toast.makeText(StoreActionActivity.this, StoreActionActivity.this.getText(R.string.EmptyActionNameError), Toast.LENGTH_LONG).show();
                } else {
                    Action action = createAction(actionName);

                    if(action != null) {
                        storeAction(action);
                    }
                }
            }
        });
    }

    protected abstract Action createAction(String actionName);

    protected void storeAction(Action action) {
        if(mServiceBinder != null) {
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
        } else {
            Toast.makeText(this, this.getText(R.string.InternalError), Toast.LENGTH_LONG).show();
        }
    }
}
