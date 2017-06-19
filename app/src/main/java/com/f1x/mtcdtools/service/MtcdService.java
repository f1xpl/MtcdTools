package com.f1x.mtcdtools.service;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.microntek.CarManager;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.f1x.mtcdtools.PlatformChecker;
import com.f1x.mtcdtools.R;
import com.f1x.mtcdtools.activities.MainActivity;
import com.f1x.mtcdtools.configuration.Configuration;
import com.f1x.mtcdtools.dispatching.DispatchingIndicationPlayer;
import com.f1x.mtcdtools.dispatching.KeysSequenceDispatcher;
import com.f1x.mtcdtools.input.PX3PressedKeysSequenceManager;
import com.f1x.mtcdtools.input.PX5PressedKeysSequenceManager;
import com.f1x.mtcdtools.input.PressedKeysSequenceManager;
import com.f1x.mtcdtools.dispatching.NamedObjectDispatcher;
import com.f1x.mtcdtools.storage.AutorunStorage;
import com.f1x.mtcdtools.storage.FileReader;
import com.f1x.mtcdtools.storage.FileWriter;
import com.f1x.mtcdtools.storage.KeysSequenceBindingsStorage;
import com.f1x.mtcdtools.storage.NamedObjectsStorage;
import com.f1x.mtcdtools.storage.exceptions.DuplicatedEntryException;
import com.f1x.mtcdtools.storage.exceptions.EntryCreationFailed;

import org.json.JSONException;

import java.io.IOException;

/**
 * Created by f1x on 2016-08-03.
 */
public class MtcdService extends android.app.Service {
    @Override
    public void onCreate() {
        super.onCreate();

        mForceRestart = false;
        mServiceInitialized = false;

        FileReader fileReader = new FileReader(this);
        FileWriter fileWriter = new FileWriter(this);
        mNamedObjectsStorage = new NamedObjectsStorage(fileReader, fileWriter);
        mKeysSequenceBindingsStorage = new KeysSequenceBindingsStorage(fileReader, fileWriter);
        mAutorunStorage = new AutorunStorage(fileReader, fileWriter);
        mConfiguration = new Configuration(this.getSharedPreferences(MainActivity.APP_NAME, Context.MODE_PRIVATE));
        mPressedKeysSequenceManager = PlatformChecker.isPX5Platform() ? new PX5PressedKeysSequenceManager(mConfiguration) : new PX3PressedKeysSequenceManager(mConfiguration, this);
        mNamedObjectsDispatcher = new NamedObjectDispatcher(mNamedObjectsStorage);
        mDispatchingIndicationPlayer = new DispatchingIndicationPlayer(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(mForceRestart) {
            MtcdServiceWatchdog.scheduleServiceRestart(this);
        }

        if(mServiceInitialized) {
            mPressedKeysSequenceManager.destroy();
            mServiceInitialized = false;
        }

        mDispatchingIndicationPlayer.release();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mServiceBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        if(mServiceInitialized) {
            return START_STICKY;
        }

        try {
            mNamedObjectsStorage.read();
            mKeysSequenceBindingsStorage.read();
            mAutorunStorage.read();

            mPressedKeysSequenceManager.init();
            mPressedKeysSequenceManager.pushListener(new KeysSequenceDispatcher(this, mKeysSequenceBindingsStorage, mNamedObjectsDispatcher, mDispatchingIndicationPlayer));
            startForeground(1555, createNotification());
            mForceRestart = true;
            mServiceInitialized = true;

            if(intent.getAction() != null && intent.getAction().equals(ACTION_AUTORUN)) {
                mNamedObjectsDispatcher.dispatch(mAutorunStorage.getItems(), this);
            }
        } catch (JSONException | IOException | DuplicatedEntryException | EntryCreationFailed e) {
            e.printStackTrace();
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
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
    private AutorunStorage mAutorunStorage;
    private PressedKeysSequenceManager mPressedKeysSequenceManager;
    private Configuration mConfiguration;
    private NamedObjectDispatcher mNamedObjectsDispatcher;
    private DispatchingIndicationPlayer mDispatchingIndicationPlayer;

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

        @Override
        public NamedObjectDispatcher getNamedObjectsDispatcher() {
            return mNamedObjectsDispatcher;
        }

        @Override
        public AutorunStorage getAutorunStorage() {
            return mAutorunStorage;
        }
    };

    public static final String ACTION_AUTORUN = "com.f1x.mtcdtools.action.autorun";
}
