package com.f1x.mtcdtools.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.f1x.mtcdtools.MessageHandlerInterface;
import com.f1x.mtcdtools.Messaging;
import com.f1x.mtcdtools.MtcdService;
import com.f1x.mtcdtools.R;
import com.f1x.mtcdtools.StaticMessageHandler;
import com.f1x.mtcdtools.adapters.KeyInputArrayAdapter;
import com.f1x.mtcdtools.input.KeyInput;

import java.util.Map;

public class RemoveBindingsActivity extends AppCompatActivity implements MessageHandlerInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_bindings);

        ListView bindingsListView = (ListView)findViewById(R.id.listViewBindings);
        mKeyInputArrayAdapter = new KeyInputArrayAdapter(this);
        bindingsListView.setAdapter(mKeyInputArrayAdapter);

        bindingsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                KeyInput keyInput = mKeyInputArrayAdapter.getItem(position);
                if(keyInput != null) {
                    sendKeyInputRemoveRequest(keyInput);
                }

                return true;
            }
        });

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
            case Messaging.MessageIds.GET_KEY_INPUTS_RESPONSE:
                mKeyInputArrayAdapter.reset((Map<Integer, KeyInput>)message.obj);
                break;
            case Messaging.MessageIds.EDIT_KEY_INPUTS_RESPONSE:
                if(message.arg1 == Messaging.KeyInputsEditResult.FAILURE) {
                    Toast.makeText(this, getString(R.string.KeyBindingRemovalFailure), Toast.LENGTH_LONG).show();
                } else {
                    sendGetKeyInputsRequest();
                }
                break;
        }
    }

    private void sendGetKeyInputsRequest() {
        Message message = new Message();
        message.what = Messaging.MessageIds.GET_KEY_INPUTS_REQUEST;
        message.replyTo = mMessenger;

        try {
            mServiceMessenger.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void sendKeyInputRemoveRequest(KeyInput keyInput) {
        Message message = new Message();
        message.what = Messaging.MessageIds.EDIT_KEY_INPUTS_REQUEST;
        message.arg1 = Messaging.KeyInputsEditType.REMOVE;
        message.obj = keyInput;
        message.replyTo = mMessenger;

        try {
            mServiceMessenger.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mServiceMessenger = new Messenger(service);
            sendGetKeyInputsRequest();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    private static final StaticMessageHandler mMessageHandler = new StaticMessageHandler();
    private final Messenger mMessenger = new Messenger(mMessageHandler);
    private Messenger mServiceMessenger;
    private KeyInputArrayAdapter mKeyInputArrayAdapter;
}
