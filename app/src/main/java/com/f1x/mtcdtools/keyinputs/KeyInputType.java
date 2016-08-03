package com.f1x.mtcdtools.keyinputs;

/**
 * Created by COMPUTER on 2016-08-01.
 */
public enum KeyInputType {
    NONE,
    LAUNCH,
    NEXT,
    PREVIOUS,
    TOGGLE_PLAY;

    public static final String toString(KeyInputType inputType) {
        if(inputType.equals(LAUNCH)) {
            return LAUNCH_STRING;
        } else if(inputType.equals(NEXT)) {
            return NEXT_STRING;
        } else if(inputType.equals(PREVIOUS)) {
            return PREVIOUS_STRING;
        } else if(inputType.equals(TOGGLE_PLAY)) {
            return TOGGLE_PLAY_STRING;
        } else {
            return NONE_STRING;
        }
    }

    public static final KeyInputType fromString(String inputType) {
        if(inputType.equals(LAUNCH_STRING)) {
            return LAUNCH;
        } else if(inputType.equals(NEXT_STRING)) {
            return NEXT;
        } else if(inputType.equals(PREVIOUS_STRING)) {
            return PREVIOUS;
        } else if(inputType.equals(TOGGLE_PLAY_STRING)) {
            return TOGGLE_PLAY;
        } else {
            return NONE;
        }
    }

    private static final String NONE_STRING = "NONE";
    private static final String LAUNCH_STRING = "Launch";
    private static final String NEXT_STRING = "Next";
    private static final String PREVIOUS_STRING = "Previous";
    private static final String TOGGLE_PLAY_STRING = "Toggle play";
}
