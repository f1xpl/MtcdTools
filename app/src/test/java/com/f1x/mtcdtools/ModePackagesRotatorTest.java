package com.f1x.mtcdtools;

import com.f1x.mtcdtools.evaluation.ModePackagesRotator;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by COMPUTER on 2016-08-16.
 */
public class ModePackagesRotatorTest {
    public ModePackagesRotatorTest() {
        mModePackagesRotator = new ModePackagesRotator();
    }

    @Test
    public void rotateEmptyList() {
        assertEquals("", mModePackagesRotator.getNextPackage());
        assertEquals("", mModePackagesRotator.getNextPackage());
        assertEquals("", mModePackagesRotator.getNextPackage());
    }

    @Test
    public void rotateListWithOneElement() {
        List<String> packages = new ArrayList<>();
        packages.add("com.test.package");
        mModePackagesRotator.updatePackages(new ArrayList<>(packages));

        assertEquals(packages.get(0), mModePackagesRotator.getNextPackage());
        assertEquals(packages.get(0), mModePackagesRotator.getNextPackage());
        assertEquals(packages.get(0), mModePackagesRotator.getNextPackage());
    }

    @Test
    public void rotateListWithManyElements() {
        List<String> packages = new ArrayList<>();
        packages.add("com.test.package");
        packages.add("com.test.package2");
        packages.add("com.test.package3");
        mModePackagesRotator.updatePackages(new ArrayList<>(packages));

        for(int i = 0; i < 9; ++i) {
            assertEquals(packages.get(i % 3), mModePackagesRotator.getNextPackage());
        }
    }

    ModePackagesRotator mModePackagesRotator;
}
