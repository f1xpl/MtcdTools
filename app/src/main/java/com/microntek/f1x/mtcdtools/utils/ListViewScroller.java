package com.microntek.f1x.mtcdtools.utils;

import android.widget.ListView;

/**
 * Created by f1x on 2017-02-06.
 */
public class ListViewScroller {
    public ListViewScroller(ListView listView) {
        mListView = listView;
        mListIndexer = new ListIndexer();
    }

    public void reset() {
        mListIndexer.reset(mListView.getAdapter().getCount());
        setListViewItem(0);
    }

    public void scrollUp() {
        int index = mListIndexer.up();
        setListViewItem(index);
    }

    public void scrollDown() {
        int index = mListIndexer.down();
        setListViewItem(index);
    }

    private void setListViewItem(int position) {
        mListView.clearChoices();
        mListView.requestLayout();

        mListView.setItemChecked(position, true);
        mListView.setSelection(position);
    }

    private final ListView mListView;
    private final ListIndexer mListIndexer;
}

