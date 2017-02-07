package com.f1x.mtcdtools.named.objects;

import com.f1x.mtcdtools.named.objects.actions.Action;
import com.f1x.mtcdtools.named.objects.actions.LaunchAction;
import com.f1x.mtcdtools.storage.NamedObjectsStorage;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by COMPUTER on 2017-02-07.
 */

public class ActionsExtractorTest {
    @Before
    public void init() {
        initMocks(this);

        mActions = Arrays.asList(new LaunchAction("action1", "com.test.package"),
                new LaunchAction("action2", "com.test.package"),
                new LaunchAction("action3", "com.test.package"),
                new LaunchAction("action4", "com.test.package"));

        when(mMockNamedObjectsStorage.getItem(mActions.get(0).getName())).thenReturn(mActions.get(0));
        when(mMockNamedObjectsStorage.getItem(mActions.get(1).getName())).thenReturn(mActions.get(1));
        when(mMockNamedObjectsStorage.getItem(mActions.get(2).getName())).thenReturn(mActions.get(2));
        when(mMockNamedObjectsStorage.getItem(mActions.get(3).getName())).thenReturn(mActions.get(3));
    }

    @Test
    public void test_extractActionsOnly() {
        List<Action> extractedActions = ActionsExtractor.extract(Arrays.asList(mActions.get(0).getName(),
                mActions.get(2).getName(), mActions.get(1).getName(), mActions.get(3).getName()), mMockNamedObjectsStorage);

        assertEquals(4, extractedActions.size());
        assertEquals(mActions.get(0).getName(), extractedActions.get(0).getName());
        assertEquals(mActions.get(2).getName(), extractedActions.get(1).getName());
        assertEquals(mActions.get(1).getName(), extractedActions.get(2).getName());
        assertEquals(mActions.get(3).getName(), extractedActions.get(3).getName());
    }

    @Test
    public void test_extractSequences() {
        ActionsSequence actionsSequence1 = new ActionsSequence("sequence1", Arrays.asList(mActions.get(0).getName(), mActions.get(3).getName()));
        when(mMockNamedObjectsStorage.getItem(actionsSequence1.getName())).thenReturn(actionsSequence1);

        ActionsSequence actionsSequence2 = new ActionsSequence("sequence2", Arrays.asList(mActions.get(2).getName(), mActions.get(1).getName()));
        when(mMockNamedObjectsStorage.getItem(actionsSequence2.getName())).thenReturn(actionsSequence2);

        List<Action> extractedActions = ActionsExtractor.extract(Arrays.asList(mActions.get(2).getName(),
                actionsSequence1.getName(), actionsSequence2.getName(), mActions.get(0).getName()), mMockNamedObjectsStorage);

        assertEquals(6, extractedActions.size());

        assertEquals(mActions.get(2).getName(), extractedActions.get(0).getName());
        assertEquals(mActions.get(0).getName(), extractedActions.get(1).getName());
        assertEquals(mActions.get(3).getName(), extractedActions.get(2).getName());
        assertEquals(mActions.get(2).getName(), extractedActions.get(3).getName());
        assertEquals(mActions.get(1).getName(), extractedActions.get(4).getName());
        assertEquals(mActions.get(0).getName(), extractedActions.get(5).getName());
    }

//    @Test
//    public void test_extractSequences_Cyclic() {
//        ActionsSequence actionsSequence1 = new ActionsSequence("sequence1", Arrays.asList(mActions.get(0).getName(), mActions.get(3).getName(), "sequence2"));
//        when(mMockNamedObjectsStorage.getItem(actionsSequence1.getName())).thenReturn(actionsSequence1);
//
//        ActionsSequence actionsSequence2 = new ActionsSequence("sequence2", Arrays.asList(mActions.get(2).getName(), mActions.get(1).getName(), "sequence1"));
//        when(mMockNamedObjectsStorage.getItem(actionsSequence2.getName())).thenReturn(actionsSequence2);
//
//        List<Action> extractedActions = ActionsExtractor.extract(Arrays.asList(actionsSequence1.getName(), actionsSequence2.getName()), mMockNamedObjectsStorage);
//
//        assertEquals(6, extractedActions.size());
//
//        assertEquals(mActions.get(0).getName(), extractedActions.get(1).getName());
//        assertEquals(mActions.get(3).getName(), extractedActions.get(2).getName());
//        assertEquals(mActions.get(2).getName(), extractedActions.get(3).getName());
//        assertEquals(mActions.get(1).getName(), extractedActions.get(4).getName());
//
//        assertEquals(mActions.get(2).getName(), extractedActions.get(3).getName());
//        assertEquals(mActions.get(1).getName(), extractedActions.get(4).getName());
//        assertEquals(mActions.get(0).getName(), extractedActions.get(1).getName());
//        assertEquals(mActions.get(3).getName(), extractedActions.get(2).getName());
//    }

    List<LaunchAction> mActions;

    @Mock
    NamedObjectsStorage mMockNamedObjectsStorage;
}
