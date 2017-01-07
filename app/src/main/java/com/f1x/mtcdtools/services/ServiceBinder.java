package com.f1x.mtcdtools.services;

import android.os.Binder;

import com.f1x.mtcdtools.input.KeyInput;

import java.util.List;
import java.util.Map;

/**
 * Created by COMPUTER on 2017-01-07.
 */

public abstract class ServiceBinder extends Binder {
    public abstract boolean addKeyInput(KeyInput keyInput);
    public abstract boolean removeKeyInput(KeyInput keyInput);
    public abstract Map<Integer, KeyInput> getKeyInputs();

    public abstract boolean saveModePackages(List<String> packages);
    public abstract List<String> getModePackages();

    public abstract void suspendKeyInputsProcessing();
    public abstract void resumeKeyInputsProcessing();
}
