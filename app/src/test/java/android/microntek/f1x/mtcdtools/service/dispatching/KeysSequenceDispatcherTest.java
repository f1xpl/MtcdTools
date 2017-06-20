package android.microntek.f1x.mtcdtools.service.dispatching;

import android.content.Context;

import android.microntek.f1x.mtcdtools.service.input.KeysSequenceBinding;
import android.microntek.f1x.mtcdtools.named.NamedObjectId;
import android.microntek.f1x.mtcdtools.service.storage.KeysSequenceBindingsStorage;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by COMPUTER on 2017-05-10.
 */

public class KeysSequenceDispatcherTest {
    @Before
    public void init() throws JSONException, IOException {
        initMocks(this);
        PowerMockito.mockStatic(DispatchingIndicationPlayer.class);
    }

    @Test
    public void test_Dispatching_KeysSequence_With_Indication() {
        KeysSequenceDispatcher keysSequenceDispatcher = new KeysSequenceDispatcher(mMockContext, mKeysSequenceBindingsStorage, mNamedObjectDispatcher, mMockDispatchingIndicationPlayer);

        final List<Integer> keysSequence = Arrays.asList(1, 2, 3, 4);
        when(mKeysSequenceBindingsStorage.getItem(keysSequence)).thenReturn(mMockKeysSequenceBinding);
        when(mMockKeysSequenceBinding.playIndication()).thenReturn(true);

        final NamedObjectId targetId = new NamedObjectId("testTarget");
        when(mMockKeysSequenceBinding.getTargetId()).thenReturn(targetId);

        keysSequenceDispatcher.handleKeysSequence(keysSequence);
        verify(mNamedObjectDispatcher, times(1)).dispatch(targetId, mMockContext);
        verify(mMockDispatchingIndicationPlayer, times(1)).play();
    }

    @Test
    public void test_Dispatching_KeysSequence_Without_Indication() {
        KeysSequenceDispatcher keysSequenceDispatcher = new KeysSequenceDispatcher(mMockContext, mKeysSequenceBindingsStorage, mNamedObjectDispatcher, mMockDispatchingIndicationPlayer);

        final List<Integer> keysSequence = Arrays.asList(1, 2, 3, 4);
        when(mKeysSequenceBindingsStorage.getItem(keysSequence)).thenReturn(mMockKeysSequenceBinding);
        when(mMockKeysSequenceBinding.playIndication()).thenReturn(false);

        final NamedObjectId targetId = new NamedObjectId("testTarget");
        when(mMockKeysSequenceBinding.getTargetId()).thenReturn(targetId);

        keysSequenceDispatcher.handleKeysSequence(keysSequence);
        verify(mNamedObjectDispatcher, times(1)).dispatch(targetId, mMockContext);
        verify(mMockDispatchingIndicationPlayer, times(0)).play();
    }

    @Mock
    Context mMockContext;

    @Mock
    KeysSequenceBindingsStorage mKeysSequenceBindingsStorage;

    @Mock
    NamedObjectDispatcher mNamedObjectDispatcher;

    @Mock
    KeysSequenceBinding mMockKeysSequenceBinding;

    @Mock
    DispatchingIndicationPlayer mMockDispatchingIndicationPlayer;
}
