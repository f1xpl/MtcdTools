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

import com.f1x.mtcdtools.named.objects.ActionsList;
import com.f1x.mtcdtools.ListIndexer;
import com.f1x.mtcdtools.R;
import com.f1x.mtcdtools.configuration.Configuration;
import com.f1x.mtcdtools.configuration.ConfigurationChangeListener;
import com.f1x.mtcdtools.input.KeysSequenceListener;
import com.f1x.mtcdtools.named.objects.NamedObjectDispatcher;
import com.f1x.mtcdtools.named.objects.NamedObject;

import java.util.List;

public class SelectNamedObjectActivity extends ServiceActivity implements KeysSequenceListener, ConfigurationChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_named_object);

        mExecuteActionProgressBar = (ProgressBar)this.findViewById(R.id.progressBarExecuteAction);
        mActionsListName = this.getIntent().getStringExtra(ACTIONS_LIST_NAME_PARAMETER);
        mActionsNamesArrayAdapter = new ArrayAdapter<>(this, R.layout.layout_action_name_row);

        mActionsListView = (ListView)this.findViewById(R.id.listViewAddedNamedObjects);
        mActionsListView.setAdapter(mActionsNamesArrayAdapter);
        mActionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String namedObjectName = mActionsNamesArrayAdapter.getItem(position);
                mDispatcher.dispatch(namedObjectName, SelectNamedObjectActivity.this);
                SelectNamedObjectActivity.this.finish();
            }
        });

        mListIndexer = new ListIndexer();
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
        mExecuteActionProgressBar.setMax(mServiceBinder.getConfiguration().getActionExecutionDelay());
        mActionsList = mActionsListName == null ? null : (ActionsList)mServiceBinder.getNamedObjectsStorage().getItem(mActionsListName);
        mServiceBinder.getPressedKeysSequenceManager().pushListener(this);
        mDispatcher = new NamedObjectDispatcher(mServiceBinder.getNamedObjectsStorage());

        if(mActionsList != null) {
            fillControls();
            mActionExecutionTimer = createActionExecutionTimer(mServiceBinder.getConfiguration().getActionExecutionDelay());
            restartTimer();
        } else {
            Toast.makeText(this, this.getText(R.string.UnknownObjectType), Toast.LENGTH_LONG).show();
            finish();
        }
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

                restartTimer();
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
                            SelectNamedObjectActivity.this.finish();
                        }
                    }
                });
            }
        };
    }

    private void restartTimer() {
        mExecuteActionProgressBar.setProgress(0);
        mActionExecutionTimer.cancel();
        mActionExecutionTimer.start();
    }

    private void fillControls() {
        mActionsListView.clearChoices();
        mActionsListView.requestLayout();

        mExecuteActionProgressBar.setProgress(0);
        mExecuteActionProgressBar.setMax(mServiceBinder.getConfiguration().getActionExecutionDelay());

        mActionsNamesArrayAdapter.clear();
        mActionsNamesArrayAdapter.addAll(mActionsList.getActionNames());
        mActionsNamesArrayAdapter.insert(this.getText(R.string.Cancel).toString(), 0);
        mListIndexer.reset(mActionsNamesArrayAdapter.getCount());

        mActionsListView.post(new Runnable() {
            @Override
            public void run() {
                mActionsListView.setItemChecked(0, true);
                mActionsListView.setSelection(0);
                mExecuteActionProgressBar.setLayoutParams(new RelativeLayout.LayoutParams(mActionsListView.getWidth(), mActionsListView.getHeight()));
            }
        });
    }

    private CountDownTimer mActionExecutionTimer;

    private ListView mActionsListView;
    private ActionsList mActionsList;
    private String mActionsListName;
    private ArrayAdapter<String> mActionsNamesArrayAdapter;
    private ListIndexer mListIndexer;
    private ProgressBar mExecuteActionProgressBar;
    private NamedObjectDispatcher mDispatcher;

    private static final int PROGRESS_BAR_DELTA = 50;
    public static final String ACTIONS_LIST_NAME_PARAMETER = "actionsListName";
}
