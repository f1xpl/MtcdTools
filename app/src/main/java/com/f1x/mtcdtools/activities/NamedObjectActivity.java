package com.f1x.mtcdtools.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.f1x.mtcdtools.R;
import com.f1x.mtcdtools.storage.NamedObject;
import com.f1x.mtcdtools.storage.exceptions.DuplicatedEntryException;

import org.json.JSONException;

import java.io.IOException;

/**
 * Created by COMPUTER on 2017-02-05.
 */

public abstract class NamedObjectActivity extends ServiceActivity {
    public NamedObjectActivity(int layoutResId) {
        mLayoutResId = layoutResId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(mLayoutResId);

        mEditName = this.getIntent().getStringExtra(NAME_PARAMETER);
        mEditMode = mEditName != null;

        initControls();
    }

    @Override
    protected void onServiceConnected() {
        if(mEditMode) {
            NamedObject namedObject = mServiceBinder.getNamedObjectsStorage().getItem(mEditName);

            if (namedObject != null) {
                fillControls(namedObject);
            } else {
                Toast.makeText(this, this.getText(R.string.ObjectNotFound), Toast.LENGTH_LONG).show();
                finish();
            }
        }
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
                String namedObjectName = mNameEditText.getText().toString();

                if(namedObjectName.isEmpty()) {
                    Toast.makeText(NamedObjectActivity.this, NamedObjectActivity.this.getText(R.string.EmptyNameError), Toast.LENGTH_LONG).show();
                } else {
                    storeNamedObject(namedObjectName);
                }
            }
        });

        mNameEditText = (EditText)this.findViewById(R.id.editTextNamedObjectName);
    }

    protected void fillControls(NamedObject namedObject) {
        mNameEditText.setText(mEditMode ? namedObject.getName() : "");
    }

    private void storeNamedObject(String namedObjectName) {
        try {
            NamedObject namedObject = createNamedObject(namedObjectName);

            if(namedObject != null) {
                if(mEditMode) {
                    mServiceBinder.getNamedObjectsStorage().replace(mEditName, namedObjectName, namedObject);
                    mServiceBinder.getKeysSequenceBindingsStorage().replaceTargetName(mEditName, namedObjectName);
                } else {
                    mServiceBinder.getNamedObjectsStorage().insert(namedObjectName, namedObject);
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

    protected abstract NamedObject createNamedObject(String namedObjectName);

    protected String mEditName;
    protected boolean mEditMode;
    private final int mLayoutResId;
    protected EditText mNameEditText;

    public static final String NAME_PARAMETER = "name";
}
