package com.f1x.mtcdtools.services;

import android.app.Notification;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.f1x.mtcdtools.R;
import com.f1x.mtcdtools.evaluation.KeyPressDispatcher;
import com.f1x.mtcdtools.evaluation.KeyPressEvaluator;
import com.f1x.mtcdtools.evaluation.ModePackagesRotator;
import com.f1x.mtcdtools.input.KeyInput;
import com.f1x.mtcdtools.input.KeyPressReceiver;
import com.f1x.mtcdtools.storage.FileReader;
import com.f1x.mtcdtools.storage.FileWriter;
import com.f1x.mtcdtools.storage.KeyInputsStorage;
import com.f1x.mtcdtools.storage.ModePackagesStorage;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by COMPUTER on 2016-08-03.
 */
public class MtcdService extends android.app.Service {
    @Override
    public void onCreate() {
        super.onCreate();

        mForceRestart = true;
        mServiceInitialized = false;
        mKeyInputDispatchingActive = true;
        mKeyInputsStorage = new KeyInputsStorage(new FileReader(this), new FileWriter(this));
        mModePackagesStorage = new ModePackagesStorage(new FileReader(this), new FileWriter(this));
        mModePackagesRotator = new ModePackagesRotator();
        KeyPressEvaluator keyInputEvaluator = new KeyPressEvaluator(this, mModePackagesRotator);
        mKeyPressDispatcher = new KeyPressDispatcher(keyInputEvaluator);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(mForceRestart) {
            MtcdServiceWatchdog.scheduleServiceRestart(this);
        }

        if(mServiceInitialized) {
            unregisterReceiver(mKeyPressReceiver);
        }

        mServiceInitialized = false;
        mKeyInputDispatchingActive = false;
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
                mKeyInputsStorage.read();
                mModePackagesStorage.read();

                registerReceiver(mKeyPressReceiver, mKeyPressReceiver.getIntentFilter());

                mServiceInitialized = true;
                mKeyInputDispatchingActive = true;
                startForeground(1555, createNotification());
            } catch(IOException e) {
                e.printStackTrace();
                Toast.makeText(this, getString(R.string.ConfigurationFileReadError), Toast.LENGTH_LONG).show();
                stopSelf();
                mForceRestart = false;
            } catch(JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, getString(R.string.ConfigurationFileParsingError), Toast.LENGTH_LONG).show();
                stopSelf();
                mForceRestart = false;
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

    private final KeyPressReceiver mKeyPressReceiver = new KeyPressReceiver() {
        @Override
        public void handleKeyInput(int keyCode) {
            if(mKeyInputDispatchingActive) {
                mKeyPressDispatcher.dispatch(keyCode);
            }
        }
    };

    private boolean mForceRestart;
    private boolean mServiceInitialized;
    private boolean mKeyInputDispatchingActive;
    private KeyInputsStorage mKeyInputsStorage;
    private ModePackagesStorage mModePackagesStorage;
    private ModePackagesRotator mModePackagesRotator;
    private KeyPressDispatcher mKeyPressDispatcher;

    private final ServiceBinder mServiceBinder = new ServiceBinder() {
        @Override
        public boolean addKeyInput(KeyInput keyInput) {
            try {
                mKeyInputsStorage.insert(keyInput);
                mKeyPressDispatcher.updateKeyInputs(mKeyInputsStorage.getInputs());
                return true;
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        public boolean removeKeyInput(KeyInput keyInput) {
            try {
                mKeyInputsStorage.remove(keyInput);
                mKeyPressDispatcher.updateKeyInputs(mKeyInputsStorage.getInputs());
                return true;
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        public Map<Integer, KeyInput> getKeyInputs() {
            return mKeyInputsStorage.getInputs();
        }

        @Override
        public boolean saveModePackages(List<String> packages) {
            try {
                mModePackagesStorage.setPackages(packages);
                mModePackagesRotator.updatePackages(getModePackages());
                return true;
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        public List<String> getModePackages() {
            return mModePackagesStorage.getPackages();
        }

        @Override
        public void suspendKeyInputsProcessing() {
            MtcdService.this.mKeyInputDispatchingActive = false;
        }

        @Override
        public void resumeKeyInputsProcessing() {
            MtcdService.this.mKeyInputDispatchingActive = true;
        }
    };
}
