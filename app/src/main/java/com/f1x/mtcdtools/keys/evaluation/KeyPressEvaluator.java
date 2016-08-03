package com.f1x.mtcdtools.keys.evaluation;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;

/**
 * Created by COMPUTER on 2016-08-03.
 */
public class KeyPressEvaluator implements KeyPressEvaluatorInterface {
    public KeyPressEvaluator(Context context) {
        mContext = context;
    }

    @Override
    public void evaluateLaunchInput(String packageName) {
        Intent intent = mContext.getPackageManager().getLaunchIntentForPackage(packageName);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    @Override
    public void evaluateMediaInput(int actionType, int androidKeyCode, String permissions) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_BUTTON);
        KeyEvent keyEvent = new KeyEvent(actionType, androidKeyCode);
        intent.putExtra(Intent.EXTRA_KEY_EVENT, keyEvent);
        mContext.sendOrderedBroadcast(intent, permissions);
    }

    private final Context mContext;
}
