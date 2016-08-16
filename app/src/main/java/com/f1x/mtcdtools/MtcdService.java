package com.f1x.mtcdtools;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

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

/**
 * Created by COMPUTER on 2016-08-03.
 */
public class MtcdService extends android.app.Service implements MessageHandlerInterface {
    @Override
    public void onCreate() {
        super.onCreate();

        mServiceStarted = false;
        mKeyInputDispatchingActive = false;

        mKeyInputsStorage = new KeyInputsStorage(new FileReader(this), new FileWriter(this));
        mModePackagesStorage = new ModePackagesStorage(new FileReader(this), new FileWriter(this));
        mModePackagesRotator = new ModePackagesRotator();

        KeyPressEvaluator keyInputEvaluator = new KeyPressEvaluator(this, mModePackagesRotator);
        mKeyPressDispatcher = new KeyPressDispatcher(keyInputEvaluator);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mServiceStarted = false;
        mKeyInputDispatchingActive = false;
        unregisterReceiver(mKeyPressReceiver);
        mServiceMessageHandler.setTarget(null);

        scheduleServiceRestart();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        if(!mServiceStarted) {
            try {
                mKeyInputsStorage.read();
                mKeyPressDispatcher.updateKeyInputs(mKeyInputsStorage.getInputs());

                mModePackagesStorage.read();
                mModePackagesRotator.updatePackages(mModePackagesStorage.getPackages());

                registerReceiver(mKeyPressReceiver, mKeyPressReceiver.getIntentFilter());
                mServiceMessageHandler.setTarget(this);

                startForeground(SERVICE_ID, createNotification());
                mKeyInputDispatchingActive = true;
                mServiceStarted = true;
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

    void scheduleServiceRestart() {
        // WORKAROUND: Do not know why MTCD Android does not respect START_STICKY
        Intent restartService = new Intent(getApplicationContext(), this.getClass());
        restartService.setPackage(getPackageName());

        PendingIntent restartServiceIntent = PendingIntent.getService(getApplicationContext(), SERVICE_ID, restartService, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmManager = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + SERVICE_RESTART_DELAY_MS, restartServiceIntent);
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
            case Messaging.MessageIds.ADD_KEY_INPUT_REQUEST:
                handleKeyInputAdditionRequest(message);
                break;
            case Messaging.MessageIds.REMOVE_KEY_INPUT_REQUEST:
                handleKeyInputRemovalRequest(message);
                break;
            case Messaging.MessageIds.KEY_INPUTS_REQUEST:
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

    private void handleKeyInputAdditionRequest(Message request) {
        Message response = new Message();
        response.what = Messaging.MessageIds.ADD_KEY_INPUT_RESPONSE;
        response.arg1 = Messaging.KeyInputAdditionResult.FAILURE;

        try {
            mKeyInputsStorage.insert((KeyInput)request.obj);
            mKeyPressDispatcher.updateKeyInputs(mKeyInputsStorage.getInputs());
            response.arg1 = Messaging.KeyInputAdditionResult.SUCCEED;
            sendMessage(response, request.replyTo);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            sendMessage(response, request.replyTo);
        }
    }

    private void handleKeyInputRemovalRequest(Message request) {
        Message response = new Message();
        response.what = Messaging.MessageIds.REMOVE_KEY_INPUT_RESPONSE;
        response.arg1 = Messaging.KeyInputRemovalResult.FAILURE;

        try {
            mKeyInputsStorage.remove((KeyInput)request.obj);
            mKeyPressDispatcher.updateKeyInputs(mKeyInputsStorage.getInputs());

            response.arg1 = Messaging.KeyInputRemovalResult.SUCCEED;
            sendMessage(response, request.replyTo);
            sendKeyInputsChangedIndication(request.replyTo);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            sendMessage(response, request.replyTo);
        }
    }

    private void handleKeyInputsRequest(Message request) {
        Message response = new Message();
        response.what = Messaging.MessageIds.KEY_INPUTS_RESPONSE;
        response.obj = mKeyInputsStorage.getInputs();
        sendMessage(response, request.replyTo);
    }

    private void sendKeyInputsChangedIndication(Messenger messenger) {
        Message indication = new Message();
        indication.what = Messaging.MessageIds.KEY_INPUTS_CHANGED;
        sendMessage(indication, messenger);
    }

    private void sendMessage(Message message, Messenger messenger) {
        try {
            messenger.send(message);
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

    private boolean mServiceStarted;
    private boolean mKeyInputDispatchingActive;

    private KeyInputsStorage mKeyInputsStorage;
    private ModePackagesStorage mModePackagesStorage;
    private ModePackagesRotator mModePackagesRotator;

    private KeyPressDispatcher mKeyPressDispatcher;
    private final Messenger mMessenger = new Messenger(mServiceMessageHandler);

    private static final StaticMessageHandler mServiceMessageHandler = new StaticMessageHandler();
    private static final int SERVICE_ID = 1555;
    private static final int SERVICE_RESTART_DELAY_MS = 100;
}
