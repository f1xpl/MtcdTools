package com.f1x.mtcdtools.services;

import android.os.Message;
import android.os.Messenger;

import com.f1x.mtcdtools.Messaging;
import com.f1x.mtcdtools.input.KeyInput;
import com.f1x.mtcdtools.storage.FileReader;
import com.f1x.mtcdtools.storage.FileWriter;
import com.f1x.mtcdtools.storage.KeyInputsStorage;
import com.f1x.mtcdtools.storage.ModePackagesStorage;

import org.json.JSONException;

import java.io.IOException;

/**
 * Created by COMPUTER on 2016-08-16.
 */
public abstract class StorageService extends android.app.Service {
    @Override
    public void onCreate() {
        mKeyInputsStorage = new KeyInputsStorage(new FileReader(this), new FileWriter(this));
        mModePackagesStorage = new ModePackagesStorage(new FileReader(this), new FileWriter(this));
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

    protected abstract void handleKeyInputsEditResult(int result, Messenger replyTo);

    protected KeyInputsStorage mKeyInputsStorage;
    protected ModePackagesStorage mModePackagesStorage;
}
