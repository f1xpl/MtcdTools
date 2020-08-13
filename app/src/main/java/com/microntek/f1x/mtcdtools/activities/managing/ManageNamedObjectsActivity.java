package com.microntek.f1x.mtcdtools.activities.managing;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.microntek.f1x.mtcdtools.R;
import com.microntek.f1x.mtcdtools.activities.named.objects.ActionsListActivity;
import com.microntek.f1x.mtcdtools.activities.named.objects.ActionsSequenceActivity;
import com.microntek.f1x.mtcdtools.activities.named.objects.ModeListActivity;
import com.microntek.f1x.mtcdtools.activities.named.objects.NamedObjectActivity;
import com.microntek.f1x.mtcdtools.service.ServiceActivity;
import com.microntek.f1x.mtcdtools.activities.named.objects.actions.BroadcastIntentActionActivity;
import com.microntek.f1x.mtcdtools.activities.named.objects.actions.KeyActionActivity;
import com.microntek.f1x.mtcdtools.activities.named.objects.actions.LaunchActionActivity;
import com.microntek.f1x.mtcdtools.activities.named.objects.actions.StartIntentActionActivity;
import com.microntek.f1x.mtcdtools.adapters.NamedObjectIdsArrayAdapter;
import com.microntek.f1x.mtcdtools.named.objects.containers.ActionsList;
import com.microntek.f1x.mtcdtools.named.objects.containers.ActionsSequence;
import com.microntek.f1x.mtcdtools.named.objects.containers.ModeList;
import com.microntek.f1x.mtcdtools.named.NamedObject;
import com.microntek.f1x.mtcdtools.named.NamedObjectId;
import com.microntek.f1x.mtcdtools.named.objects.actions.BroadcastIntentAction;
import com.microntek.f1x.mtcdtools.named.objects.actions.KeyAction;
import com.microntek.f1x.mtcdtools.named.objects.actions.LaunchAction;
import com.microntek.f1x.mtcdtools.named.objects.actions.StartIntentAction;

import org.json.JSONException;

import java.io.IOException;

public class ManageNamedObjectsActivity extends ServiceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_named_objects);

        ListView mNamedObjectsListView = (ListView)this.findViewById(R.id.listViewAddedNamedObjects);
        mNamedObjectIdsArrayAdapter = new NamedObjectIdsArrayAdapter(this);
        mNamedObjectsListView.setAdapter(mNamedObjectIdsArrayAdapter);

        mNamedObjectsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                try {
                    NamedObjectId namedObjectId = mNamedObjectIdsArrayAdapter.getItem(position);
                    mServiceBinder.getNamedObjectsStorage().remove(namedObjectId);
                    mServiceBinder.getKeysSequenceBindingsStorage().removeBindingWithTarget(namedObjectId);
                    mServiceBinder.getAutorunStorage().remove(namedObjectId);
                    mNamedObjectIdsArrayAdapter.remove(namedObjectId);
                } catch(IOException | JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ManageNamedObjectsActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }

                return true;
            }
        });

        mNamedObjectsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                NamedObjectId namedObjectId = mNamedObjectIdsArrayAdapter.getItem(position);
                NamedObject namedObject = mServiceBinder.getNamedObjectsStorage().getItem(namedObjectId);

                Intent intent = new Intent();
                intent.putExtra(NamedObjectActivity.NAMED_OBJECT_ID_PARAMETER, namedObjectId);

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
                    case StartIntentAction.OBJECT_TYPE:
                        intent.setClass(ManageNamedObjectsActivity.this, StartIntentActionActivity.class);
                        break;
                    case ActionsList.OBJECT_TYPE:
                        intent.setClass(ManageNamedObjectsActivity.this, ActionsListActivity.class);
                        break;
                    case ActionsSequence.OBJECT_TYPE:
                        intent.setClass(ManageNamedObjectsActivity.this, ActionsSequenceActivity.class);
                        break;
                    case ModeList.OBJECT_TYPE:
                        intent.setClass(ManageNamedObjectsActivity.this, ModeListActivity.class);
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
            mNamedObjectIdsArrayAdapter.reset(mServiceBinder.getNamedObjectsStorage().getItems());
        }
    }

    @Override
    protected void onServiceConnected() {
        mNamedObjectIdsArrayAdapter.reset(mServiceBinder.getNamedObjectsStorage().getItems());
    }

    private NamedObjectIdsArrayAdapter mNamedObjectIdsArrayAdapter;
}
