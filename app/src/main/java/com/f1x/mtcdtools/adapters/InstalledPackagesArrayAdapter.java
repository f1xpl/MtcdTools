package com.f1x.mtcdtools.adapters;

import android.content.Context;
import android.content.pm.ApplicationInfo;

/**
 * Created by COMPUTER on 2016-08-19.
 */
public class InstalledPackagesArrayAdapter extends PackageEntryArrayAdapter {
    public InstalledPackagesArrayAdapter(Context context) {
        super(context);

        for(ApplicationInfo applicationInfo : mPackageManager.getInstalledApplications(0)) {
            PackageEntry packageEntry = getPackageEntry(applicationInfo.packageName);

            if(packageEntry != null) {
                add(packageEntry);
            }
        }
    }

    public int getPosition(String packageName) {
        for(int i = 0; i < getCount(); ++i) {
            PackageEntry packageEntry = getItem(i);
            if(packageEntry.getName().equals(packageName)) {
                return i;
            }
        }

        return INVALID_INDEX;
    }

    public static final int INVALID_INDEX = -1;
}
