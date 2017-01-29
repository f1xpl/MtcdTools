package com.f1x.mtcdtools.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.f1x.mtcdtools.R;
import com.f1x.mtcdtools.adapters.KeysSequenceArrayAdapter;
import com.f1x.mtcdtools.adapters.NamesArrayAdapter;
import com.f1x.mtcdtools.input.KeysSequenceBinding;
import com.f1x.mtcdtools.input.KeysSequenceConverter;
import com.f1x.mtcdtools.storage.exceptions.DuplicatedEntryException;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class BindingActivity extends ServiceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binding_details);

        mNamesSpinner = (Spinner)this.findViewById(R.id.spinnerNames);
        mNamesArrayAdapter = new NamesArrayAdapter(this);
        mNamesSpinner.setAdapter(mNamesArrayAdapter);

        ListView keysSequenceListView = (ListView)this.findViewById(R.id.listViewKeysSequence);
        mKeysSequenceArrayAdapter = new KeysSequenceArrayAdapter(this);
        keysSequenceListView.setAdapter(mKeysSequenceArrayAdapter);

        mActionRadioButton = (RadioButton)this.findViewById(R.id.radioButtonAction);
        mActionRadioButton.setChecked(true);
        mActionRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if(checked && mServiceBinder != null) {
                    mNamesArrayAdapter.reset(mServiceBinder.getActionsStorage().getItems().keySet());
                }
            }
        });

        mActionsListRadioButton = (RadioButton)this.findViewById(R.id.radioButtonActionsList);
        mActionsListRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if(checked && mServiceBinder != null) {
                    mNamesArrayAdapter.reset(mServiceBinder.getActionsListsStorage().getItems().keySet());
                }
            }
        });

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
                storeKeysSequenceBinding();
            }
        });

        Button obtainKeysSequenceButton = (Button)this.findViewById(R.id.buttonObtainKeysSequence);
        obtainKeysSequenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(BindingActivity.this, ObtainKeysSequenceActivity.class), 0);
            }
        });
    }

    protected void storeKeysSequenceBinding() {
        List<Integer> keysSequence = mKeysSequenceArrayAdapter.getItems();

        if(keysSequence.isEmpty()) {
            Toast.makeText(this, this.getText(R.string.ProvideKeysSequence), Toast.LENGTH_LONG).show();
            return;
        }

        try {
            String targetType = mActionRadioButton.isChecked() ? KeysSequenceBinding.TARGET_TYPE_ACTION : KeysSequenceBinding.TARGET_TYPE_ACTIONS_LIST;
            String targetName = (String)mNamesSpinner.getSelectedItem();
            KeysSequenceBinding keysSequenceBinding = new KeysSequenceBinding(keysSequence, targetType, targetName);
            mServiceBinder.getKeysSequenceBindingsStorage().insert(keysSequence, keysSequenceBinding);
            finish();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (DuplicatedEntryException e) {
            e.printStackTrace();
            Toast.makeText(this, this.getText(R.string.ObjectAlreadyAdded), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onServiceConnected() {
        if(mActionsListRadioButton.isChecked()) {
            mNamesArrayAdapter.reset(mServiceBinder.getActionsListsStorage().getItems().keySet());
        } else if(mActionRadioButton.isChecked()) {
            mNamesArrayAdapter.reset(mServiceBinder.getActionsStorage().getItems().keySet());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == ObtainKeysSequenceActivity.RESULT_CANCELED) {
            return;
        }

        List<Integer> keysSequence = KeysSequenceConverter.fromArray(data.getIntArrayExtra(ObtainKeysSequenceActivity.RESULT_NAME));
        mKeysSequenceArrayAdapter.reset(keysSequence);
    }

    private RadioButton mActionRadioButton;
    private RadioButton mActionsListRadioButton;
    private Spinner mNamesSpinner;
    private NamesArrayAdapter mNamesArrayAdapter;
    KeysSequenceArrayAdapter mKeysSequenceArrayAdapter;
}
