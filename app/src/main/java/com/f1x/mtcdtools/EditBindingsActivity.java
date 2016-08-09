package com.f1x.mtcdtools;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.f1x.mtcdtools.keys.input.KeyInput;

import java.util.Map;

public class EditBindingsActivity extends AppCompatActivity implements MessageHandlerInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bindings);

        ListView bindingsListView = (ListView)findViewById(R.id.listViewBindings);
        mKeyInputArrayAdapter = new KeyInputArrayAdapter(this);
        bindingsListView.setAdapter(mKeyInputArrayAdapter);

        bindingsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                KeyInput keyInput = mKeyInputArrayAdapter.getItem(position);
                if(keyInput != null) {
                    sendMessage(Messaging.MessageIds.REMOVE_KEY_INPUT_REQUEST, keyInput);
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
            case Messaging.MessageIds.KEY_INPUTS_RESPONSE:
                mKeyInputArrayAdapter.reset((Map<Integer, KeyInput>)message.obj);
                break;
            case Messaging.MessageIds.KEY_INPUTS_CHANGED:
                sendMessage(Messaging.MessageIds.KEY_INPUTS_REQUEST);
                break;
            case Messaging.MessageIds.REMOVE_KEY_INPUT_RESPONSE:
                if(message.arg1 == Messaging.KeyInputRemovalResult.FAILURE) {
                    Toast.makeText(this, getString(R.string.KeyBindingRemovalFailure), Toast.LENGTH_LONG).show();
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

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mServiceMessenger = new Messenger(service);
            sendMessage(Messaging.MessageIds.KEY_INPUTS_REQUEST);
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
