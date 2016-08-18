package com.f1x.mtcdtools.services;

import android.app.Notification;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.f1x.mtcdtools.MessageHandlerInterface;
import com.f1x.mtcdtools.Messaging;
import com.f1x.mtcdtools.MtcdServiceWatchdog;
import com.f1x.mtcdtools.R;
import com.f1x.mtcdtools.StaticMessageHandler;
import com.f1x.mtcdtools.evaluation.KeyPressDispatcher;
import com.f1x.mtcdtools.evaluation.KeyPressEvaluator;
import com.f1x.mtcdtools.evaluation.ModePackagesRotator;
import com.f1x.mtcdtools.input.KeyPressReceiver;

import org.json.JSONException;

import java.io.IOException;

/**
 * Created by COMPUTER on 2016-08-03.
 */
public class MtcdService extends StorageService implements MessageHandlerInterface {
    @Override
    public void onCreate() {
        super.onCreate();

        mServiceInitialized = false;
        mKeyInputDispatchingActive = true;
        mModePackagesRotator = new ModePackagesRotator();
        KeyPressEvaluator keyInputEvaluator = new KeyPressEvaluator(this, mModePackagesRotator);
        mKeyPressDispatcher = new KeyPressDispatcher(keyInputEvaluator);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(mServiceInitialized) {
            unregisterReceiver(mKeyPressReceiver);
            MtcdServiceWatchdog.schedulerServiceRestart(this);
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
                mKeyInputsStorage.read();
                mKeyPressDispatcher.updateKeyInputs(mKeyInputsStorage.getInputs());

                mModePackagesStorage.read();
                mModePackagesRotator.updatePackages(mModePackagesStorage.getPackages());

                registerReceiver(mKeyPressReceiver, mKeyPressReceiver.getIntentFilter());
                mServiceMessageHandler.setTarget(this);

                startForeground(1555, createNotification());
                mServiceInitialized = true;
                mKeyInputDispatchingActive = true;
            } catch(IOException e) {
                e.printStackTrace();
                Toast.makeText(this, getString(R.string.ConfigurationFileReadError), Toast.LENGTH_LONG).show();
                stopSelf();
            } catch(JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, getString(R.string.ConfigurationFileParsingError), Toast.LENGTH_LONG).show();
                stopSelf();
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

    public void handleMessage(Message message) {
        switch(message.what) {
            case Messaging.MessageIds.EDIT_KEY_INPUTS_REQUEST:
                handleKeyInputsEditRequest(message);
                break;
            case Messaging.MessageIds.GET_KEY_INPUTS_REQUEST:
                handleKeyInputsRequest(message);
                break;
            case Messaging.MessageIds.SUSPEND_KEY_INPUT_DISPATCHING:
                mKeyInputDispatchingActive = false;
                break;
            case Messaging.MessageIds.RESUME_KEY_INPUT_DISPATCHING:
                mKeyInputDispatchingActive = true;
                break;
        }
    }

    @Override
    protected void handleKeyInputsEditResult(int result, Messenger replyTo) {
        if(result == Messaging.KeyInputsEditResult.SUCCEED) {
            mKeyPressDispatcher.updateKeyInputs(mKeyInputsStorage.getInputs());
        }

        Message response = new Message();
        response.what = Messaging.MessageIds.EDIT_KEY_INPUTS_RESPONSE;
        response.arg1 = result;

        try {
            replyTo.send(response);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void handleKeyInputsRequest(Message request) {
        Message response = new Message();
        response.what = Messaging.MessageIds.GET_KEY_INPUTS_RESPONSE;
        response.obj = mKeyInputsStorage.getInputs();

        try {
            request.replyTo.send(response);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private final KeyPressReceiver mKeyPressReceiver = new KeyPressReceiver() {
        @Override
        public void handleKeyInput(int keyCode) {
            if(mKeyInputDispatchingActive) {
                mKeyPressDispatcher.dispatch(keyCode);
            }
        }
    };

    private boolean mServiceInitialized;
    private boolean mKeyInputDispatchingActive;
    private ModePackagesRotator mModePackagesRotator;
    private KeyPressDispatcher mKeyPressDispatcher;
    private final Messenger mMessenger = new Messenger(mServiceMessageHandler);

    private static final StaticMessageHandler mServiceMessageHandler = new StaticMessageHandler();
}
