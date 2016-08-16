package com.f1x.mtcdtools.evaluation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by COMPUTER on 2016-08-16.
 */
public class ModePackagesRotator {
    public ModePackagesRotator() {
        mPackages = new ArrayList<>();
        mIndex = 0;
    }

    public void updatePackages(List<String> packages) {
        mPackages = packages;
    }

    public String getNextPackage() {
        String packageName = "";

        if(!mPackages.isEmpty()) {
            if(mIndex >= mPackages.size()) {
                mIndex = 0;
            }

            packageName = mPackages.get(mIndex++);
        }

        return packageName;
    }

    List<String> mPackages;
    int mIndex;
}
