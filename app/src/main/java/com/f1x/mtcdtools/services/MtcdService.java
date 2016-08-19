package com.f1x.mtcdtools.services;

import android.app.Notification;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.f1x.mtcdtools.Messaging;
import com.f1x.mtcdtools.R;
import com.f1x.mtcdtools.StaticMessageHandler;
import com.f1x.mtcdtools.evaluation.KeyPressDispatcher;
import com.f1x.mtcdtools.evaluation.KeyPressEvaluator;
import com.f1x.mtcdtools.evaluation.ModePackagesRotator;
import com.f1x.mtcdtools.input.KeyInput;
import com.f1x.mtcdtools.input.KeyPressReceiver;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by COMPUTER on 2016-08-03.
 */
public class MtcdService extends StorageService {
    @Override
    public void onCreate() {
        super.onCreate();

        mForceRestart = true;
        mServiceInitialized = false;
        mKeyInputDispatchingActive = true;
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
        mServiceMessageHandler.setTarget(null);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        if(!mServiceInitialized) {
            try {
                readStorage();

                registerReceiver(mKeyPressReceiver, mKeyPressReceiver.getIntentFilter());
                mServiceMessageHandler.setTarget(this);

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

    Notification createNotification() {
        return new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.MtcdServiceDescription))
                .setSmallIcon(R.drawable.notificationicon)
                .setOngoing(true)
                .build();
    }

    @Override
    public void handleMessage(Message message) {
        super.handleMessage(message);

        switch(message.what) {
             case Messaging.MessageIds.SUSPEND_KEY_INPUT_DISPATCHING:
                mKeyInputDispatchingActive = false;
                break;
            case Messaging.MessageIds.RESUME_KEY_INPUT_DISPATCHING:
                mKeyInputDispatchingActive = true;
                break;
        }
    }

    @Override
    protected void updateKeyInputs(Map<Integer, KeyInput> keyInputs) {
        mKeyPressDispatcher.updateKeyInputs(keyInputs);
    }

    @Override
    protected void updateModePackages(List<String> packages) {
        mModePackagesRotator.updatePackages(packages);
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
    private ModePackagesRotator mModePackagesRotator;
    private KeyPressDispatcher mKeyPressDispatcher;
    private final Messenger mMessenger = new Messenger(mServiceMessageHandler);

    private static final StaticMessageHandler mServiceMessageHandler = new StaticMessageHandler();
}
