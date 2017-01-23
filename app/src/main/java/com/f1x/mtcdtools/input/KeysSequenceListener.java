package com.f1x.mtcdtools.input;

import java.util.List;

/**
 * Created by COMPUTER on 2017-01-09.
 */

public interface KeysSequenceListener {
    void handleKeysSequence(List<Integer> keysSequence);
    void handleSingleKey(int keyCode);
}
