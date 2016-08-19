package com.f1x.mtcdtools.activities;

import android.os.Bundle;
import android.os.Message;
import android.os.RemoteException;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.f1x.mtcdtools.Messaging;
import com.f1x.mtcdtools.R;
import com.f1x.mtcdtools.adapters.InstalledPackagesArrayAdapter;
import com.f1x.mtcdtools.adapters.PackageEntry;
import com.f1x.mtcdtools.adapters.PackageEntryArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public class EditModePackagesActivity extends ServiceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_mode_packages);

        InstalledPackagesArrayAdapter installedPackagesArrayAdapter = new InstalledPackagesArrayAdapter(this);
        final Spinner packagesListSpinner = (Spinner)findViewById(R.id.spinnerInstalledPackagesModePackages);
        packagesListSpinner.setAdapter(installedPackagesArrayAdapter);
        //-------------------------------------------------------------------------------------

        Button saveButton = (Button)findViewById(R.id.buttonSaveModePackages);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendSaveModePackagesRequest();
            }
        });
        //-------------------------------------------------------------------------------------

        Button cancelButton = (Button)findViewById(R.id.buttonCancelModePackages);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //-------------------------------------------------------------------------------------

        mModePackagesArrayAdapter = new PackageEntryArrayAdapter(this);
        ListView modePackagesListView = (ListView)findViewById(R.id.listViewModePackages);
        modePackagesListView.setAdapter(mModePackagesArrayAdapter);
        modePackagesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                PackageEntry packageEntry = mModePackagesArrayAdapter.getItem(position);
                if(packageEntry != null) {
                    mModePackagesArrayAdapter.remove(packageEntry);
                }

                return true;
            }
        });
        //-------------------------------------------------------------------------------------

        Button addButton = (Button)findViewById(R.id.buttonAddModePackage);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PackageEntry packageEntry = (PackageEntry)packagesListSpinner.getSelectedItem();
                mModePackagesArrayAdapter.add(packageEntry);
            }
        });
        //-------------------------------------------------------------------------------------
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(mServiceMessenger != null) {
            sendMessage(Messaging.MessageIds.GET_MODE_PACKAGES_REQUEST);
        }
    }

    @Override
    protected void onServiceConnected() {
        sendMessage(Messaging.MessageIds.GET_MODE_PACKAGES_REQUEST);
    }

    @Override
    public void handleMessage(Message message) {
        switch(message.what) {
            case Messaging.MessageIds.GET_MODE_PACKAGES_RESPONSE:
                mModePackagesArrayAdapter.reset((List<String>)message.obj);
                break;
            case Messaging.MessageIds.SAVE_MODE_PACKAGES_RESPONSE:
                handleSaveModePackagesResult(message);
                break;
        }
    }

    private void sendSaveModePackagesRequest() {
        Message message = new Message();
        message.what = Messaging.MessageIds.SAVE_MODE_PACKAGES_REQUEST;
        message.replyTo = mMessenger;

        ArrayList<String> modePackagesList = new ArrayList<>();

        for(int i = 0; i < mModePackagesArrayAdapter.getCount(); ++i) {
            PackageEntry packageEntry = mModePackagesArrayAdapter.getItem(i);
            modePackagesList.add(packageEntry.getName());
        }

        message.obj = modePackagesList;

        try {
            mServiceMessenger.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void handleSaveModePackagesResult(Message message) {
        if(message.arg1 == Messaging.ModePackagesSaveResult.SUCCEED) {
            finish();
        } else {
            Toast.makeText(this, getString(R.string.ModePackagesSaveFailure), Toast.LENGTH_LONG).show();
        }
    }

    private PackageEntryArrayAdapter mModePackagesArrayAdapter;
}
