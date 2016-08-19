package com.f1x.mtcdtools.services;

import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.f1x.mtcdtools.MessageHandlerInterface;
import com.f1x.mtcdtools.Messaging;
import com.f1x.mtcdtools.input.KeyInput;
import com.f1x.mtcdtools.storage.FileReader;
import com.f1x.mtcdtools.storage.FileWriter;
import com.f1x.mtcdtools.storage.KeyInputsStorage;
import com.f1x.mtcdtools.storage.ModePackagesStorage;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by COMPUTER on 2016-08-16.
 */
public abstract class StorageService extends android.app.Service implements MessageHandlerInterface {
    @Override
    public void onCreate() {
        mKeyInputsStorage = new KeyInputsStorage(new FileReader(this), new FileWriter(this));
        mModePackagesStorage = new ModePackagesStorage(new FileReader(this), new FileWriter(this));
    }

    protected void readStorage() throws IOException, JSONException {
        mKeyInputsStorage.read();
        updateKeyInputs(mKeyInputsStorage.getInputs());

        mModePackagesStorage.read();
        updateModePackages(mModePackagesStorage.getPackages());
    }

    protected void handleKeyInputsEditRequest(Message request) {
        try {
            if(request.arg1 == Messaging.KeyInputsEditType.ADD) {
                mKeyInputsStorage.insert((KeyInput) request.obj);
            } else {
                mKeyInputsStorage.remove((KeyInput)request.obj);
            }

            handleKeyInputsEditResult(Messaging.KeyInputsEditResult.SUCCEED, request.replyTo);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            handleKeyInputsEditResult(Messaging.KeyInputsEditResult.FAILURE, request.replyTo);
        }
    }

    protected void handleKeyInputsEditResult(int result, Messenger replyTo) {
        if(result == Messaging.KeyInputsEditResult.SUCCEED) {
            updateKeyInputs(mKeyInputsStorage.getInputs());
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

    private void handleSaveModePackagesRequest(Message request) {
        try {
            List<String> packages = (List<String>)request.obj;
            mModePackagesStorage.setPackages(packages);
            handleSaveModePackagesResult(Messaging.ModePackagesSaveResult.SUCCEED, request.replyTo);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            handleSaveModePackagesResult(Messaging.ModePackagesSaveResult.FAILURE, request.replyTo);
        }
    }

    private void handleSaveModePackagesResult(int result, Messenger replyTo) {
        if(result == Messaging.ModePackagesSaveResult.SUCCEED) {
            updateModePackages(mModePackagesStorage.getPackages());
        }

        Message response = new Message();
        response.what = Messaging.MessageIds.SAVE_MODE_PACKAGES_RESPONSE;
        response.arg1 = result;

        try {
            replyTo.send(response);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void handleModePackagesRequest(Message request) {
        Message response = new Message();
        response.what = Messaging.MessageIds.GET_MODE_PACKAGES_RESPONSE;
        response.obj = mModePackagesStorage.getPackages();

        try {
            request.replyTo.send(response);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleMessage(Message message) {
        switch(message.what) {
            case Messaging.MessageIds.EDIT_KEY_INPUTS_REQUEST:
                handleKeyInputsEditRequest(message);
                break;
            case Messaging.MessageIds.GET_KEY_INPUTS_REQUEST:
                handleKeyInputsRequest(message);
                break;
            case Messaging.MessageIds.SAVE_MODE_PACKAGES_REQUEST:
                handleSaveModePackagesRequest(message);
                break;
            case Messaging.MessageIds.GET_MODE_PACKAGES_REQUEST:
                handleModePackagesRequest(message);
                break;
        }
    }

    protected abstract void updateKeyInputs(Map<Integer, KeyInput> keyInputs);
    protected abstract void updateModePackages(List<String> packages);

    private KeyInputsStorage mKeyInputsStorage;
    private ModePackagesStorage mModePackagesStorage;
}
