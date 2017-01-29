package com.f1x.mtcdtools.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.f1x.mtcdtools.R;
import com.f1x.mtcdtools.adapters.NamesArrayAdapter;

import org.json.JSONException;

import java.io.IOException;

public class ManageActionsListsActivity extends ServiceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_actions_lists);

        mActionsListsNamesArrayAdapter = new NamesArrayAdapter(this);
        ListView actionsListsListView = (ListView)this.findViewById(R.id.listViewActionsLists);
        actionsListsListView.setAdapter(mActionsListsNamesArrayAdapter);

        actionsListsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                try {
                    String actionName = mActionsListsNamesArrayAdapter.getItem(position);
                    mServiceBinder.getActionsListsStorage().remove(actionName);
                    mActionsListsNamesArrayAdapter.remove(actionName);
                } catch(IOException | JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ManageActionsListsActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }

                return true;
            }
        });

        actionsListsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String actionName = mActionsListsNamesArrayAdapter.getItem(position);

                Intent intent = new Intent(ManageActionsListsActivity.this, ActionsListActivity.class);
                intent.putExtra(ActionsListActivity.ACTIONS_LIST_NAME_PARAMETER, actionName);
                ManageActionsListsActivity.this.startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(mServiceBinder != null) {
            mActionsListsNamesArrayAdapter.reset(mServiceBinder.getActionsListsStorage().getItems().keySet());
        }
    }

    @Override
    protected void onServiceConnected() {
        mActionsListsNamesArrayAdapter.reset(mServiceBinder.getActionsListsStorage().getItems().keySet());
    }

    NamesArrayAdapter mActionsListsNamesArrayAdapter;
}
