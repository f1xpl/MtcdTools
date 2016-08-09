package com.f1x.mtcdtools;

import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.widget.Toast;

import com.f1x.mtcdtools.keys.evaluation.KeyPressDispatcher;
import com.f1x.mtcdtools.keys.evaluation.KeyPressEvaluator;
import com.f1x.mtcdtools.keys.input.KeyInput;
import com.f1x.mtcdtools.keys.input.KeyPressReceiver;
import com.f1x.mtcdtools.keys.storage.KeyInputsFileReader;
import com.f1x.mtcdtools.keys.storage.KeyInputsFileWriter;
import com.f1x.mtcdtools.keys.storage.KeyInputsStorage;

import org.json.JSONException;

import java.io.IOException;

/**
 * Created by COMPUTER on 2016-08-03.
 */
public class MtcdService extends android.app.Service implements MessageHandlerInterface {
    @Override
    public void onCreate() {
        super.onCreate();

        KeyInputsFileReader keyInputsFileReader = new KeyInputsFileReader(this);
        KeyInputsFileWriter keyInputsFileWriter = new KeyInputsFileWriter(this);

        try {
            mKeyInputsStorage = new KeyInputsStorage(keyInputsFileReader, keyInputsFileWriter);
            mKeyInputsStorage.read();
            KeyPressEvaluator keyInputEvaluator = new KeyPressEvaluator(this);
            mKeyPressDispatcher = new KeyPressDispatcher(keyInputEvaluator, mKeyInputsStorage.getInputs());

            mServiceMessageHandler.setTarget(this);
            mKeyInputDispatchingActive = true;
            registerReceiver(mKeyPressReceiver, mKeyPressReceiver.getIntentFilter());
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

    @Override
    public void onDestroy() {
        super.onDestroy();

        mKeyInputDispatchingActive = false;
        unregisterReceiver(mKeyPressReceiver);
        mServiceMessageHandler.setTarget(null);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
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
            sendKeyInputsChanged(request.replyTo);
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

    private void sendKeyInputsChanged(Messenger messenger) {
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
        public void handleKeyInput(int keyCode, int actionType) {
            if(mKeyInputDispatchingActive) {
                mKeyPressDispatcher.dispatch(keyCode, actionType);
            }
        }
    };

    private KeyInputsStorage mKeyInputsStorage;
    private KeyPressDispatcher mKeyPressDispatcher;
    private boolean mKeyInputDispatchingActive;
    private final Messenger mMessenger = new Messenger(mServiceMessageHandler);
    private static final StaticMessageHandler mServiceMessageHandler = new StaticMessageHandler();
}
