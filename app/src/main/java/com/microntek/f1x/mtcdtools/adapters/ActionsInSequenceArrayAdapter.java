package com.microntek.f1x.mtcdtools.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.TextView;

import com.microntek.f1x.mtcdtools.R;
import com.microntek.f1x.mtcdtools.adapters.entries.ActionInSequenceEntry;
import com.microntek.f1x.mtcdtools.named.NamedObjectId;

import java.util.ArrayList;
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
            actionNameTextView.setText(entry.getActionId().toString());
            actionDelaySeekBar.setProgress(entry.getDelay());
        }

        return entryView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    public void reset(List<Map.Entry<NamedObjectId, Integer>> items) {
        clear();

        for(Map.Entry<NamedObjectId, Integer> entry : items) {
            add(new ActionInSequenceEntry(entry.getKey(), entry.getValue()));
        }
    }

    public void removeAt(int position) {
        if(position != -1 && position < getCount()) {
            List<ActionInSequenceEntry> items = new ArrayList<>();

            for(int i = 0; i < getCount(); ++i) {
                items.add(getItem(i));
            }

            items.remove(position);
            clear();
            addAll(items);
        }
    }
}
