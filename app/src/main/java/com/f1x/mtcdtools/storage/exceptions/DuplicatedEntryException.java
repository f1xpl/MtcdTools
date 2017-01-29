package com.f1x.mtcdtools.storage.exceptions;

/**
 * Created by COMPUTER on 2017-01-22.
 */

public class DuplicatedEntryException extends Throwable {
    public DuplicatedEntryException(String entryName) {
        mEntryName = entryName;
    }

    @Override
    public String getMessage() {
        return "Entry: " + mEntryName + " is duplicated.";
    }

    private final String mEntryName;
}
