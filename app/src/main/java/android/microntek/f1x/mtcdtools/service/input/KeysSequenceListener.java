package android.microntek.f1x.mtcdtools.service.input;

import java.util.List;

/**
 * Created by f1x on 2017-01-09.
 */

public interface KeysSequenceListener {
    void handleKeysSequence(List<Integer> keysSequence);
    void handleSingleKey(int keyCode);
}
