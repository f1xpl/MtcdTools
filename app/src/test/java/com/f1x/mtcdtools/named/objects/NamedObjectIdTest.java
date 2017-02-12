package com.f1x.mtcdtools.named.objects;

import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Created by f1x on 2017-02-11.
 */

public class NamedObjectIdTest {

    @Test
    public void test_equals() {
        NamedObjectId id1 = new NamedObjectId("testName");
        NamedObjectId id2 = new NamedObjectId("TeStNaMe");
        NamedObjectId id3 = new NamedObjectId("another");

        assertTrue(id1.equals(id2));
        assertFalse(id1.equals(id3));
        assertFalse(id2.equals(id3));
    }
}
