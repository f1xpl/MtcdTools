package com.f1x.mtcdtools.activities.actions;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.f1x.mtcdtools.R;
import com.f1x.mtcdtools.actions.Action;
import com.f1x.mtcdtools.actions.BroadcastIntentAction;
import com.f1x.mtcdtools.actions.KeyAction;
import com.f1x.mtcdtools.actions.LaunchAction;
import com.f1x.mtcdtools.actions.StartActivityAction;
import com.f1x.mtcdtools.activities.ServiceActivity;
import com.f1x.mtcdtools.adapters.NamesArrayAdapter;

import org.json.JSONException;

import java.io.IOException;

public class ManageActionsActivity extends ServiceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_actions);

        ListView mActionsListView = (ListView)this.findViewById(R.id.listViewActions);
        mActionsNamesArrayAdapter = new NamesArrayAdapter(this);
        mActionsListView.setAdapter(mActionsNamesArrayAdapter);

        mActionsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                try {
                    String actionName = mActionsNamesArrayAdapter.getItem(position);
                    mServiceBinder.getActionsStorage().remove(actionName);
                    mServiceBinder.getActionsListsStorage().removeActionFromActionsList(actionName);
                    mServiceBinder.getKeysSequenceBindingsStorage().removeBindingsWithAction(actionName);
                    mActionsNamesArrayAdapter.remove(actionName);
                } catch(IOException | JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ManageActionsActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }

                return true;
            }
        });

        mActionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String actionName = mActionsNamesArrayAdapter.getItem(position);
                Action action = mServiceBinder.getActionsStorage().getItem(actionName);

                Intent intent = new Intent();
                intent.putExtra(ActionActivity.ACTION_NAME_PARAMETER, actionName);

                switch(action.getType()) {
                    case KeyAction.ACTION_TYPE:
                        intent.setClass(ManageActionsActivity.this, KeyActionActivity.class);
                        break;
                    case LaunchAction.ACTION_TYPE:
                        intent.setClass(ManageActionsActivity.this, LaunchActionActivity.class);
                        break;
                    case BroadcastIntentAction.ACTION_TYPE:
                        intent.setClass(ManageActionsActivity.this, BroadcastIntentActionActivity.class);
                        break;
                    case StartActivityAction.ACTION_TYPE:
                        intent.setClass(ManageActionsActivity.this, StartActivityActionActivity.class);
                        break;
                    default:
                        Toast.makeText(ManageActionsActivity.this, ManageActionsActivity.this.getText(R.string.UnknownObjectType), Toast.LENGTH_LONG).show();
                        return;
                }

                ManageActionsActivity.this.startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(mServiceBinder != null) {
            mActionsNamesArrayAdapter.reset(mServiceBinder.getActionsStorage().getItems().keySet());
        }
    }

    @Override
    protected void onServiceConnected() {
        mActionsNamesArrayAdapter.reset(mServiceBinder.getActionsStorage().getItems().keySet());
    }

    NamesArrayAdapter mActionsNamesArrayAdapter;
}
