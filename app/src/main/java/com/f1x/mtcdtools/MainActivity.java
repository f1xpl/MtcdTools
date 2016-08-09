package com.f1x.mtcdtools;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button addNewBindingButton = (Button)findViewById(R.id.buttonAddNewBinding);
        addNewBindingButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, NewBindingActivity.class));
            }
        });

        this.startService(new Intent(this, MtcdService.class));
    }
}
