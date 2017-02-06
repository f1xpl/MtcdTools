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

import com.f1x.mtcdtools.ListViewScroller;
import com.f1x.mtcdtools.R;
import com.f1x.mtcdtools.configuration.Configuration;
import com.f1x.mtcdtools.configuration.ConfigurationChangeListener;
import com.f1x.mtcdtools.input.KeysSequenceListener;
import com.f1x.mtcdtools.named.objects.ActionsList;
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
                mServiceBinder.getNamedObjectsDispatcher().dispatch(namedObjectName, SelectNamedObjectActivity.this);
                SelectNamedObjectActivity.this.finish();
            }
        });

        mListViewScroller = new ListViewScroller(mActionsListView);
    }

    @Override
    protected void onResume() {
        mListViewScroller.reset();
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
        NamedObject namedObject = mActionsListName == null ? null : mServiceBinder.getNamedObjectsStorage().getItem(mActionsListName);

        if(namedObject == null || !namedObject.getObjectType().equals(ActionsList.OBJECT_TYPE)) {
            Toast.makeText(this, this.getText(R.string.UnknownObjectType), Toast.LENGTH_LONG).show();
            finish();

            return;
        }

        mActionsList = (ActionsList)namedObject;
        mServiceBinder.getConfiguration().addChangeListener(this);
        mExecuteActionProgressBar.setMax(mServiceBinder.getConfiguration().getVoiceCommandExecutionDelay());
        mServiceBinder.getPressedKeysSequenceManager().pushListener(this);

        fillControls();
        mActionExecutionTimer = createActionExecutionTimer(mServiceBinder.getConfiguration().getVoiceCommandExecutionDelay());
        restartTimer();
    }

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

    @Override
    public void onParameterChanged(String parameterName, Configuration configuration) {
        if(parameterName.equals(Configuration.VOICE_COMMAND_EXECUTION_DELAY_PROPERTY_NAME)) {
            mActionExecutionTimer = createActionExecutionTimer(mServiceBinder.getConfiguration().getVoiceCommandExecutionDelay());
            mActionExecutionTimer.start();
            mExecuteActionProgressBar.setMax(mServiceBinder.getConfiguration().getVoiceCommandExecutionDelay());
        }
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
        mExecuteActionProgressBar.setProgress(0);
        mExecuteActionProgressBar.setMax(mServiceBinder.getConfiguration().getVoiceCommandExecutionDelay());

        mActionsNamesArrayAdapter.clear();
        mActionsNamesArrayAdapter.addAll(mActionsList.getActionNames());
        mActionsNamesArrayAdapter.insert(this.getText(R.string.Cancel).toString(), 0);
        mListViewScroller.reset();

        mActionsListView.post(new Runnable() {
            @Override
            public void run() {
                mExecuteActionProgressBar.setLayoutParams(new RelativeLayout.LayoutParams(mActionsListView.getWidth(), mActionsListView.getHeight()));
            }
        });
    }

    private CountDownTimer mActionExecutionTimer;

    private ListView mActionsListView;
    private ActionsList mActionsList;
    private String mActionsListName;
    private ArrayAdapter<String> mActionsNamesArrayAdapter;
    private ListViewScroller mListViewScroller;
    private ProgressBar mExecuteActionProgressBar;

    private static final int PROGRESS_BAR_DELTA = 50;
    public static final String ACTIONS_LIST_NAME_PARAMETER = "actionsListName";
}
