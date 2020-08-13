package com.microntek;

import android.os.Bundle;
import android.os.RemoteException;

public class CarManageCallback extends ICarManageCallback.Stub {
    public void onStatusChanged(String type, Bundle bundle) throws RemoteException {
    }
}
