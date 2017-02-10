package com.f1x.mtcdtools.adapters.entries;

import android.graphics.drawable.Drawable;

/**
 * Created by f1x on 2016-08-08.
 */
public class PackageEntry {
    public PackageEntry(Drawable icon, String label, String name) {
        mIcon = icon;
        mLabel = label;
        mName = name;
    }

    public Drawable getIcon() {
        return mIcon;
    }

    public String getLabel() {
        return mLabel;
    }

    public String getName() {
        return mName;
    }

    private final Drawable mIcon;
    private final String mLabel;
    private final String mName;
}
