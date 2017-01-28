package com.f1x.mtcdtools;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by COMPUTER on 2017-01-28.
 */

public class ListIndexerTest {
    @Test
    public void test_Down() {
        ListIndexer listIndexer = new ListIndexer();

        listIndexer.reset(5);
        assertEquals(1, listIndexer.down());
        assertEquals(2, listIndexer.down());
        assertEquals(3, listIndexer.down());
        assertEquals(4, listIndexer.down());

        assertEquals(0, listIndexer.down());
        assertEquals(1, listIndexer.down());
        assertEquals(2, listIndexer.down());
        assertEquals(3, listIndexer.down());
        assertEquals(4, listIndexer.down());
    }

    @Test
    public void test_Up() {
        ListIndexer listIndexer = new ListIndexer();

        listIndexer.reset(5);
        assertEquals(4, listIndexer.up());
        assertEquals(3, listIndexer.up());
        assertEquals(2, listIndexer.up());
        assertEquals(1, listIndexer.up());
        assertEquals(0, listIndexer.up());

        assertEquals(4, listIndexer.up());
        assertEquals(3, listIndexer.up());
        assertEquals(2, listIndexer.up());
        assertEquals(1, listIndexer.up());
        assertEquals(0, listIndexer.up());
    }

    @Test
    public void test_UpAndDown() {
        ListIndexer listIndexer = new ListIndexer();

        listIndexer.reset(5);
        assertEquals(4, listIndexer.up());
        assertEquals(0, listIndexer.down());
        assertEquals(1, listIndexer.down());
        assertEquals(2, listIndexer.down());
        assertEquals(1, listIndexer.up());

        assertEquals(0, listIndexer.up());
        assertEquals(4, listIndexer.up());
    }
}
