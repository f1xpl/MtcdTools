package com.f1x.mtcdtools.adapters;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;

import com.f1x.mtcdtools.input.KeyInput;
import com.f1x.mtcdtools.input.KeyInputType;

import java.util.Map;

/**
 * Created by COMPUTER on 2016-08-09.
 */
public class KeyInputArrayAdapter extends ArrayAdapter<KeyInput> {
    public KeyInputArrayAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_checked);
    }

    public void reset(Map<Integer, KeyInput> keyInputs) {
        clear();

        for (Map.Entry<Integer, KeyInput> input : keyInputs.entrySet()) {
            add(input.getValue());
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CheckedTextView keyInputTextView = (CheckedTextView)super.getView(position, convertView, parent);

        KeyInput keyInput = getItem(position);
        if(keyInput == null) {
            return keyInputTextView;
        }

        Drawable packageIcon = null;
        String labelText = "";

        if(keyInput.getType() == KeyInputType.LAUNCH) {
            PackageManager packageManager = getContext().getPackageManager();

            try {
                packageIcon = packageManager.getApplicationIcon(keyInput.getLaunchPackage());
                packageIcon.setBounds(new Rect(0, 0, 64, 64));
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(keyInput.getLaunchPackage(), 0);
                labelText = "[" + Integer.toString(keyInput.getKeyCode()) + "] [" + KeyInputType.toString(keyInput.getType()) + "] :: " + packageManager.getApplicationLabel(applicationInfo);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            labelText = "[" + Integer.toString(keyInput.getKeyCode()) + "] [" + KeyInputType.toString(keyInput.getType()) + "]";
        }


        keyInputTextView.setCompoundDrawables(null, null, packageIcon, null);
        keyInputTextView.setText(labelText);

        return keyInputTextView;
    }
}
