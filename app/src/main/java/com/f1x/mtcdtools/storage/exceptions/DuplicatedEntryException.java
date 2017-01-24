package com.f1x.mtcdtools.storage.exceptions;

/**
 * Created by COMPUTER on 2017-01-22.
 */

public class DuplicatedEntryException extends Throwable {
    public DuplicatedEntryException(String entryDetails) {
        mEntryDetails = entryDetails;
    }

    String getEntryDetails() {
        return mEntryDetails;
    }

    @Override
    public String getMessage() {
        return "Entry: " + mEntryDetails + " is duplicated.";
    }

    private final String mEntryDetails;
}
