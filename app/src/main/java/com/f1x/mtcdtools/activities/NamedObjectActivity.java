package com.f1x.mtcdtools.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.f1x.mtcdtools.R;
import com.f1x.mtcdtools.named.objects.NamedObject;
import com.f1x.mtcdtools.named.objects.NamedObjectId;
import com.f1x.mtcdtools.storage.exceptions.DuplicatedEntryException;

import org.json.JSONException;

import java.io.IOException;

/**
 * Created by f1x on 2017-02-05.
 */

public abstract class NamedObjectActivity extends ServiceActivity {
    public NamedObjectActivity(int layoutResId) {
        mLayoutResId = layoutResId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(mLayoutResId);

        mEditNamedObjectId = this.getIntent().getParcelableExtra(NAMED_OBJECT_ID_PARAMETER);
        mEditMode = mEditNamedObjectId != null;

        initControls();
    }

    @Override
    protected void onServiceConnected() {
        if(mEditMode) {
            NamedObject namedObject = mServiceBinder.getNamedObjectsStorage().getItem(mEditNamedObjectId);

            if (namedObject == null) {
                Toast.makeText(this, this.getText(R.string.ObjectNotFound), Toast.LENGTH_LONG).show();
                finish();
                return;
            }

            try {
                fillControls(namedObject);
            } catch(ClassCastException e) {
                e.printStackTrace();
                Toast.makeText(this, this.getText(R.string.UnknownObjectType), Toast.LENGTH_LONG).show();
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
                    storeNamedObject(new NamedObjectId(namedObjectName));
                }
            }
        });

        mNameEditText = (EditText)this.findViewById(R.id.editTextNamedObjectName);
    }

    protected void fillControls(NamedObject namedObject) throws ClassCastException {
        mNameEditText.setText(mEditMode ? namedObject.getId().toString() : "");
    }

    private void storeNamedObject(NamedObjectId namedObjectId) {
        try {
            NamedObject namedObject = createNamedObject(namedObjectId);

            if(namedObject != null) {
                if(mEditMode) {
                    mServiceBinder.getNamedObjectsStorage().replace(mEditNamedObjectId, namedObjectId, namedObject);
                    mServiceBinder.getKeysSequenceBindingsStorage().replaceTarget(mEditNamedObjectId, namedObjectId);
                } else {
                    mServiceBinder.getNamedObjectsStorage().insert(namedObjectId, namedObject);
                }

                finish();
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        } catch (DuplicatedEntryException e) {
            e.printStackTrace();
            Toast.makeText(this, this.getText(R.string.ObjectAlreadyAdded), Toast.LENGTH_LONG).show();
        }
    }

    protected abstract NamedObject createNamedObject(NamedObjectId namedObjectId);

    private NamedObjectId mEditNamedObjectId;
    private boolean mEditMode;
    private final int mLayoutResId;
    protected EditText mNameEditText;

    public static final String NAMED_OBJECT_ID_PARAMETER = "namedObjectId";
}
