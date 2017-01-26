package com.f1x.mtcdtools.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.f1x.mtcdtools.R;
import com.f1x.mtcdtools.StartActivityButtonClickListener;
import com.f1x.mtcdtools.activities.actions.CreateActionActivity;
import com.f1x.mtcdtools.activities.actions.ManageActionsActivity;
import com.f1x.mtcdtools.service.MtcdService;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button createActionButton = (Button)findViewById(R.id.buttonCreateNewAction);
        createActionButton.setOnClickListener(new StartActivityButtonClickListener(this, CreateActionActivity.class));

        Button createBindingButton = (Button)findViewById(R.id.buttonCreateNewBinding);
        createBindingButton.setOnClickListener(new StartActivityButtonClickListener(this, CreateBindingActivity.class));

        Button createActionsListButton = (Button)findViewById(R.id.buttonCreateNewActionsList);
        createActionsListButton.setOnClickListener(new StartActivityButtonClickListener(this, CreateActionsListActivity.class));

        Button manageActionsButton = (Button)findViewById(R.id.buttonManageActions);
        manageActionsButton.setOnClickListener(new StartActivityButtonClickListener(this, ManageActionsActivity.class));

        this.startService(new Intent(this, MtcdService.class));
    }
}
