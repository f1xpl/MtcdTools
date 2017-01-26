package com.f1x.mtcdtools.service;

import android.os.Binder;

import com.f1x.mtcdtools.input.PressedKeysSequenceManager;
import com.f1x.mtcdtools.storage.ActionsListStorage;
import com.f1x.mtcdtools.storage.ActionsStorage;
import com.f1x.mtcdtools.storage.KeysSequenceBindingsStorage;

/**
 * Created by COMPUTER on 2017-01-07.
 */

public abstract class ServiceBinder extends Binder {
    public abstract KeysSequenceBindingsStorage getKeysSequenceBindingsStorage();
    public abstract ActionsStorage getActionsStorage();
    public abstract ActionsListStorage getActionsListStorage();
    public abstract PressedKeysSequenceManager getPressedKeysSequenceManager();
}
