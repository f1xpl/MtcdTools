package com.f1x.mtcdtools.service;

import android.os.Binder;

import com.f1x.mtcdtools.configuration.Configuration;
import com.f1x.mtcdtools.input.PressedKeysSequenceManager;
import com.f1x.mtcdtools.storage.ActionsListsStorage;
import com.f1x.mtcdtools.storage.ActionsStorage;
import com.f1x.mtcdtools.storage.KeysSequenceBindingsStorage;

/**
 * Created by COMPUTER on 2017-01-07.
 */

public abstract class ServiceBinder extends Binder {
    public abstract KeysSequenceBindingsStorage getKeysSequenceBindingsStorage();
    public abstract ActionsStorage getActionsStorage();
    public abstract ActionsListsStorage getActionsListsStorage();
    public abstract PressedKeysSequenceManager getPressedKeysSequenceManager();
    public abstract Configuration getConfiguration();
}
