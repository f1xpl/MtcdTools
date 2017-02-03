package com.f1x.mtcdtools.activities;

import android.content.Intent;
import android.os.Bundle;
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

    String[] splitCommand(ArrayList<String> text) {
        return text == null ? null : (text.isEmpty() ? null : text.get(0).split("\\s+"));
    }

    void launchAction(String actionName) {
        Action action = mServiceBinder.getActionsStorage().getItem(actionName);

        if(action != null) {
            action.evaluate(this);
        } else {
            Toast.makeText(this, this.getText(R.string.ObjectNotFound), Toast.LENGTH_LONG).show();
        }
    }

    boolean executeCommand(String text) {
        String[] commandWords = text.split("\\s+");

        if (commandWords.length > 1 && commandWords[0].equalsIgnoreCase("włącz")) {
            Action action = mServiceBinder.getActionsStorage().getItem(commandWords[1]);

            if(action != null) {
                action.evaluate(this);
                return true;
            }
        }

        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data == null) {
            Toast.makeText(this, this.getText(R.string.NotRecognizedCommand), Toast.LENGTH_LONG).show();
        } else {
            ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            boolean commandExecuted = false;
            for (int i = 0; i < text.size() && !commandExecuted; ++i) {
                commandExecuted = executeCommand(text.get(i));
            }

            if (!commandExecuted) {
                Toast.makeText(this, this.getText(R.string.NotRecognizedCommand), Toast.LENGTH_LONG).show();
            }
        }

        finish();
    }
}
