package com.f1x.mtcdtools.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.f1x.mtcdtools.R;
import com.f1x.mtcdtools.input.KeysSequenceListener;

import java.util.List;

public class ObtainKeysSequenceActivity extends ServiceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obtain_keys_sequence);

        mPressedKeysArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        ListView keysSequenceListView = (ListView)this.findViewById(R.id.listViewKeysSequence);
        keysSequenceListView.setAdapter(mPressedKeysArrayAdapter);

        Button resetButton = (Button)this.findViewById(R.id.buttonReset);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPressedKeysArrayAdapter.clear();
            }
        });

        Button saveButton = (Button)this.findViewById(R.id.buttonSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int keysSequence[] = new int[mPressedKeysArrayAdapter.getCount()];

                for(int i = 0; i < mPressedKeysArrayAdapter.getCount(); ++i) {
                    keysSequence[i] = mPressedKeysArrayAdapter.getItem(i);
                }

                Intent resultIntent = new Intent();
                resultIntent.putExtra(RESULT_NAME, keysSequence);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        Button cancelButton = (Button)this.findViewById(R.id.buttonCancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    @Override
    protected void onServiceConnected() {
        mServiceBinder.getPressedKeysSequenceManager().pushListener(mKeysSequenceListener);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(mServiceBinder != null) {
            mServiceBinder.getPressedKeysSequenceManager().pushListener(mKeysSequenceListener);
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
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    KeysSequenceListener mKeysSequenceListener = new KeysSequenceListener() {
        @Override
        public void handleKeysSequence(List<Integer> keysSequence) {
        }

        @Override
        public void handleSingleKey(int keyCode) {
            mPressedKeysArrayAdapter.add(keyCode);
        }
    };

    private ArrayAdapter<Integer> mPressedKeysArrayAdapter;

    public static final int RESULT_OK = 0;
    public static final int RESULT_CANCELED = 1;
    public static final String RESULT_NAME = "keysSequence";
}
