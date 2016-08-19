package com.f1x.mtcdtools;

/**
 * Created by COMPUTER on 2016-08-03.
 */
public class Messaging {
    public class MessageIds {
        public static final int GET_KEY_INPUTS_REQUEST = 0x1000;
        public static final int GET_KEY_INPUTS_RESPONSE = 0x1001;

        public static final int SUSPEND_KEY_INPUT_DISPATCHING = 0x1002;
        public static final int RESUME_KEY_INPUT_DISPATCHING = 0x1003;

        public static final int EDIT_KEY_INPUTS_REQUEST = 0x1004;
        public static final int EDIT_KEY_INPUTS_RESPONSE = 0x1005;

        public static final int GET_MODE_PACKAGES_REQUEST = 0x1006;
        public static final int GET_MODE_PACKAGES_RESPONSE = 0x1007;

        public static final int SAVE_MODE_PACKAGES_REQUEST = 0x1008;
        public static final int SAVE_MODE_PACKAGES_RESPONSE = 0x1009;
    }

    public class KeyInputsEditType {
        public static final int ADD = 0;
        public static final int REMOVE = 1;
    }

    public class KeyInputsEditResult {
        public static final int FAILURE = 0;
        public static final int SUCCEED = 1;
    }

    public class ModePackagesSaveResult {
        public static final int FAILURE = 0;
        public static final int SUCCEED = 1;
    }
}
