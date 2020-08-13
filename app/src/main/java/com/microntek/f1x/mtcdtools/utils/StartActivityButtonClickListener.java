package com.microntek.f1x.mtcdtools.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

/**
 * Created by f1x on 2017-01-19.
 */

public class StartActivityButtonClickListener implements Button.OnClickListener {
    public StartActivityButtonClickListener(Context context, Class<? extends Activity> activity) {
        mContext = context;
        mActivity = activity;
    }

    @Override
    public void onClick(View view) {
        mContext.startActivity(new Intent(mContext, mActivity));
    }

    private final Class<? extends Activity> mActivity;
    private final Context mContext;
}
