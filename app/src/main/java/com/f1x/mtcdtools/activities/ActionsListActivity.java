package com.f1x.mtcdtools.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.f1x.mtcdtools.ActionsList;
import com.f1x.mtcdtools.R;
import com.f1x.mtcdtools.adapters.ActionsNamesArrayAdapter;
import com.f1x.mtcdtools.adapters.KeysSequenceArrayAdapter;
import com.f1x.mtcdtools.input.KeysSequenceConverter;

import org.json.JSONException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ActionsListActivity extends ServiceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actions_list_details);

        mKeysSequenceUpArrayAdapter = new KeysSequenceArrayAdapter(this);
        ListView mKeysSequenceUpListView = (ListView)this.findViewById(R.id.listViewKeysSequenceUp);
        mKeysSequenceUpListView.setAdapter(mKeysSequenceUpArrayAdapter);

        mKeysSequenceDownArrayAdapter = new KeysSequenceArrayAdapter(this);
        ListView mKeysSequenceDownListView = (ListView)this.findViewById(R.id.listViewKeysSequenceDown);
        mKeysSequenceDownListView.setAdapter(mKeysSequenceDownArrayAdapter);

        ListView addedActionsListView = (ListView)this.findViewById(R.id.listViewActions);
        mAddedActionsNamesArrayAdapter = new ActionsNamesArrayAdapter(this);
        addedActionsListView.setAdapter(mAddedActionsNamesArrayAdapter);

        mActionsNamesArrayAdapter = new ActionsNamesArrayAdapter(this);
        final Spinner mActionsSpinner = (Spinner)this.findViewById(R.id.spinnerActions);
        mActionsSpinner.setAdapter(mActionsNamesArrayAdapter);

        mEditActionsListName = this.getIntent().getStringExtra(ACTIONS_LIST_NAME_PARAMETER);
        mEditMode = mEditActionsListName != null;

        mActionsListName = (EditText)this.findViewById(R.id.editTextActionsListName);

        Button cancelButton = (Button)this.findViewById(R.id.buttonCancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActionsListActivity.this.finish();
            }
        });

        Button addActionButton = (Button)this.findViewById(R.id.buttonAddAction);
        addActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String actionName = (String)mActionsSpinner.getSelectedItem();

                if(!mAddedActionsNamesArrayAdapter.containsAction(actionName)) {
                    mAddedActionsNamesArrayAdapter.add(actionName);
                } else {
                    Toast.makeText(ActionsListActivity.this, ActionsListActivity.this.getText(R.string.ActionAlreadyAdded), Toast.LENGTH_LONG).show();
                }
            }
        });

        Button obtainKeysSequenceUpButton = (Button)this.findViewById(R.id.buttonObtainKeysSequenceUp);
        obtainKeysSequenceUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(ActionsListActivity.this, ObtainKeysSequenceActivity.class), REQUEST_CODE_KEYS_SEQUENCE_UP);
            }
        });

        Button obtainKeysSequenceDownButton = (Button)this.findViewById(R.id.buttonObtainKeysSequenceDown);
        obtainKeysSequenceDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(ActionsListActivity.this, ObtainKeysSequenceActivity.class), REQUEST_CODE_KEYS_SEQUENCE_DOWN);
            }
        });
    }

    @Override
    protected void onServiceConnected() {
        if(mEditMode) {
            ActionsList actionsList = mServiceBinder.getActionsListStorage().getActionsList(mEditActionsListName);

            if(actionsList != null) {
                mAddedActionsNamesArrayAdapter.reset(actionsList.getActionNames());
                mKeysSequenceDownArrayAdapter.reset(actionsList.getKeysSequenceDown());
                mKeysSequenceUpArrayAdapter.reset(actionsList.getKeysSequenceUp());
                mActionsListName.setText(actionsList.getName());

            }
        }

        mActionsNamesArrayAdapter.reset(mServiceBinder.getActionsStorage().getActions().keySet());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == ObtainKeysSequenceActivity.RESULT_CANCELED) {
            return;
        }

        List<Integer> keysSequence = KeysSequenceConverter.fromArray(data.getIntArrayExtra(ObtainKeysSequenceActivity.RESULT_NAME));

        if(requestCode == REQUEST_CODE_KEYS_SEQUENCE_UP) {
            mKeysSequenceUpArrayAdapter.reset(keysSequence);
        } else if(requestCode == REQUEST_CODE_KEYS_SEQUENCE_DOWN) {
            mKeysSequenceDownArrayAdapter.reset(keysSequence);
        }
    }

    private String mEditActionsListName;
    private boolean mEditMode;

    EditText mActionsListName;
    KeysSequenceArrayAdapter mKeysSequenceUpArrayAdapter;
    KeysSequenceArrayAdapter mKeysSequenceDownArrayAdapter;
    ActionsNamesArrayAdapter mActionsNamesArrayAdapter;
    ActionsNamesArrayAdapter mAddedActionsNamesArrayAdapter;

    public static final String ACTIONS_LIST_NAME_PARAMETER = "actionName";
    private static int REQUEST_CODE_KEYS_SEQUENCE_UP = 100;
    private static int REQUEST_CODE_KEYS_SEQUENCE_DOWN = 101;
}
