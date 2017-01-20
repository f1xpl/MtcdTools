package com.f1x.mtcdtools.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.f1x.mtcdtools.R;
import com.f1x.mtcdtools.StartActivityButtonClickListener;

public class CreateActionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_action);

        Button createKeyActionButton = (Button)findViewById(R.id.buttonCreateNewKeyAction);
        createKeyActionButton.setOnClickListener(new StartActivityButtonClickListener(this, CreateKeyActionActivity.class));

        Button createLaunchActionButton = (Button)findViewById(R.id.buttonCreateNewLaunchAction);
        createLaunchActionButton.setOnClickListener(new StartActivityButtonClickListener(this, CreateLaunchActionActivity.class));

        Button createStartActivityActionButton = (Button)findViewById(R.id.buttonCreateNewStartActivityAction);
        createStartActivityActionButton.setOnClickListener(new StartActivityButtonClickListener(this, CreateStartActivityActionActivity.class));

        Button createBroadcastIntentActionButton = (Button)findViewById(R.id.buttonCreateNewBroadcastIntentAction);
        createBroadcastIntentActionButton.setOnClickListener(new StartActivityButtonClickListener(this, CreateBroadcastIntentActionActivity.class));
    }
}
