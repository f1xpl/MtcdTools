package com.microntek.f1x.mtcdtools.activities.managing;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.microntek.f1x.mtcdtools.R;
import com.microntek.f1x.mtcdtools.activities.input.BindingActivity;
import com.microntek.f1x.mtcdtools.service.ServiceActivity;
import com.microntek.f1x.mtcdtools.service.input.KeysSequenceBinding;
import com.microntek.f1x.mtcdtools.utils.KeysSequenceConverter;
import com.microntek.f1x.mtcdtools.service.input.KeysSequenceListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by f1x on 2017-02-01.
 */

public class ManageBindingsActivity extends ServiceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_bindings);

        mBindingsArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_checked);
        mBindingsListView = (ListView)this.findViewById(R.id.listViewBindings);
        mBindingsListView.setAdapter(mBindingsArrayAdapter);

        mBindingsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                try {
                    String keysSequenceString = mBindingsArrayAdapter.getItem(position);
                    List<Integer> keysSequence = KeysSequenceConverter.fromJsonArray(new JSONArray(keysSequenceString));

                    mServiceBinder.getKeysSequenceBindingsStorage().remove(keysSequence);
                    mBindingsArrayAdapter.remove(keysSequenceString);
                } catch(IOException | JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ManageBindingsActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }

                return true;
            }
        });

        mBindingsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

        mBindingsListView.clearChoices();
        mBindingsListView.requestLayout();

        if(mServiceBinder != null) {
            mServiceBinder.getPressedKeysSequenceManager().pushListener(mKeysSequenceListener);
            mBindingsArrayAdapter.clear();
            mBindingsArrayAdapter.addAll(bindingsToStringList(mServiceBinder.getKeysSequenceBindingsStorage().getItems()));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(mServiceBinder != null) {
            mServiceBinder.getPressedKeysSequenceManager().popListener(mKeysSequenceListener);
        }
    }

    @Override
    protected void onServiceConnected() {
        mServiceBinder.getPressedKeysSequenceManager().pushListener(mKeysSequenceListener);

        mBindingsArrayAdapter.clear();
        mBindingsArrayAdapter.addAll(bindingsToStringList(mServiceBinder.getKeysSequenceBindingsStorage().getItems()));
    }

    private Set<String> bindingsToStringList(Map<List<Integer>, KeysSequenceBinding> bindings) {
        Set<String> bindingsList = new HashSet<>();

        for(List<Integer> keysSequence : bindings.keySet()) {
            JSONArray jsonArray = KeysSequenceConverter.toJsonArray(keysSequence);
            bindingsList.add(jsonArray.toString());
        }

        return bindingsList;
    }

    private final KeysSequenceListener mKeysSequenceListener = new KeysSequenceListener() {
        @Override
        public void handleKeysSequence(List<Integer> keysSequence) {
            mBindingsListView.clearChoices();
            mBindingsListView.requestLayout();
            int position = mBindingsArrayAdapter.getPosition(KeysSequenceConverter.toJsonArray(keysSequence).toString());

            if(position != -1) {
                mBindingsListView.setItemChecked(position, true);
                mBindingsListView.requestFocusFromTouch();
                mBindingsListView.setSelection(position);
            }
        }

        @Override
        public void handleSingleKey(int keyCode) {

        }
    };

    private ListView mBindingsListView;
    private ArrayAdapter<String> mBindingsArrayAdapter;
}
