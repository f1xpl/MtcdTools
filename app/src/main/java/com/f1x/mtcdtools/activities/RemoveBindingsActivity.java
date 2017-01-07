package com.f1x.mtcdtools.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.f1x.mtcdtools.R;
import com.f1x.mtcdtools.adapters.KeyInputArrayAdapter;
import com.f1x.mtcdtools.input.KeyInput;

public class RemoveBindingsActivity extends EditBindingsActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_bindings);

        mBindingsListView = (ListView)findViewById(R.id.listViewBindings);
        mKeyInputArrayAdapter = new KeyInputArrayAdapter(this);
        mBindingsListView.setAdapter(mKeyInputArrayAdapter);

        mBindingsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                KeyInput keyInput = mKeyInputArrayAdapter.getItem(position);
                if(keyInput != null) {
                    if(mServiceBinder.removeKeyInput(keyInput)) {
                        mBindingsListView.clearChoices();
                        mBindingsListView.requestLayout();
                        mKeyInputArrayAdapter.reset(mServiceBinder.getKeyInputs());
                    } else {
                        Toast.makeText(RemoveBindingsActivity.this, getString(R.string.KeyBindingRemovalFailure), Toast.LENGTH_LONG).show();
                    }
                }

                return true;
            }
        });
    }

    @Override
    protected void handleKeyInput(int keyCode) {
        mBindingsListView.clearChoices();
        mBindingsListView.requestLayout();

        for(int i = 0; i < mKeyInputArrayAdapter.getCount(); ++i) {
            KeyInput keyInput = mKeyInputArrayAdapter.getItem(i);

            if(keyInput.getKeyCode() == keyCode) {
                mBindingsListView.setItemChecked(i, true);
                mBindingsListView.requestFocusFromTouch();
                mBindingsListView.setSelection(i);
            }
        }
    }

    private ListView mBindingsListView;
    private KeyInputArrayAdapter mKeyInputArrayAdapter;
}
