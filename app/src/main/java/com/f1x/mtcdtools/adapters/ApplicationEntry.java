package com.f1x.mtcdtools.adapters;

import android.graphics.drawable.Drawable;

/**
 * Created by COMPUTER on 2016-08-08.
 */
public class ApplicationEntry {
    public ApplicationEntry(Drawable icon, String name, String packageName) {
        mIcon = icon;
        mName = name;
        mPackageName = packageName;
    }

    public Drawable getIcon() {
        return mIcon;
    }

    public String getApplicationName() {
        return mName;
    }

    public String getPackageName() {
        return mPackageName;
    }

    private final Drawable mIcon;
    private final String mName;
    private final String mPackageName;
}
