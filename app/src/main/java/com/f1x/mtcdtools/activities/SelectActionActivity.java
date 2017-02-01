package com.f1x.mtcdtools.activities;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.f1x.mtcdtools.ActionsList;
import com.f1x.mtcdtools.ListIndexer;
import com.f1x.mtcdtools.R;
import com.f1x.mtcdtools.actions.Action;
import com.f1x.mtcdtools.input.KeysSequenceListener;

import java.util.List;

public class SelectActionActivity extends ServiceActivity implements KeysSequenceListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_action);

        mActionsListName = this.getIntent().getStringExtra(ACTIONS_LIST_NAME_PARAMETER);
        mActionsNamesArrayAdapter = new ArrayAdapter<>(this, R.layout.layout_action_name_row);

        mActionsListView = (ListView)this.findViewById(R.id.listViewActions);
        mActionsListView.setAdapter(mActionsNamesArrayAdapter);

        mListIndexer = new ListIndexer();
        mActionExecutionTimer.start();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(mServiceBinder != null) {
            mServiceBinder.getPressedKeysSequenceManager().pushListener(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(mServiceBinder != null) {
            mServiceBinder.getPressedKeysSequenceManager().popListener(this);
        }
    }

    @Override
    protected void onServiceConnected() {
        if(mActionsListName != null) {
            mActionsList = mServiceBinder.getActionsListsStorage().getItem(mActionsListName);

            if(mActionsList != null) {
                mServiceBinder.getPressedKeysSequenceManager().pushListener(this);
                mActionsNamesArrayAdapter.clear();
                mActionsNamesArrayAdapter.addAll(mActionsList.getActionNames());
                mActionsNamesArrayAdapter.insert(this.getText(R.string.Cancel).toString(), 0);
                mListIndexer.reset(mActionsNamesArrayAdapter.getCount());

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mActionsListView.clearFocus();
                        mActionsListView.requestFocusFromTouch();
                        mActionsListView.setSelection(0);
                    }
                }, 200); //that is insane... Sometimes the listview is not yet drawed.

                return;
            }
        }

        Toast.makeText(this, this.getText(R.string.UnknownObjectType), Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void handleKeysSequence(List<Integer> keysSequence) {
        if(mActionsList != null) {
            try {
                int index;

                if (mActionsList.getKeysSequenceDown().equals(keysSequence)) {
                    index = mListIndexer.down();
                } else if (mActionsList.getKeysSequenceUp().equals(keysSequence)) {
                    index = mListIndexer.up();
                } else {
                    return;
                }

                mActionsListView.requestFocusFromTouch();
                mActionsListView.setSelection(index);

                mActionExecutionTimer.cancel();
                mActionExecutionTimer.start();
            }
            catch(IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void handleSingleKey(int keyCode) {

    }

    CountDownTimer mActionExecutionTimer = new CountDownTimer(5000, 5000) {
        @Override
        public void onTick(long l) {
        }

        @Override
        public void onFinish() {
            String selectedActionName = (String)mActionsListView.getSelectedItem();

            if(selectedActionName != null) {
                Action action = mServiceBinder.getActionsStorage().getItem(selectedActionName);

                if(action != null) {
                    action.evaluate(SelectActionActivity.this);
                }
            }

            finish();
        }
    };

    private ListView mActionsListView;
    private ActionsList mActionsList;
    private String mActionsListName;
    private ArrayAdapter<String> mActionsNamesArrayAdapter;
    private ListIndexer mListIndexer;

    public static final String ACTIONS_LIST_NAME_PARAMETER = "actionsListName";
}
