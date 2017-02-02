package com.f1x.mtcdtools.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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

        mSharedPreferences = getSharedPreferences(MainActivity.APP_NAME, Context.MODE_PRIVATE);
        int actionExecutionDelay = mSharedPreferences.getInt(SettingsActivity.ACTION_EXECUTION_DELAY_PROPERTY_NAME, SettingsActivity.ACTION_EXECUTION_DELAY_DEFAULT_VALUE_MS);
        mActionExecutionTimer = createActionExecutionTimer(actionExecutionDelay);

        mSharedPreferences.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String prefixName) {
                if(prefixName.equals(SettingsActivity.ACTION_EXECUTION_DELAY_PROPERTY_NAME)) {
                    int actionExecutionDelay = sharedPreferences.getInt(SettingsActivity.ACTION_EXECUTION_DELAY_PROPERTY_NAME, SettingsActivity.ACTION_EXECUTION_DELAY_DEFAULT_VALUE_MS);
                    mActionExecutionTimer = createActionExecutionTimer(actionExecutionDelay);
                    mExecuteActionProgressBar.setMax(actionExecutionDelay);
                }
            }
        });

        mActionsListName = this.getIntent().getStringExtra(ACTIONS_LIST_NAME_PARAMETER);
        mActionsNamesArrayAdapter = new ArrayAdapter<>(this, R.layout.layout_action_name_row);

        mActionsListView = (ListView)this.findViewById(R.id.listViewActions);
        mActionsListView.setAdapter(mActionsNamesArrayAdapter);

        mListIndexer = new ListIndexer();
        mActionExecutionTimer.start();

        mExecuteActionProgressBar = (ProgressBar)this.findViewById(R.id.progressBarExecuteAction);
        mExecuteActionProgressBar.setMax(actionExecutionDelay);
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
                mActionsListView.setItemChecked(0, true);
                mActionsListView.post(new Runnable() {
                    @Override
                    public void run() {
                        mExecuteActionProgressBar.setLayoutParams(new RelativeLayout.LayoutParams(mActionsListView.getWidth(), mActionsListView.getHeight()));
                    }
                });

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

                mActionsListView.setItemChecked(index, true);

                mActionExecutionTimer.cancel();
                mActionExecutionTimer.start();
                mExecuteActionProgressBar.setProgress(0);
            }
            catch(IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void handleSingleKey(int keyCode) {

    }

    CountDownTimer createActionExecutionTimer(int delayMs) {
        return new CountDownTimer(delayMs, PROGRESS_BAR_DELTA) {
            @Override
            public void onTick(long l) {
                mExecuteActionProgressBar.incrementProgressBy(PROGRESS_BAR_DELTA);
            }

            @Override
            public void onFinish() {
                mExecuteActionProgressBar.setProgress(mExecuteActionProgressBar.getMax());

                int checkedActionPosition = mActionsListView.getCheckedItemPosition();
                String checkedActionName = checkedActionPosition != ListView.INVALID_POSITION ? (String)mActionsListView.getItemAtPosition(checkedActionPosition) : null;

                if(checkedActionName != null) {
                    Action action = mServiceBinder.getActionsStorage().getItem(checkedActionName);

                    if(action != null) {
                        action.evaluate(SelectActionActivity.this);
                    }
                }

                SelectActionActivity.this.finish();
            }
        };
    }

    private CountDownTimer mActionExecutionTimer;

    private ListView mActionsListView;
    private ActionsList mActionsList;
    private String mActionsListName;
    private ArrayAdapter<String> mActionsNamesArrayAdapter;
    private ListIndexer mListIndexer;
    private ProgressBar mExecuteActionProgressBar;
    private SharedPreferences mSharedPreferences;

    private static final int PROGRESS_BAR_DELTA = 50;

    public static final String ACTIONS_LIST_NAME_PARAMETER = "actionsListName";
}
