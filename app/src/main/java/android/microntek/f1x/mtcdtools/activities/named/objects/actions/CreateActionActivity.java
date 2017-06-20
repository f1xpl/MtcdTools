package android.microntek.f1x.mtcdtools.activities.named.objects.actions;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

import android.microntek.f1x.mtcdtools.R;
import android.microntek.f1x.mtcdtools.utils.StartActivityButtonClickListener;

public class CreateActionActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_action);

        Button createKeyActionButton = (Button)findViewById(R.id.buttonCreateNewKeyAction);
        createKeyActionButton.setOnClickListener(new StartActivityButtonClickListener(this, KeyActionActivity.class));

        Button createLaunchActionButton = (Button)findViewById(R.id.buttonCreateNewLaunchAction);
        createLaunchActionButton.setOnClickListener(new StartActivityButtonClickListener(this, LaunchActionActivity.class));

        Button createStartActivityActionButton = (Button)findViewById(R.id.buttonCreateNewStartActivityAction);
        createStartActivityActionButton.setOnClickListener(new StartActivityButtonClickListener(this, StartIntentActionActivity.class));

        Button createBroadcastIntentActionButton = (Button)findViewById(R.id.buttonCreateNewBroadcastIntentAction);
        createBroadcastIntentActionButton.setOnClickListener(new StartActivityButtonClickListener(this, BroadcastIntentActionActivity.class));
    }
}
