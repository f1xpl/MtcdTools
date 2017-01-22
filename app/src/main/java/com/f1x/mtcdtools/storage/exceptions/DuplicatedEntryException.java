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

    private final String mEntryDetails;
}
