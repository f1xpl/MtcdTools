package com.f1x.mtcdtools.activities;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.f1x.mtcdtools.MessageHandlerInterface;
import com.f1x.mtcdtools.Messaging;
import com.f1x.mtcdtools.MtcdService;
import com.f1x.mtcdtools.R;
import com.f1x.mtcdtools.StaticMessageHandler;
import com.f1x.mtcdtools.adapters.ApplicationEntry;
import com.f1x.mtcdtools.adapters.ApplicationEntryArrayAdapter;
import com.f1x.mtcdtools.adapters.KeyInputTypeArrayAdapter;
import com.f1x.mtcdtools.input.KeyInput;
import com.f1x.mtcdtools.input.KeyInputType;
import com.f1x.mtcdtools.input.KeyPressReceiver;

public class NewBindingActivity extends AppCompatActivity implements MessageHandlerInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_binding);

        ApplicationEntryArrayAdapter applicationEntryArrayAdapter = new ApplicationEntryArrayAdapter(this);
        final Spinner applicationListSpinner = (Spinner)findViewById(R.id.spinnerApplication);
        applicationListSpinner.setAdapter(applicationEntryArrayAdapter);
        applicationListSpinner.setVisibility(View.INVISIBLE);
        //-------------------------------------------------------------------------------------

        mKeyInputTypeArrayAdapter = new KeyInputTypeArrayAdapter(this);
        final Spinner keyInputTypeSpinner = (Spinner)findViewById(R.id.spinnerKeyInputType);
        keyInputTypeSpinner.setAdapter(mKeyInputTypeArrayAdapter);
        keyInputTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(KeyInputType.fromString(mKeyInputTypeArrayAdapter.getItem(position)) == KeyInputType.LAUNCH) {
                    applicationListSpinner.setVisibility(View.VISIBLE);
                } else {
                    applicationListSpinner.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                applicationListSpinner.setVisibility(View.INVISIBLE);
            }
        });
        //-------------------------------------------------------------------------------------

        mKeyCodeTextView = (TextView)findViewById(R.id.textViewKeyCode);
        //-------------------------------------------------------------------------------------

        Button obtainKeyInputButton = (Button)findViewById(R.id.buttonObtainKeyInput);
        obtainKeyInputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage(Messaging.MessageIds.SUSPEND_KEY_INPUT_DISPATCHING);
                registerReceiver(mKeyPressReceiver, mKeyPressReceiver.getIntentFilter());

                mKeyObtainProgressDialog.show();
                mKeyObtainTimer.start();
            }
        });
        //-------------------------------------------------------------------------------------

        Button saveButton = (Button)findViewById(R.id.buttonSaveNew);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KeyInputType keyInputType = KeyInputType.fromString((String)keyInputTypeSpinner.getSelectedItem());
                String packageName = "";

                if(keyInputType == KeyInputType.LAUNCH) {
                    ApplicationEntry applicationEntry = (ApplicationEntry)applicationListSpinner.getSelectedItem();
                    packageName = applicationEntry.getPackageName();
                }

                String keyCodeText = mKeyCodeTextView.getText().toString();
                if(keyCodeText.equals(getString(R.string.DefaultKeyCodeLabel))) {
                    Toast.makeText(NewBindingActivity.this, getString(R.string.NotObtainedKeyCode), Toast.LENGTH_LONG).show();
                } else if(keyInputType == KeyInputType.NONE) {
                    Toast.makeText(NewBindingActivity.this, getString(R.string.NotSelectedInputType), Toast.LENGTH_LONG).show();
                } else if(keyInputType == KeyInputType.LAUNCH && packageName.isEmpty()) {
                    Toast.makeText(NewBindingActivity.this, getString(R.string.NotSelectedApplication), Toast.LENGTH_LONG).show();
                } else {
                    KeyInput keyInput = new KeyInput(Integer.parseInt(mKeyCodeTextView.getText().toString()), keyInputType, packageName);
                    sendMessage(Messaging.MessageIds.ADD_KEY_INPUT_REQUEST, keyInput);
                }
            }
        });
        //-------------------------------------------------------------------------------------

        Button cancelButton = (Button)findViewById(R.id.buttonCancelNew);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //-------------------------------------------------------------------------------------

        mKeyObtainProgressDialog = new ProgressDialog(this);
        mKeyObtainProgressDialog.setTitle(getString(R.string.ObtainKeyInputProgressBarTitle));
        mKeyObtainProgressDialog.setMessage(getString(R.string.ObtainKeyInputProgressBarText));
        mKeyObtainProgressDialog.setCancelable(false);
        //-------------------------------------------------------------------------------------

        mMessageHandler.setTarget(this);
        this.bindService(new Intent(this, MtcdService.class), mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unbindService(mServiceConnection);
    }

    @Override
    public void handleMessage(Message message) {
        switch(message.what) {
            case Messaging.MessageIds.ADD_KEY_INPUT_RESPONSE:
                if(message.arg1 == Messaging.KeyInputAdditionResult.SUCCEED) {
                    finish();
                } else if(message.arg1 == Messaging.KeyInputAdditionResult.FAILURE) {
                    Toast.makeText(this, getString(R.string.KeyBindingAdditionFailure), Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void sendMessage(int messageId) {
        sendMessage(messageId, null);
    }

    private void sendMessage(int messageId, Object object) {
        try {
            Message message = new Message();
            message.what = messageId;
            message.obj = object;
            message.replyTo = mMessenger;

            mServiceMessenger.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private final KeyPressReceiver mKeyPressReceiver = new KeyPressReceiver() {
        @Override
        public void handleKeyInput(int keyCode) {
            mKeyObtainTimer.cancel();
            mKeyObtainProgressDialog.dismiss();
            mKeyCodeTextView.setText(Integer.toString(keyCode));
            sendMessage(Messaging.MessageIds.RESUME_KEY_INPUT_DISPATCHING);

            unregisterReceiver(mKeyPressReceiver);
        }
    };

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mServiceMessenger = new Messenger(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };


    private final CountDownTimer mKeyObtainTimer = new CountDownTimer(KEY_OBTAIN_TIMER_DURATION_MS, KEY_OBTAIN_TIMER_STEP_MS) {
        @Override
        public void onTick(long l) {}

        @Override
        public void onFinish() {
            mKeyObtainProgressDialog.dismiss();
            unregisterReceiver(mKeyPressReceiver);
            sendMessage(Messaging.MessageIds.RESUME_KEY_INPUT_DISPATCHING);
        }
    };

    private TextView mKeyCodeTextView;
    private ProgressDialog mKeyObtainProgressDialog;
    private static final StaticMessageHandler mMessageHandler = new StaticMessageHandler();
    private final Messenger mMessenger = new Messenger(mMessageHandler);
    private Messenger mServiceMessenger;

    private KeyInputTypeArrayAdapter mKeyInputTypeArrayAdapter;

    private static final long KEY_OBTAIN_TIMER_DURATION_MS = 5000;
    private static final long KEY_OBTAIN_TIMER_STEP_MS = 1000;
}
