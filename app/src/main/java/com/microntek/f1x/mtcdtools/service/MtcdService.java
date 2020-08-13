package com.microntek.f1x.mtcdtools.service;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.microntek.f1x.mtcdtools.R;
import com.microntek.f1x.mtcdtools.service.configuration.Configuration;
import com.microntek.f1x.mtcdtools.service.dispatching.DispatchingIndicationPlayer;
import com.microntek.f1x.mtcdtools.service.dispatching.KeysSequenceDispatcher;
import com.microntek.f1x.mtcdtools.service.input.PX3PressedKeysSequenceManager;
import com.microntek.f1x.mtcdtools.service.input.PX5PressedKeysSequenceManager;
import com.microntek.f1x.mtcdtools.service.input.PressedKeysSequenceManager;
import com.microntek.f1x.mtcdtools.service.dispatching.NamedObjectDispatcher;
import com.microntek.f1x.mtcdtools.service.storage.AutorunStorage;
import com.microntek.f1x.mtcdtools.service.storage.FileReader;
import com.microntek.f1x.mtcdtools.service.storage.FileWriter;
import com.microntek.f1x.mtcdtools.service.storage.KeysSequenceBindingsStorage;
import com.microntek.f1x.mtcdtools.service.storage.NamedObjectsStorage;
import com.microntek.f1x.mtcdtools.service.storage.exceptions.DuplicatedEntryException;
import com.microntek.f1x.mtcdtools.service.storage.exceptions.EntryCreationFailed;
import com.microntek.f1x.mtcdtools.utils.PlatformChecker;

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
        mConfiguration = new Configuration(this.getSharedPreferences(APP_NAME, Context.MODE_PRIVATE));
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
            unregisterReceiver(mScreenOnBroadcastReceiver);
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
            registerReceiver(mScreenOnBroadcastReceiver, new IntentFilter(Intent.ACTION_SCREEN_ON));
            mForceRestart = true;
            mServiceInitialized = true;

            if(ACTION_AUTORUN.equals(intent.getAction())) {
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

    private final BroadcastReceiver mScreenOnBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {
                mNamedObjectsDispatcher.dispatch(mAutorunStorage.getItems(), MtcdService.this);
            }
        }
    };

    public static final String ACTION_AUTORUN = "com.microntek.f1x.mtcdtools.action.autorun";
    private static final String APP_NAME = "MtcdTools";
}
