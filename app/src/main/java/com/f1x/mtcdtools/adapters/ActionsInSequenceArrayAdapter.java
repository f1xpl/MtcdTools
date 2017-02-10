package com.f1x.mtcdtools.adapters;

import android.content.Context;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.TextView;

import com.f1x.mtcdtools.R;
import com.f1x.mtcdtools.adapters.entries.ActionInSequenceEntry;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by COMPUTER on 2017-02-10.
 */

public class ActionsInSequenceArrayAdapter extends ArrayAdapter<ActionInSequenceEntry> {
    public ActionsInSequenceArrayAdapter(Context context) {
        super(context, R.layout.layout_action_in_sequence_row, R.id.textViewActionName);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View entryView = super.getView(position, convertView, parent);

        TextView actionNameTextView = (TextView)entryView.findViewById(R.id.textViewActionName);
        final TextView actionDelayValueTextView = (TextView)entryView.findViewById(R.id.textViewActionDelayValue);

        final ActionInSequenceEntry actionInSequenceEntry = getItem(position);
        actionDelayValueTextView.setText(String.format(Locale.getDefault(), "%d [ms]", actionInSequenceEntry.getDelay()));
        SeekBar actionDelaySeekBar = (SeekBar)entryView.findViewById(R.id.seekBarActionDelay);

        actionDelaySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                actionDelayValueTextView.setText(String.format(Locale.getDefault(), "%d [ms]", progress));
                actionInSequenceEntry.setDelay(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        ActionInSequenceEntry entry = getItem(position);

        if(entry != null) {
            actionNameTextView.setText(entry.getActionName());
            actionDelaySeekBar.setProgress(entry.getDelay());
        }

        return entryView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    public void reset(List<String> actionNames, Map<String, Integer> actionDelays) {
        clear();

        for(String actionName : actionNames) {
            int delay = actionDelays.containsKey(actionName) ? actionDelays.get(actionName) : 0;
            add(new ActionInSequenceEntry(actionName, delay));
        }
    }
}
