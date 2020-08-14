package com.microntek.f1x.mtcdtools.service.dispatching.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;

import com.microntek.f1x.mtcdtools.R;
import com.microntek.f1x.mtcdtools.utils.SpeechParser;
import com.microntek.f1x.mtcdtools.service.ServiceActivity;
import com.microntek.f1x.mtcdtools.named.NamedObjectId;

import java.util.ArrayList;
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
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, this.getText(R.string.SpeechPrompt));

        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_OK || data == null) {
            VoiceDispatchActivity.this.finish();
            return;
        }

        List<String> texts = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
        if(texts.isEmpty()) {
            VoiceDispatchActivity.this.finish();
            return;
        }

        final List<String> extractedTexts = mSpeechParser.parse(texts.get(0), mServiceBinder.getConfiguration().getActionsVoiceDelimiter());
        if(extractedTexts.isEmpty()) {
            VoiceDispatchActivity.this.finish();
            return;
        }

        // Recognizer will pause playback during speech recognition
        // If user wants to execute any media button action, it can
        // collides and overlaps with recognizer
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                List<NamedObjectId> extractedNamedObjectIds = new ArrayList<>();
                for(String extractedText : extractedTexts) {
                    extractedNamedObjectIds.add(new NamedObjectId(extractedText));
                }

                mServiceBinder.getNamedObjectsDispatcher().dispatch(extractedNamedObjectIds, VoiceDispatchActivity.this);
            }
        }, mServiceBinder.getConfiguration().getVoiceCommandExecutionDelay());

        VoiceDispatchActivity.this.finish();
    }

    private SpeechParser mSpeechParser;
}
