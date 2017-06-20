package android.microntek.f1x.mtcdtools.utils;

import android.microntek.f1x.mtcdtools.utils.ListIndexer;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by f1x on 2017-01-28.
 */

public class ListIndexerTest {
    @Test
    public void test_down() {
        ListIndexer listIndexer = new ListIndexer();
        listIndexer.reset(5);

        for(int i = 1; i < 50; ++i) {
            assertEquals(i % 5, listIndexer.down());
        }
    }

    @Test
    public void test_up() {
        ListIndexer listIndexer = new ListIndexer();
        listIndexer.reset(5);

        for(int i = 44; i >= 0; --i) {
            assertEquals(i % 5, listIndexer.up());
        }
    }

    @Test
    public void test_up_and_down() {
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
