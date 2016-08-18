package com.f1x.mtcdtools.adapters;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.f1x.mtcdtools.R;
import com.f1x.mtcdtools.input.KeyInput;
import com.f1x.mtcdtools.input.KeyInputType;

import java.util.Map;

/**
 * Created by COMPUTER on 2016-08-09.
 */
public class KeyInputArrayAdapter extends ArrayAdapter<KeyInput> {
    public KeyInputArrayAdapter(Context context) {
        super(context, R.layout.key_input_row);
    }

    public void reset(Map<Integer, KeyInput> keyInputs) {
        clear();

        for (Map.Entry<Integer, KeyInput> input : keyInputs.entrySet()) {
            add(input.getValue());
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView keyInputTextView = (TextView)super.getView(position, convertView, parent);

        KeyInput keyInput = getItem(position);
        if(keyInput == null) {
            return keyInputTextView;
        }

        Drawable applicationIcon = null;
        String labelText = "";

        if(keyInput.getType() == KeyInputType.LAUNCH) {
            PackageManager packageManager = getContext().getPackageManager();

            try {
                applicationIcon = packageManager.getApplicationIcon(keyInput.getLaunchPackage());
                applicationIcon.setBounds(new Rect(0, 0, 64, 64));
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(keyInput.getLaunchPackage(), 0);
                labelText = "[" + Integer.toString(keyInput.getKeyCode()) + "] [" + KeyInputType.toString(keyInput.getType()) + "] :: " + packageManager.getApplicationLabel(applicationInfo);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            labelText = "[" + Integer.toString(keyInput.getKeyCode()) + "] [" + KeyInputType.toString(keyInput.getType()) + "]";
        }


        keyInputTextView.setCompoundDrawables(null, null, applicationIcon, null);
        keyInputTextView.setText(labelText);

        return keyInputTextView;
    }
}
