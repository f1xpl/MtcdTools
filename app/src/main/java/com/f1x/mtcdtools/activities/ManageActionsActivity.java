package com.f1x.mtcdtools.activities;

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
import com.f1x.mtcdtools.activities.actions.BroadcastIntentActionActivity;
import com.f1x.mtcdtools.activities.actions.KeyActionActivity;
import com.f1x.mtcdtools.activities.actions.LaunchActionActivity;
import com.f1x.mtcdtools.activities.actions.StartActivityActionActivity;
import com.f1x.mtcdtools.adapters.ActionsArrayAdapter;

import org.json.JSONException;

import java.io.IOException;

public class ManageActionsActivity extends ServiceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_actions);

        ListView mActionsListView = (ListView)this.findViewById(R.id.listViewActions);
        mActionsArrayAdapter = new ActionsArrayAdapter(this);
        mActionsListView.setAdapter(mActionsArrayAdapter);

        mActionsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                try {
                    String actionName = mActionsArrayAdapter.getItem(position);
                    mServiceBinder.getActionsStorage().remove(actionName);
                    mActionsArrayAdapter.remove(actionName);
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
                String actionName = mActionsArrayAdapter.getItem(position);
                Action action = mServiceBinder.getActionsStorage().getAction(actionName);

                Intent intent = new Intent();
                intent.putExtra(KeyActionActivity.ACTION_NAME_PARAMETER, actionName);

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
                        Toast.makeText(ManageActionsActivity.this, ManageActionsActivity.this.getText(R.string.UnknownActionType), Toast.LENGTH_LONG).show();
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
            mActionsArrayAdapter.reset(mServiceBinder.getActionsStorage().getActions());
        }
    }

    @Override
    protected void onServiceConnected() {
        mActionsArrayAdapter.reset(mServiceBinder.getActionsStorage().getActions());
    }

    ActionsArrayAdapter mActionsArrayAdapter;
}
