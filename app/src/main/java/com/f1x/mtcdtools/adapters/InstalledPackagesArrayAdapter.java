package com.f1x.mtcdtools.adapters;

import android.content.Context;
import android.content.pm.ApplicationInfo;

import java.util.Comparator;

/**
 * Created by f1x on 2016-08-19.
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

        sort(new Comparator<PackageEntry>() {
            @Override
            public int compare(PackageEntry left, PackageEntry right) {
                return left.getLabel().compareTo(right.getLabel());
            }
        });
    }

    public int getPosition(String packageName) {
        for(int i = 0; i < getCount(); ++i) {
            PackageEntry packageEntry = getItem(i);
            if(packageEntry != null && packageEntry.getName().equals(packageName)) {
                return i;
            }
        }

        return INVALID_INDEX;
    }

    private static final int INVALID_INDEX = -1;
}
