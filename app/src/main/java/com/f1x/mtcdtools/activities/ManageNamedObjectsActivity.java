package com.f1x.mtcdtools.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.f1x.mtcdtools.named.objects.ActionsList;
import com.f1x.mtcdtools.R;
import com.f1x.mtcdtools.named.objects.ActionsSequence;
import com.f1x.mtcdtools.named.objects.actions.BroadcastIntentAction;
import com.f1x.mtcdtools.named.objects.actions.KeyAction;
import com.f1x.mtcdtools.named.objects.actions.LaunchAction;
import com.f1x.mtcdtools.named.objects.actions.StartActivityAction;
import com.f1x.mtcdtools.activities.actions.BroadcastIntentActionActivity;
import com.f1x.mtcdtools.activities.actions.KeyActionActivity;
import com.f1x.mtcdtools.activities.actions.LaunchActionActivity;
import com.f1x.mtcdtools.activities.actions.StartActivityActionActivity;
import com.f1x.mtcdtools.adapters.NamedObjectsArrayAdapter;
import com.f1x.mtcdtools.named.objects.NamedObject;

import org.json.JSONException;

import java.io.IOException;

public class ManageNamedObjectsActivity extends ServiceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_named_objects);

        ListView mNamedObjectsListView = (ListView)this.findViewById(R.id.listViewAddedNamedObjects);
        mNamedObjectsArrayAdapter = new NamedObjectsArrayAdapter(this);
        mNamedObjectsListView.setAdapter(mNamedObjectsArrayAdapter);

        mNamedObjectsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                try {
                    String namedObjectName = mNamedObjectsArrayAdapter.getItem(position);
                    mServiceBinder.getNamedObjectsStorage().remove(namedObjectName);
                    mServiceBinder.getKeysSequenceBindingsStorage().removeBindingWithTarget(namedObjectName);
                    mNamedObjectsArrayAdapter.remove(namedObjectName);
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
                String namedObjectName = mNamedObjectsArrayAdapter.getItem(position);
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
                    case ActionsSequence.OBJECT_TYPE:
                        intent.setClass(ManageNamedObjectsActivity.this, ActionsSequenceActivity.class);
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
            mNamedObjectsArrayAdapter.reset(mServiceBinder.getNamedObjectsStorage().getItems());
        }
    }

    @Override
    protected void onServiceConnected() {
        mNamedObjectsArrayAdapter.reset(mServiceBinder.getNamedObjectsStorage().getItems());
    }

    NamedObjectsArrayAdapter mNamedObjectsArrayAdapter;
}
