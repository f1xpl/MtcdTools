package com.microntek.f1x.mtcdtools.service.dispatching.activities;

import com.microntek.f1x.mtcdtools.utils.ListViewScroller;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.microntek.f1x.mtcdtools.R;
import com.microntek.f1x.mtcdtools.service.ServiceActivity;
import com.microntek.f1x.mtcdtools.adapters.NamedObjectIdsArrayAdapter;
import com.microntek.f1x.mtcdtools.service.configuration.Configuration;
import com.microntek.f1x.mtcdtools.service.configuration.ConfigurationChangeListener;
import com.microntek.f1x.mtcdtools.service.input.KeysSequenceListener;
import com.microntek.f1x.mtcdtools.named.objects.containers.ActionsList;
import com.microntek.f1x.mtcdtools.named.NamedObject;
import com.microntek.f1x.mtcdtools.named.NamedObjectId;

import java.util.List;

public class ActionsListDispatchActivity extends ServiceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_named_object);

        mExecuteActionProgressBar = (ProgressBar)this.findViewById(R.id.progressBarExecuteAction);
        mActionsListId = this.getIntent().getParcelableExtra(ACTIONS_LIST_ID_PARAMETER);
        mActionIdsArrayAdapter = new NamedObjectIdsArrayAdapter(this, R.layout.layout_action_name_row);

        mActionsListView = (ListView)this.findViewById(R.id.listViewAddedNamedObjects);
        mActionsListView.setAdapter(mActionIdsArrayAdapter);
        mActionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                NamedObjectId namedObjectId = mActionIdsArrayAdapter.getItem(position);
                mServiceBinder.getNamedObjectsDispatcher().dispatch(namedObjectId, ActionsListDispatchActivity.this);
                ActionsListDispatchActivity.this.finish();
            }
        });

        mListViewScroller = new ListViewScroller(mActionsListView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mListViewScroller.reset();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(mServiceBinder != null) {
            mServiceBinder.getPressedKeysSequenceManager().popListener(mKeysSequenceListener);
            mServiceBinder.getConfiguration().removeChangeListener(mConfigurationChangeListener);
        }

        if(mActionExecutionTimer != null) {
            mActionExecutionTimer.cancel();
        }
    }

    @Override
    protected void onServiceConnected() {
        NamedObject namedObject = mActionsListId == null ? null : mServiceBinder.getNamedObjectsStorage().getItem(mActionsListId);

        if(namedObject == null || !namedObject.getObjectType().equals(ActionsList.OBJECT_TYPE)) {
            Toast.makeText(this, this.getText(R.string.UnknownObjectType), Toast.LENGTH_LONG).show();
            finish();

            return;
        }

        mActionsList = (ActionsList)namedObject;
        mServiceBinder.getConfiguration().addChangeListener(mConfigurationChangeListener);
        mExecuteActionProgressBar.setMax(mServiceBinder.getConfiguration().getActionExecutionDelay());
        mServiceBinder.getPressedKeysSequenceManager().pushListener(mKeysSequenceListener);

        fillControls();
        mActionExecutionTimer = createActionExecutionTimer(mServiceBinder.getConfiguration().getActionExecutionDelay());
        restartTimer();
    }

    private CountDownTimer createActionExecutionTimer(int delayMs) {
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
                            ActionsListDispatchActivity.this.finish();
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
        mExecuteActionProgressBar.setProgress(0);
        mExecuteActionProgressBar.setMax(mServiceBinder.getConfiguration().getActionExecutionDelay());

        mActionIdsArrayAdapter.reset(mActionsList.getActionIds());
        mActionIdsArrayAdapter.insert(new NamedObjectId(this.getText(R.string.Cancel).toString()), 0);

        mActionsListView.post(new Runnable() {
            @Override
            public void run() {
                mListViewScroller.reset();
                mExecuteActionProgressBar.setLayoutParams(new RelativeLayout.LayoutParams(mActionsListView.getWidth(), mActionsListView.getHeight()));
            }
        });
    }

    private final ConfigurationChangeListener mConfigurationChangeListener = new ConfigurationChangeListener() {
        @Override
        public void onParameterChanged(String parameterName, Configuration configuration) {
            if(parameterName.equals(Configuration.VOICE_COMMAND_EXECUTION_DELAY_PROPERTY_NAME)) {
                mActionExecutionTimer = createActionExecutionTimer(mServiceBinder.getConfiguration().getActionExecutionDelay());
                mActionExecutionTimer.start();
                mExecuteActionProgressBar.setMax(mServiceBinder.getConfiguration().getActionExecutionDelay());
            }
        }
    };

    private final KeysSequenceListener mKeysSequenceListener = new KeysSequenceListener() {
        @Override
        public void handleKeysSequence(List<Integer> keysSequence) {
            if(mActionsList.getKeysSequenceDown().size() > 1 && mActionsList.getKeysSequenceDown().equals(keysSequence)) {
                mListViewScroller.scrollDown();
                restartTimer();
            } else if(mActionsList.getKeysSequenceUp().size() > 1 && mActionsList.getKeysSequenceUp().equals(keysSequence)) {
                mListViewScroller.scrollUp();
                restartTimer();
            }
        }

        @Override
        public void handleSingleKey(int keyCode) {
            if(mActionsList.getKeysSequenceDown().size() == 1 && mActionsList.getKeysSequenceDown().contains(keyCode)) {
                mListViewScroller.scrollDown();
                restartTimer();
            } else if(mActionsList.getKeysSequenceUp().size() == 1 && mActionsList.getKeysSequenceUp().contains(keyCode)) {
                mListViewScroller.scrollUp();
                restartTimer();
            }
        }
    };

    private CountDownTimer mActionExecutionTimer;

    private ListView mActionsListView;
    private ActionsList mActionsList;
    private NamedObjectId mActionsListId;
    private NamedObjectIdsArrayAdapter mActionIdsArrayAdapter;
    private ListViewScroller mListViewScroller;
    private ProgressBar mExecuteActionProgressBar;

    private static final int PROGRESS_BAR_DELTA = 50;
    public static final String ACTIONS_LIST_ID_PARAMETER = "actionsListId";
}
