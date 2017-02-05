package com.f1x.mtcdtools.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.widget.Toast;

import com.f1x.mtcdtools.R;
import com.f1x.mtcdtools.NamedObjectDispatcher;
import com.f1x.mtcdtools.storage.NamedObject;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by COMPUTER on 2017-02-03.
 */

public class VoiceDispatchActivity extends ServiceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_dispatch_action);

        mDispatcher = new NamedObjectDispatcher();
    }

    @Override
    protected void onServiceConnected() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault().toString());

        startActivityForResult(intent, 1);
    }

    NamedObject findNamedObject(String text) {
        String actionExecutionCommand = mServiceBinder.getConfiguration().getExecuteActionVoiceCommandText();

        if (text.contains(actionExecutionCommand)) {
            String actionName = text.replace(actionExecutionCommand , "").trim();
            return mServiceBinder.getNamedObjectsStorage().getItem(actionName);
        }

        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        boolean commandExecuted = false;

        if(data != null) {
            ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            for (int i = 0; i < text.size() && !commandExecuted; ++i) {
                final NamedObject namedObject = findNamedObject(text.get(i));

                if(namedObject != null) {
                    commandExecuted = true;
                    new Handler().postDelayed(new Runnable() {
                        @Override public void run() {
                            mDispatcher.dispatch(namedObject, VoiceDispatchActivity.this);
                            VoiceDispatchActivity.this.finish();
                        }
                    }, ACTION_EXECUTION_DELAY_MS);
                }
            }
        }

        if (!commandExecuted) {
            Toast.makeText(this, this.getText(R.string.NotRecognizedCommand), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private NamedObjectDispatcher mDispatcher;

    // Recognizer will pause playback during speech recognition
    // If user wants to execute any media button action, it can
    // collides and overlaps with recognizer
    private final int ACTION_EXECUTION_DELAY_MS = 1000;
}
