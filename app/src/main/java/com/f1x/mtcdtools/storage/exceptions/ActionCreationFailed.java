package com.f1x.mtcdtools.storage.exceptions;

/**
 * Created by COMPUTER on 2017-01-22.
 */

public class ActionCreationFailed extends Throwable {
    public ActionCreationFailed(String actionName) {
        mActionName = actionName;
    }

    @Override
    public String getMessage() {
        return "Failed to create action: " + mActionName;
    }

    private final String mActionName;
}
