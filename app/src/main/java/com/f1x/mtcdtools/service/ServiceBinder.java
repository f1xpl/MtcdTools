package com.f1x.mtcdtools.service;

import android.os.Binder;

import com.f1x.mtcdtools.configuration.Configuration;
import com.f1x.mtcdtools.input.PressedKeysSequenceManager;
import com.f1x.mtcdtools.dispatching.NamedObjectDispatcher;
import com.f1x.mtcdtools.storage.KeysSequenceBindingsStorage;
import com.f1x.mtcdtools.storage.NamedObjectsStorage;

/**
 * Created by f1x on 2017-01-07.
 */

public abstract class ServiceBinder extends Binder {
    public abstract KeysSequenceBindingsStorage getKeysSequenceBindingsStorage();
    public abstract NamedObjectsStorage getNamedObjectsStorage();
    public abstract PressedKeysSequenceManager getPressedKeysSequenceManager();
    public abstract Configuration getConfiguration();
    public abstract NamedObjectDispatcher getNamedObjectsDispatcher();
}
