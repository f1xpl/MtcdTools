package com.f1x.mtcdtools.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;

import com.f1x.mtcdtools.R;
import com.f1x.mtcdtools.storage.SpeechParser;

import java.util.List;
import java.util.Locale;

/**
 * Created by f1x on 2017-02-03.
 */

public class VoiceDispatchActivity extends ServiceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_dispatch_action);
        mSpeechParser = new SpeechParser();
    }

    @Override
    protected void onServiceConnected() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault().toString());

        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data != null) {
            List<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            final List<String> namedObjectsNames = mSpeechParser.parse(text, mServiceBinder.getConfiguration().getActionsVoiceDelimiter());

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mServiceBinder.getNamedObjectsDispatcher().dispatchNamedObjects(namedObjectsNames, VoiceDispatchActivity.this);
                    VoiceDispatchActivity.this.finish();
                }
            }, ACTION_EXECUTION_DELAY_MS);
        }
    }

    private SpeechParser mSpeechParser;

    // Recognizer will pause playback during speech recognition
    // If user wants to execute any media button action, it can
    // collides and overlaps with recognizer
    private final int ACTION_EXECUTION_DELAY_MS = 1000;
}
