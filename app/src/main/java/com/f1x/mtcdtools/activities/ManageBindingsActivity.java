package com.f1x.mtcdtools.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.f1x.mtcdtools.R;
import com.f1x.mtcdtools.adapters.NamesArrayAdapter;
import com.f1x.mtcdtools.input.KeysSequenceBinding;
import com.f1x.mtcdtools.input.KeysSequenceConverter;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by COMPUTER on 2017-02-01.
 */

public class ManageBindingsActivity extends ServiceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_bindings);

        mBindingsArrayAdapter = new NamesArrayAdapter(this);
        ListView bindingsListView = (ListView)this.findViewById(R.id.listViewBindings);
        bindingsListView.setAdapter(mBindingsArrayAdapter);

        bindingsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                try {
                    String keysSequenceString = mBindingsArrayAdapter.getItem(position);
                    List<Integer> keysSequence = KeysSequenceConverter.fromJsonArray(new JSONArray());

                    mServiceBinder.getKeysSequenceBindingsStorage().remove(keysSequence);
                    mBindingsArrayAdapter.remove(keysSequenceString);
                } catch(IOException | JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ManageBindingsActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }

                return true;
            }
        });

        bindingsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String keysSequenceString = mBindingsArrayAdapter.getItem(position);

                Intent intent = new Intent(ManageBindingsActivity.this, BindingActivity.class);
                intent.putExtra(BindingActivity.KEYS_SEQUENCE_NAME_PARAMETER, keysSequenceString);
                ManageBindingsActivity.this.startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(mServiceBinder != null) {
            mBindingsArrayAdapter.reset(bindingsToStringList(mServiceBinder.getKeysSequenceBindingsStorage().getItems()));
        }
    }

    @Override
    protected void onServiceConnected() {
        mBindingsArrayAdapter.reset(bindingsToStringList(mServiceBinder.getKeysSequenceBindingsStorage().getItems()));
    }

    Set<String> bindingsToStringList(Map<List<Integer>, KeysSequenceBinding> bindings) {
        Set<String> bindingsList = new HashSet<>();

        for(List<Integer> keysSequence : bindings.keySet()) {
            JSONArray jsonArray = KeysSequenceConverter.toJsonArray(keysSequence);
            bindingsList.add(jsonArray.toString());
        }

        return bindingsList;
    }

    NamesArrayAdapter mBindingsArrayAdapter;
}
