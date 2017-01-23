package com.f1x.mtcdtools.service;

import android.os.Binder;

import com.f1x.mtcdtools.input.PressedKeysSequenceManager;
import com.f1x.mtcdtools.storage.ActionsSequencesStorage;
import com.f1x.mtcdtools.storage.ActionsStorage;
import com.f1x.mtcdtools.storage.KeysSequenceBindingsStorage;

import java.util.List;
import java.util.Map;

/**
 * Created by COMPUTER on 2017-01-07.
 */

public abstract class ServiceBinder extends Binder {
    public abstract KeysSequenceBindingsStorage getKeysSequenceBindingsStorage();
    public abstract ActionsStorage getActionsStorage();
    public abstract ActionsSequencesStorage getActionsSequencesStorage();
    public abstract PressedKeysSequenceManager getPressedKeysSequenceManager();
}
