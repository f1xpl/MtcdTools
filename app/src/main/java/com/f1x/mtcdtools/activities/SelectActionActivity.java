package com.f1x.mtcdtools.activities;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.f1x.mtcdtools.ActionsList;
import com.f1x.mtcdtools.ListIndexer;
import com.f1x.mtcdtools.R;
import com.f1x.mtcdtools.actions.Action;
import com.f1x.mtcdtools.configuration.Configuration;
import com.f1x.mtcdtools.configuration.ConfigurationChangeListener;
import com.f1x.mtcdtools.input.KeysSequenceListener;

import java.util.List;

public class SelectActionActivity extends ServiceActivity implements KeysSequenceListener, ConfigurationChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_action);

        mExecuteActionProgressBar = (ProgressBar)this.findViewById(R.id.progressBarExecuteAction);

        mActionsListName = this.getIntent().getStringExtra(ACTIONS_LIST_NAME_PARAMETER);
        mActionsNamesArrayAdapter = new ArrayAdapter<>(this, R.layout.layout_action_name_row);

        mActionsListView = (ListView)this.findViewById(R.id.listViewActions);
        mActionsListView.setAdapter(mActionsNamesArrayAdapter);
        mActionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String actionName = mActionsNamesArrayAdapter.getItem(position);
                Action action = mServiceBinder.getActionsStorage().getItem(actionName);

                if(action != null) {
                    action.evaluate(SelectActionActivity.this);
                }

                SelectActionActivity.this.finish();
            }
        });

        mListIndexer = new ListIndexer();
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
            mServiceBinder.getConfiguration().removeChangeListener(this);
        }

        if(mActionExecutionTimer != null) {
            mActionExecutionTimer.cancel();
        }
    }

    @Override
    protected void onServiceConnected() {
        mServiceBinder.getConfiguration().addChangeListener(this);
        mActionExecutionTimer = createActionExecutionTimer(mServiceBinder.getConfiguration().getActionExecutionDelay());
        mActionExecutionTimer.start();
        mExecuteActionProgressBar.setMax(mServiceBinder.getConfiguration().getActionExecutionDelay());

        if(mActionsListName != null) {
            mActionsList = mServiceBinder.getActionsListsStorage().getItem(mActionsListName);

            if(mActionsList != null) {
                mServiceBinder.getPressedKeysSequenceManager().pushListener(this);
                mActionsNamesArrayAdapter.clear();
                mActionsNamesArrayAdapter.addAll(mActionsList.getActionNames());
                mActionsNamesArrayAdapter.insert(this.getText(R.string.Cancel).toString(), 0);
                mListIndexer.reset(mActionsNamesArrayAdapter.getCount());
                mActionsListView.setItemChecked(0, true);
                mActionsListView.setSelection(0);
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
                mActionsListView.setSelection(index);

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

    @Override
    public void onParameterChanged(String parameterName, Configuration configuration) {
        if(parameterName.equals(Configuration.ACTION_EXECUTION_DELAY_PROPERTY_NAME)) {
            mActionExecutionTimer = createActionExecutionTimer(mServiceBinder.getConfiguration().getActionExecutionDelay());
            mActionExecutionTimer.start();
            mExecuteActionProgressBar.setMax(mServiceBinder.getConfiguration().getActionExecutionDelay());
        }
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
                mExecuteActionProgressBar.post(new Runnable() {
                    @Override
                    public void run() {
                        int checkedActionPosition = mActionsListView.getCheckedItemPosition();
                        if(checkedActionPosition != ListView.INVALID_POSITION) {
                            mActionsListView.performItemClick(mActionsListView.getAdapter().getView(checkedActionPosition, null, null), checkedActionPosition, checkedActionPosition);
                        } else {
                            SelectActionActivity.this.finish();
                        }
                    }
                });
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

    private static final int PROGRESS_BAR_DELTA = 50;

    public static final String ACTIONS_LIST_NAME_PARAMETER = "actionsListName";
}
