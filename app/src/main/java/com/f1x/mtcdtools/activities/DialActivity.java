package com.f1x.mtcdtools.activities;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.microntek.mtcser.BTServiceInf;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.Toast;

/**
 * Created by COMPUTER on 2017-02-16.
 */

public class DialActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent startBluetoothServiceIntent = new Intent();
        startBluetoothServiceIntent.setComponent(new ComponentName("android.microntek.mtcser", "android.microntek.mtcser.BTSerialService"));

        if(!bindService(startBluetoothServiceIntent, mServiceConnection, BIND_AUTO_CREATE)) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
    }

    String getPhoneNumber(Intent intent) {
        Uri uri = intent.getData();

        if(uri != null && uri.getScheme().equals("tel")) {
            return uri.toString().replace("tel:", "").replace(" ", "");
        } else {
            return null;
        }
    }

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            if(iBinder != null) {
                BTServiceInf bluetoothServiceInterface = BTServiceInf.Stub.asInterface(iBinder);

                if(bluetoothServiceInterface != null) {
                    final String number = DialActivity.this.getPhoneNumber(DialActivity.this.getIntent());

                    if (number != null && !number.isEmpty()) {
                        try {
                            bluetoothServiceInterface.dialOut(number);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                            Toast.makeText(DialActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }

            DialActivity.this.finish();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    };
}
