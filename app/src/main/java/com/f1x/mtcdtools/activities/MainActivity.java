package com.f1x.mtcdtools.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.f1x.mtcdtools.R;
import com.f1x.mtcdtools.service.MtcdService;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button addNewBindingButton = (Button)findViewById(R.id.buttonAddNewBinding);
        addNewBindingButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddBindingsActivity.class));
            }
        });

        Button removeBindingsButton = (Button)findViewById(R.id.buttonRemoveBindings);
        removeBindingsButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RemoveBindingsActivity.class));
            }
        });

        Button editModePackagesButton = (Button)findViewById(R.id.buttonEditModePackages);
        editModePackagesButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, EditModePackagesActivity.class));
            }
        });

        this.startService(new Intent(this, MtcdService.class));
    }
}
