package com.f1x.mtcdtools.service;

import android.app.Notification;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.f1x.mtcdtools.R;
import com.f1x.mtcdtools.input.PressedKeysSequenceManager;
import com.f1x.mtcdtools.storage.ActionsListStorage;
import com.f1x.mtcdtools.storage.ActionsStorage;
import com.f1x.mtcdtools.storage.FileReader;
import com.f1x.mtcdtools.storage.FileWriter;
import com.f1x.mtcdtools.storage.KeysSequenceBindingsStorage;
import com.f1x.mtcdtools.storage.exceptions.DuplicatedEntryException;
import com.f1x.mtcdtools.storage.exceptions.ActionCreationFailed;

import org.json.JSONException;

import java.io.IOException;

/**
 * Created by COMPUTER on 2016-08-03.
 */
public class MtcdService extends android.app.Service {
    @Override
    public void onCreate() {
        super.onCreate();

        mForceRestart = true;
        mServiceInitialized = false;

        FileReader fileReader = new FileReader(this);
        FileWriter fileWriter = new FileWriter(this);
        mActionsStorage = new ActionsStorage(fileReader, fileWriter, this);
        mActionsListStorage = new ActionsListStorage(fileReader, fileWriter);
        mKeysSequenceBindingsStorage = new KeysSequenceBindingsStorage(fileReader, fileWriter);
        mPressedKeysSequenceManager = new PressedKeysSequenceManager();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(mForceRestart) {
            MtcdServiceWatchdog.scheduleServiceRestart(this);
        }

        if(mServiceInitialized) {
            unregisterReceiver(mPressedKeysSequenceManager);
        }

        mServiceInitialized = false;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mServiceBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        if(!mServiceInitialized) {
            try {
                mActionsStorage.read();
                mActionsListStorage.read();
                mKeysSequenceBindingsStorage.read();
                registerReceiver(mPressedKeysSequenceManager, mPressedKeysSequenceManager.getIntentFilter());

                mServiceInitialized = true;
                startForeground(1555, createNotification());
            } catch (JSONException | IOException | DuplicatedEntryException | ActionCreationFailed e) {
                e.printStackTrace();
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        return START_STICKY;
    }

    private Notification createNotification() {
        return new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.MtcdServiceDescription))
                .setSmallIcon(R.drawable.notificationicon)
                .setOngoing(true)
                .build();
    }

    private boolean mForceRestart;
    private boolean mServiceInitialized;
    private ActionsStorage mActionsStorage;
    private ActionsListStorage mActionsListStorage;
    private KeysSequenceBindingsStorage mKeysSequenceBindingsStorage;
    private PressedKeysSequenceManager mPressedKeysSequenceManager;

    private final ServiceBinder mServiceBinder = new ServiceBinder() {
        @Override
        public KeysSequenceBindingsStorage getKeysSequenceBindingsStorage() {
            return mKeysSequenceBindingsStorage;
        }

        @Override
        public ActionsStorage getActionsStorage() {
            return mActionsStorage;
        }

        @Override
        public ActionsListStorage getActionsListStorage() {
            return mActionsListStorage;
        }

        @Override
        public PressedKeysSequenceManager getPressedKeysSequenceManager() {
            return mPressedKeysSequenceManager;
        }
    };
}
