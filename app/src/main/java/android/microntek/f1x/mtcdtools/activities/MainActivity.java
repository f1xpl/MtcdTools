package android.microntek.f1x.mtcdtools.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import android.microntek.f1x.mtcdtools.R;
import android.microntek.f1x.mtcdtools.utils.StartActivityButtonClickListener;
import android.microntek.f1x.mtcdtools.activities.input.BindingActivity;
import android.microntek.f1x.mtcdtools.activities.named.objects.actions.CreateActionActivity;
import android.microntek.f1x.mtcdtools.activities.managing.ManageAutorunActivity;
import android.microntek.f1x.mtcdtools.activities.managing.ManageBindingsActivity;
import android.microntek.f1x.mtcdtools.activities.managing.ManageNamedObjectsActivity;
import android.microntek.f1x.mtcdtools.activities.named.objects.ActionsListActivity;
import android.microntek.f1x.mtcdtools.activities.named.objects.ActionsSequenceActivity;
import android.microntek.f1x.mtcdtools.activities.named.objects.ModeListActivity;
import android.microntek.f1x.mtcdtools.service.MtcdService;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button createActionButton = (Button)this.findViewById(R.id.buttonCreateNewAction);
        createActionButton.setOnClickListener(new StartActivityButtonClickListener(this, CreateActionActivity.class));

        Button createBindingButton = (Button)this.findViewById(R.id.buttonCreateNewBinding);
        createBindingButton.setOnClickListener(new StartActivityButtonClickListener(this, BindingActivity.class));

        Button createActionsListButton = (Button)this.findViewById(R.id.buttonCreateNewActionsList);
        createActionsListButton.setOnClickListener(new StartActivityButtonClickListener(this, ActionsListActivity.class));

        Button createActionsSequenceButton = (Button)this.findViewById(R.id.buttonCreateNewActionsSequence);
        createActionsSequenceButton.setOnClickListener(new StartActivityButtonClickListener(this, ActionsSequenceActivity.class));

        Button createModeListButton = (Button)this.findViewById(R.id.buttonCreateNewModeList);
        createModeListButton.setOnClickListener(new StartActivityButtonClickListener(this, ModeListActivity.class));

        Button manageNamedObjectsButton = (Button)this.findViewById(R.id.buttonManageNamedObjects);
        manageNamedObjectsButton.setOnClickListener(new StartActivityButtonClickListener(this, ManageNamedObjectsActivity.class));

        Button manageBindingsButton = (Button)this.findViewById(R.id.buttonManageBindings);
        manageBindingsButton.setOnClickListener(new StartActivityButtonClickListener(this, ManageBindingsActivity.class));

        Button manageAutorunButton = (Button)this.findViewById(R.id.buttonManageAutorun);
        manageAutorunButton.setOnClickListener(new StartActivityButtonClickListener(this, ManageAutorunActivity.class));

        Button settingsButton = (Button)this.findViewById(R.id.buttonSettings);
        settingsButton.setOnClickListener(new StartActivityButtonClickListener(this, SettingsActivity.class));

        this.startService(new Intent(this, MtcdService.class));
    }

    public static final String APP_NAME = "MtcdTools";
}
