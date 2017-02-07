package com.f1x.mtcdtools.activities;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import com.f1x.mtcdtools.R;
import com.f1x.mtcdtools.service.MtcdService;
import com.f1x.mtcdtools.service.ServiceBinder;

/**
 * Created by f1x on 2016-08-18.
 */
public abstract class ServiceActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.bindService(new Intent(this, MtcdService.class), mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unbindService(mServiceConnection);
    }

    protected abstract void onServiceConnected();

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if(service == null) {
                Toast.makeText(ServiceActivity.this, ServiceActivity.this.getText(R.string.ServiceUnavailable), Toast.LENGTH_LONG).show();
                ServiceActivity.this.finish();
            } else {
                mServiceBinder = (ServiceBinder)service;
                ServiceActivity.this.onServiceConnected();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServiceBinder = null;
        }
    };

    ServiceBinder mServiceBinder;
}
