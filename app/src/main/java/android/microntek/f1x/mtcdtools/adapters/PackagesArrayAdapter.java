package android.microntek.f1x.mtcdtools.adapters;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import android.microntek.f1x.mtcdtools.R;
import android.microntek.f1x.mtcdtools.adapters.entries.PackageEntry;

/**
 * Created by f1x on 2016-08-08.
 */
class PackagesArrayAdapter extends ArrayAdapter<PackageEntry> {
    PackagesArrayAdapter(Context context) {
        super(context, R.layout.layout_package_row, R.id.textViewPackageName);
        mPackageManager = context.getPackageManager();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView applicationNameTextView = (TextView)super.getView(position, convertView, parent);
        PackageEntry packageEntry = getItem(position);

        if(packageEntry != null) {
            Drawable packageIcon = packageEntry.getIcon();
            packageIcon.setBounds(new Rect(0, 0, 64, 64));

            applicationNameTextView.setText(packageEntry.getLabel());
            applicationNameTextView.setCompoundDrawables(packageIcon, null, null, null);
        }

        return applicationNameTextView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    PackageEntry getPackageEntry(String packageName) {
        try {
            Drawable packageIcon = mPackageManager.getApplicationIcon(packageName);
            ApplicationInfo applicationInfo = mPackageManager.getApplicationInfo(packageName, 0);
            return new PackageEntry(packageIcon, mPackageManager.getApplicationLabel(applicationInfo).toString(), packageName);
        } catch(PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    final PackageManager mPackageManager;
}
