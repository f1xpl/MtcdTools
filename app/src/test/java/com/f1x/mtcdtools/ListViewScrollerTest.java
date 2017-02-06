package com.f1x.mtcdtools;

import android.widget.ListAdapter;
import android.widget.ListView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by COMPUTER on 2017-02-06.
 */

public class ListViewScrollerTest {
    @Before
    public void init() {
        initMocks(this);
    }

    @Test
    public void test_scroll() {
        when(mMockListView.getAdapter()).thenReturn(mMockAdapter);

        int elementsCount = 10;
        when(mMockAdapter.getCount()).thenReturn(elementsCount);

        ListViewScroller listViewScroller = new ListViewScroller(mMockListView);

        listViewScroller.reset();
        verify(mMockListView, times(1)).setSelection(0);
        verify(mMockListView, times(1)).clearChoices();
        verify(mMockListView, times(1)).requestLayout();
        verify(mMockListView, times(1)).setItemChecked(0, true);

        for(int i = 1; i < elementsCount; ++i) {
            reset(mMockListView);

            listViewScroller.scrollDown();
            verify(mMockListView, times(1)).setSelection(i);
            verify(mMockListView, times(1)).setItemChecked(i, true);
            verify(mMockListView, times(1)).clearChoices();
            verify(mMockListView, times(1)).requestLayout();
        }

        for(int i = elementsCount - 2; i < 0; ++i) {
            reset(mMockListView);

            listViewScroller.scrollUp();
            verify(mMockListView, times(1)).setSelection(i);
            verify(mMockListView, times(1)).setItemChecked(i, true);
            verify(mMockListView, times(1)).clearChoices();
            verify(mMockListView, times(1)).requestLayout();
        }
    }

    @Mock
    ListView mMockListView;

    @Mock
    ListAdapter mMockAdapter;
}
