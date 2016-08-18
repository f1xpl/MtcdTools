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

import com.f1x.mtcdtools.MessageHandlerInterface;
import com.f1x.mtcdtools.services.MtcdService;
import com.f1x.mtcdtools.StaticMessageHandler;

/**
 * Created by COMPUTER on 2016-08-18.
 */
public abstract class ServiceActivity extends AppCompatActivity implements MessageHandlerInterface {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMessageHandler.setTarget(this);
        this.bindService(new Intent(this, MtcdService.class), mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unbindService(mServiceConnection);
    }

    protected abstract void onServiceConnected();

    protected void sendMessage(int messageId) {
        Message message = new Message();
        message.what = messageId;
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
            ServiceActivity.this.onServiceConnected();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    protected static final StaticMessageHandler mMessageHandler = new StaticMessageHandler();
    protected final Messenger mMessenger = new Messenger(mMessageHandler);
    protected Messenger mServiceMessenger;
}
