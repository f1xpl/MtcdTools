package android.microntek.f1x.mtcdtools.activities.managing;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import android.microntek.f1x.mtcdtools.R;
import android.microntek.f1x.mtcdtools.service.ServiceActivity;
import android.microntek.f1x.mtcdtools.adapters.NamedObjectIdsArrayAdapter;
import android.microntek.f1x.mtcdtools.named.objects.containers.ActionsSequence;
import android.microntek.f1x.mtcdtools.named.NamedObjectId;
import android.microntek.f1x.mtcdtools.named.objects.actions.BroadcastIntentAction;
import android.microntek.f1x.mtcdtools.named.objects.actions.KeyAction;
import android.microntek.f1x.mtcdtools.named.objects.actions.LaunchAction;
import android.microntek.f1x.mtcdtools.named.objects.actions.StartIntentAction;

import org.json.JSONException;

import java.io.IOException;
import java.util.Arrays;
import java.util.TreeSet;

/**
 * Created by COMPUTER on 2017-02-26.
 */

public class ManageAutorunActivity extends ServiceActivity {
    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_manage_autorun);

        mNamedObjectIdsArrayAdapter = new NamedObjectIdsArrayAdapter(this);
        mNamedObjectIdsArrayAdapter.setObjectTypeFilters(new TreeSet<>(Arrays.asList(ActionsSequence.OBJECT_TYPE, KeyAction.OBJECT_TYPE, LaunchAction.OBJECT_TYPE, BroadcastIntentAction.OBJECT_TYPE, StartIntentAction.OBJECT_TYPE)));

        mNamedObjectsSpinner = (Spinner)this.findViewById(R.id.spinnerNamedObjects);
        mNamedObjectsSpinner.setAdapter(mNamedObjectIdsArrayAdapter);

        ListView addedActionsListView = (ListView)this.findViewById(R.id.listViewAddedNamedObjects);
        mAddedNamedObjectIdsArrayAdapter = new NamedObjectIdsArrayAdapter(this);
        addedActionsListView.setAdapter(mAddedNamedObjectIdsArrayAdapter);
        addedActionsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                NamedObjectId removedObjectId = mAddedNamedObjectIdsArrayAdapter.getItem(position);

                try {
                    mServiceBinder.getAutorunStorage().remove(removedObjectId);
                    mAddedNamedObjectIdsArrayAdapter.removeAt(position);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ManageAutorunActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }

                return true;
            }
        });

        Button addNamedObjectButton = (Button)this.findViewById(R.id.buttonAddNamedObject);
        addNamedObjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NamedObjectId addedObjectId = (NamedObjectId)mNamedObjectsSpinner.getSelectedItem();

                try {
                    mServiceBinder.getAutorunStorage().insert(addedObjectId);
                    mAddedNamedObjectIdsArrayAdapter.add(addedObjectId);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ManageAutorunActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onServiceConnected() {
        mNamedObjectIdsArrayAdapter.reset(mServiceBinder.getNamedObjectsStorage().getItems());
        mAddedNamedObjectIdsArrayAdapter.reset(mServiceBinder.getAutorunStorage().getItems());
    }

    private NamedObjectIdsArrayAdapter mNamedObjectIdsArrayAdapter;
    private NamedObjectIdsArrayAdapter mAddedNamedObjectIdsArrayAdapter;
    private Spinner mNamedObjectsSpinner;
}
