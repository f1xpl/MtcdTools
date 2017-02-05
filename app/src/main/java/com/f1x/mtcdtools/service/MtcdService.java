package com.f1x.mtcdtools.service;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.f1x.mtcdtools.input.KeysSequenceDispatcher;
import com.f1x.mtcdtools.configuration.Configuration;
import com.f1x.mtcdtools.R;
import com.f1x.mtcdtools.activities.MainActivity;
import com.f1x.mtcdtools.input.PressedKeysSequenceManager;
import com.f1x.mtcdtools.storage.FileReader;
import com.f1x.mtcdtools.storage.FileWriter;
import com.f1x.mtcdtools.storage.KeysSequenceBindingsStorage;
import com.f1x.mtcdtools.storage.NamedObjectsStorage;
import com.f1x.mtcdtools.storage.exceptions.DuplicatedEntryException;
import com.f1x.mtcdtools.storage.exceptions.EntryCreationFailed;

import org.json.JSONException;

import java.io.IOException;

/**
 * Created by COMPUTER on 2016-08-03.
 */
public class MtcdService extends android.app.Service {
    @Override
    public void onCreate() {
        super.onCreate();

        mForceRestart = false;
        mServiceInitialized = false;

        mConfiguration = new Configuration(this.getSharedPreferences(MainActivity.APP_NAME, Context.MODE_PRIVATE));

        FileReader fileReader = new FileReader(this);
        FileWriter fileWriter = new FileWriter(this);
        mNamedObjectsStorage = new NamedObjectsStorage(fileReader, fileWriter);
        mKeysSequenceBindingsStorage = new KeysSequenceBindingsStorage(fileReader, fileWriter);
        mKeysSequenceDispatcher = new KeysSequenceDispatcher(this, mNamedObjectsStorage, mKeysSequenceBindingsStorage);
        mPressedKeysSequenceManager = new PressedKeysSequenceManager(mConfiguration);
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

        mPressedKeysSequenceManager.popListener(mKeysSequenceDispatcher);
        mPressedKeysSequenceManager.destroy();
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
                mNamedObjectsStorage.read();
                mKeysSequenceBindingsStorage.read();
                registerReceiver(mPressedKeysSequenceManager, mPressedKeysSequenceManager.getIntentFilter());

                mPressedKeysSequenceManager.pushListener(mKeysSequenceDispatcher);
                mServiceInitialized = true;
                mForceRestart = true;
                startForeground(1555, createNotification());
            } catch (JSONException | IOException | DuplicatedEntryException | EntryCreationFailed e) {
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
                .setSmallIcon(R.drawable.service_notification_icon)
                .setOngoing(true)
                .build();
    }

    private boolean mForceRestart;
    private boolean mServiceInitialized;
    private NamedObjectsStorage mNamedObjectsStorage;
    private KeysSequenceBindingsStorage mKeysSequenceBindingsStorage;
    private PressedKeysSequenceManager mPressedKeysSequenceManager;
    private KeysSequenceDispatcher mKeysSequenceDispatcher;
    private Configuration mConfiguration;

    private final ServiceBinder mServiceBinder = new ServiceBinder() {
        @Override
        public KeysSequenceBindingsStorage getKeysSequenceBindingsStorage() {
            return mKeysSequenceBindingsStorage;
        }

        @Override
        public NamedObjectsStorage getNamedObjectsStorage() {
            return mNamedObjectsStorage;
        }

        @Override
        public PressedKeysSequenceManager getPressedKeysSequenceManager() {
            return mPressedKeysSequenceManager;
        }

        @Override
        public Configuration getConfiguration() {
            return mConfiguration;
        }
    };
}
