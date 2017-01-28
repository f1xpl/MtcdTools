package com.f1x.mtcdtools.activities;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.f1x.mtcdtools.ActionsList;
import com.f1x.mtcdtools.ListIndexer;
import com.f1x.mtcdtools.R;
import com.f1x.mtcdtools.actions.Action;
import com.f1x.mtcdtools.adapters.ActionsNamesArrayAdapter;
import com.f1x.mtcdtools.input.KeysSequenceListener;

import java.util.List;

public class SelectActionActivity extends ServiceActivity implements KeysSequenceListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_action);

        mActionsListName = this.getIntent().getStringExtra(ACTIONS_LIST_NAME_PARAMETER);
        mActionsNamesArrayAdapter = new ActionsNamesArrayAdapter(this);

        mActionsListView = (ListView)this.findViewById(R.id.listViewActions);
        mActionsListView.setAdapter(mActionsNamesArrayAdapter);

        mActionExecutionProgressBar = (ProgressBar)this.findViewById(R.id.progressBarActionExecution);
        mActionExecutionProgressBar.setMax(5);

        mListIndexer = new ListIndexer();
    }

    @Override
    protected void onResume() {
        if(mServiceBinder != null) {
            mServiceBinder.getPressedKeysSequenceManager().pushListener(this);
        }
    }

    @Override
    protected void onPause() {
        if(mServiceBinder != null) {
            mServiceBinder.getPressedKeysSequenceManager().popListener(this);
        }
    }

    @Override
    protected void onServiceConnected() {
        if(mActionsListName != null) {
            mActionsList = mServiceBinder.getActionsListStorage().getActionsList(mActionsListName);

            if(mActionsList != null) {
                mServiceBinder.getPressedKeysSequenceManager().pushListener(this);
                mActionsNamesArrayAdapter.reset(mActionsList.getActionNames());
                mActionsNamesArrayAdapter.insert(this.getText(R.string.Cancel).toString(), 0);
                mListIndexer.reset(mActionsNamesArrayAdapter.getCount());

                mActionsListView.requestFocusFromTouch();
                mActionsListView.setSelection(0);

                return;
            }
        }

        Toast.makeText(this, this.getText(R.string.ActionsListNotFound), Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void handleKeysSequence(List<Integer> keysSequence) {
        if(mActionsList != null) {
            try {
                int index;

                if (mActionsList.getKeysSequenceDown() == keysSequence) {
                    index = mListIndexer.down();
                } else if (mActionsList.getKeysSequenceUp() == keysSequence) {
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

    CountDownTimer mActionExecutionTimer = new CountDownTimer(5000, 1000) {
        @Override
        public void onTick(long l) {
            mActionExecutionProgressBar.incrementProgressBy((int)(l / 1000));
        }

        @Override
        public void onFinish() {
            String selectedActionName = (String)mActionsListView.getSelectedItem();

            if(selectedActionName != null) {
                Action action = mServiceBinder.getActionsStorage().getAction(selectedActionName);

                if(action != null) {
                    action.evaluate(SelectActionActivity.this);
                }
            }
        }
    };

    private ListView mActionsListView;
    private ProgressBar mActionExecutionProgressBar;

    private ActionsList mActionsList;
    private String mActionsListName;
    private ActionsNamesArrayAdapter mActionsNamesArrayAdapter;
    private ListIndexer mListIndexer;

    public static final String ACTIONS_LIST_NAME_PARAMETER = "actionsListName";
}
