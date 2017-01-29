package com.f1x.mtcdtools.storage.exceptions;

/**
 * Created by COMPUTER on 2017-01-22.
 */

public class EntryCreationFailed extends Throwable {
    public EntryCreationFailed(String objectName) {
        mObjectName = objectName;
    }

    @Override
    public String getMessage() {
        return "Failed to create object, name: " + mObjectName;
    }

    private final String mObjectName;
}
