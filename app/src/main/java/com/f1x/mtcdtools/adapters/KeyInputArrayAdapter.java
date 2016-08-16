package com.f1x.mtcdtools.adapters;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
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
        super(context, R.layout.binding_row, R.id.bindingName);
    }

    public void reset(Map<Integer, KeyInput> keyInputs) {
        clear();

        for (Map.Entry<Integer, KeyInput> input : keyInputs.entrySet()) {
            add(input.getValue());
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.binding_row, null);
        }

        KeyInput keyInput = getItem(position);
        if(keyInput == null) {
            return view;
        }

        Drawable bindingIcon = null;
        String bindingName = "";

        if(keyInput.getType() == KeyInputType.LAUNCH) {
            PackageManager packageManager = getContext().getPackageManager();

            try {
                bindingIcon = packageManager.getApplicationIcon(keyInput.getLaunchPackage());
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(keyInput.getLaunchPackage(), 0);
                bindingName = "[" + Integer.toString(keyInput.getKeyCode()) + "] [" + KeyInputType.toString(keyInput.getType()) + "] :: " + packageManager.getApplicationLabel(applicationInfo);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            bindingName = "[" + Integer.toString(keyInput.getKeyCode()) + "] [" + KeyInputType.toString(keyInput.getType()) + "]";
        }

        ImageView bindingIconImageView = (ImageView) view.findViewById(R.id.bindingIcon);
        bindingIconImageView.setImageDrawable(bindingIcon);

        TextView bindingNameTextView = (TextView)view.findViewById(R.id.bindingName);
        bindingNameTextView.setText(bindingName);

        return view;
    }
}
