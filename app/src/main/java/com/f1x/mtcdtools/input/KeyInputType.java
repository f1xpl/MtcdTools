package com.f1x.mtcdtools.input;

/**
 * Created by COMPUTER on 2016-08-01.
 */
public enum KeyInputType {
    NONE,
    LAUNCH,
    NEXT,
    PREVIOUS,
    TOGGLE_PLAY,
    MODE;

    public static String toString(KeyInputType inputType) {
        switch(inputType) {
            case LAUNCH:
                return LAUNCH_STRING;
            case NEXT:
                return NEXT_STRING;
            case PREVIOUS:
                return PREVIOUS_STRING;
            case TOGGLE_PLAY:
                return TOGGLE_PLAY_STRING;
            case MODE:
                return MODE_STRING;
            default:
                return NONE_STRING;
        }
    }

    public static KeyInputType fromString(String inputType) {
        switch(inputType) {
            case LAUNCH_STRING:
                return LAUNCH;
            case NEXT_STRING:
                return NEXT;
            case PREVIOUS_STRING:
                return PREVIOUS;
            case TOGGLE_PLAY_STRING:
                return TOGGLE_PLAY;
            case MODE_STRING:
                return MODE;
            default:
                return NONE;
        }
    }

    private static final String NONE_STRING = "NONE";
    private static final String LAUNCH_STRING = "Launch";
    private static final String NEXT_STRING = "Next";
    private static final String PREVIOUS_STRING = "Previous";
    private static final String TOGGLE_PLAY_STRING = "Toggle play";
    private static final String MODE_STRING = "Mode";
}
