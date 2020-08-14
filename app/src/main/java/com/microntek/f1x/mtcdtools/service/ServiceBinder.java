package com.microntek.f1x.mtcdtools.service;

import android.os.Binder;

import com.microntek.f1x.mtcdtools.service.configuration.Configuration;
import com.microntek.f1x.mtcdtools.service.input.PressedKeysSequenceManager;
import com.microntek.f1x.mtcdtools.service.dispatching.NamedObjectDispatcher;
import com.microntek.f1x.mtcdtools.service.storage.AutorunStorage;
import com.microntek.f1x.mtcdtools.service.storage.KeysSequenceBindingsStorage;
import com.microntek.f1x.mtcdtools.service.storage.NamedObjectsStorage;

/**
 * Created by f1x on 2017-01-07.
 */

public abstract class ServiceBinder extends Binder {
    public abstract KeysSequenceBindingsStorage getKeysSequenceBindingsStorage();
    public abstract NamedObjectsStorage getNamedObjectsStorage();
    public abstract PressedKeysSequenceManager getPressedKeysSequenceManager();
    public abstract Configuration getConfiguration();
    public abstract NamedObjectDispatcher getNamedObjectsDispatcher();
    public abstract AutorunStorage getAutorunStorage();
}
