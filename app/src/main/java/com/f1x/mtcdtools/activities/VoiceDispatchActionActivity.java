package com.f1x.mtcdtools.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.widget.Toast;

import com.f1x.mtcdtools.R;
import com.f1x.mtcdtools.actions.Action;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by COMPUTER on 2017-02-03.
 */

public class VoiceDispatchActionActivity extends ServiceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_dispatch_action);
    }

    @Override
    protected void onServiceConnected() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault().toString());

        startActivityForResult(intent, 1);
    }

    Action findCommandAction(String text) {
        String[] commandWords = text.split("\\s+");

        if (commandWords.length > 1 && commandWords[0].equalsIgnoreCase(mServiceBinder.getConfiguration().getExecuteActionVoiceCommandText())) {
            return mServiceBinder.getActionsStorage().getItem(commandWords[1]);
        }

        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data == null) {
            Toast.makeText(this, this.getText(R.string.NotRecognizedCommand), Toast.LENGTH_LONG).show();
        } else {
            ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            boolean commandExecuted = false;
            for (int i = 0; i < text.size() && !commandExecuted; ++i) {
                final Action action = findCommandAction(text.get(i));

                if(action != null) {
                    commandExecuted = true;

                    new Handler().postDelayed(new Runnable() {
                        @Override public void run() { action.evaluate(VoiceDispatchActionActivity.this); }
                    }, ACTION_EXECUTION_DELAY_MS);

                }
            }

            if (!commandExecuted) {
                Toast.makeText(this, this.getText(R.string.NotRecognizedCommand), Toast.LENGTH_LONG).show();
            }
        }

        finish();
    }

    // Recognizer will pause playback during speech recognition
    // If user wants to execute any media button action, it can
    // collides and overlaps with recognizer
    private final int ACTION_EXECUTION_DELAY_MS = 1000;
}
