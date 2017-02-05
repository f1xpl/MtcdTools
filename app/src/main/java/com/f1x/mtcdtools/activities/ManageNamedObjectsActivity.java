package com.f1x.mtcdtools.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.f1x.mtcdtools.ActionsList;
import com.f1x.mtcdtools.R;
import com.f1x.mtcdtools.actions.BroadcastIntentAction;
import com.f1x.mtcdtools.actions.KeyAction;
import com.f1x.mtcdtools.actions.LaunchAction;
import com.f1x.mtcdtools.actions.StartActivityAction;
import com.f1x.mtcdtools.activities.actions.BroadcastIntentActionActivity;
import com.f1x.mtcdtools.activities.actions.KeyActionActivity;
import com.f1x.mtcdtools.activities.actions.LaunchActionActivity;
import com.f1x.mtcdtools.activities.actions.StartActivityActionActivity;
import com.f1x.mtcdtools.adapters.NamesArrayAdapter;
import com.f1x.mtcdtools.storage.NamedObject;

import org.json.JSONException;

import java.io.IOException;

public class ManageNamedObjectsActivity extends ServiceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_named_objects);

        ListView mNamedObjectsListView = (ListView)this.findViewById(R.id.listViewAddedNamedObjects);
        mNamesArrayAdapter = new NamesArrayAdapter(this);
        mNamedObjectsListView.setAdapter(mNamesArrayAdapter);

        mNamedObjectsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                try {
                    String namedObjectName = mNamesArrayAdapter.getItem(position);
                    mServiceBinder.getNamedObjectsStorage().remove(namedObjectName);
                    mServiceBinder.getKeysSequenceBindingsStorage().removeBindingWithTarget(namedObjectName);
                    mNamesArrayAdapter.remove(namedObjectName);
                } catch(IOException | JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ManageNamedObjectsActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }

                return true;
            }
        });

        mNamedObjectsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String namedObjectName = mNamesArrayAdapter.getItem(position);
                NamedObject namedObject = mServiceBinder.getNamedObjectsStorage().getItem(namedObjectName);

                Intent intent = new Intent();
                intent.putExtra(NamedObjectActivity.NAME_PARAMETER, namedObjectName);

                switch(namedObject.getObjectType()) {
                    case KeyAction.OBJECT_TYPE:
                        intent.setClass(ManageNamedObjectsActivity.this, KeyActionActivity.class);
                        break;
                    case LaunchAction.OBJECT_TYPE:
                        intent.setClass(ManageNamedObjectsActivity.this, LaunchActionActivity.class);
                        break;
                    case BroadcastIntentAction.OBJECT_TYPE:
                        intent.setClass(ManageNamedObjectsActivity.this, BroadcastIntentActionActivity.class);
                        break;
                    case StartActivityAction.OBJECT_TYPE:
                        intent.setClass(ManageNamedObjectsActivity.this, StartActivityActionActivity.class);
                        break;
                    case ActionsList.OBJECT_TYPE:
                        intent.setClass(ManageNamedObjectsActivity.this, ActionsListActivity.class);
                        break;
                    default:
                        Toast.makeText(ManageNamedObjectsActivity.this, ManageNamedObjectsActivity.this.getText(R.string.UnknownObjectType), Toast.LENGTH_LONG).show();
                        return;
                }

                ManageNamedObjectsActivity.this.startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(mServiceBinder != null) {
            mNamesArrayAdapter.reset(mServiceBinder.getNamedObjectsStorage().getItems().keySet());
        }
    }

    @Override
    protected void onServiceConnected() {
        mNamesArrayAdapter.reset(mServiceBinder.getNamedObjectsStorage().getItems().keySet());
    }

    NamesArrayAdapter mNamesArrayAdapter;
}
