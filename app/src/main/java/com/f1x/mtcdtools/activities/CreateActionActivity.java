package com.f1x.mtcdtools.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.f1x.mtcdtools.R;
import com.f1x.mtcdtools.StartActivityButtonClickListener;
import com.f1x.mtcdtools.activities.actions.BroadcastIntentActionActivity;
import com.f1x.mtcdtools.activities.actions.LaunchActionActivity;
import com.f1x.mtcdtools.activities.actions.StartActivityActionActivity;
import com.f1x.mtcdtools.activities.actions.KeyActionActivity;

public class CreateActionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_action);

        Button createKeyActionButton = (Button)findViewById(R.id.buttonCreateNewKeyAction);
        createKeyActionButton.setOnClickListener(new StartActivityButtonClickListener(this, KeyActionActivity.class));

        Button createLaunchActionButton = (Button)findViewById(R.id.buttonCreateNewLaunchAction);
        createLaunchActionButton.setOnClickListener(new StartActivityButtonClickListener(this, LaunchActionActivity.class));

        Button createStartActivityActionButton = (Button)findViewById(R.id.buttonCreateNewStartActivityAction);
        createStartActivityActionButton.setOnClickListener(new StartActivityButtonClickListener(this, StartActivityActionActivity.class));

        Button createBroadcastIntentActionButton = (Button)findViewById(R.id.buttonCreateNewBroadcastIntentAction);
        createBroadcastIntentActionButton.setOnClickListener(new StartActivityButtonClickListener(this, BroadcastIntentActionActivity.class));
    }
}
