package com.f1x.mtcdtools.storage;

import com.f1x.mtcdtools.input.KeysSequenceBinding;
import com.f1x.mtcdtools.storage.exceptions.DuplicatedEntryException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by COMPUTER on 2017-01-17.
 */

@PowerMockIgnore({ "org.mockito.*", "org.robolectric.*", "android.*" })
@PrepareForTest({KeysSequenceBindingsStorage.class})
@RunWith(PowerMockRunner.class)
public class KeysSequenceBindingsStorageTest {
    @Before
    public void init() throws JSONException, IOException {
        initMocks(this);

        mKeysSequenceBindings = new ArrayList<>();
        mKeysSequenceBindings.add(new KeysSequenceBinding(Arrays.asList(1, 2, 5), KeysSequenceBinding.TARGET_TYPE_ACTIONS_SEQUENCE, "binding1"));
        mKeysSequenceBindings.add(new KeysSequenceBinding(Arrays.asList(5, 6, 7), KeysSequenceBinding.TARGET_TYPE_ACTION, "binding2"));

        mKeysSequenceBindingsArray = new JSONArray();
        mKeysSequenceBindingsArray.put(mKeysSequenceBindings.get(0).toJson());
        mKeysSequenceBindingsArray.put(mKeysSequenceBindings.get(1).toJson());

        mKeysSequenceBindingsJson = new JSONObject();
        mKeysSequenceBindingsJson.put(KeysSequenceBindingsStorage.ROOT_ARRAY_NAME, mKeysSequenceBindingsArray);

        when(mMockFileReader.read(KeysSequenceBindingsStorage.STORAGE_FILE_NAME, "UTF-8")).thenReturn(mKeysSequenceBindingsJson.toString());
    }

    @Test
    public void test_Read() throws JSONException, IOException, DuplicatedEntryException {
        KeysSequenceBindingsStorage keysSequenceBindingsStorage = new KeysSequenceBindingsStorage(mMockFileReader, mMockFileWriter);
        keysSequenceBindingsStorage.read();

        Map<List<Integer>, KeysSequenceBinding> keysSequenceBindings = keysSequenceBindingsStorage.getKeysSequenceBindings();
        assertEquals(mKeysSequenceBindings.size(), keysSequenceBindings.size());

        for(int i = 0; i < mKeysSequenceBindings.size(); ++i) {
            KeysSequenceBinding expectedKeysSequenceBinding = mKeysSequenceBindings.get(i);
            KeysSequenceBinding actualKeysSequenceBinding = keysSequenceBindings.get(expectedKeysSequenceBinding.getKeysSequence());
            assertEquals(actualKeysSequenceBinding.toJson().toString(), expectedKeysSequenceBinding.toJson().toString());
        }
    }

    @Test
    public void test_Insert_Duplicated() throws JSONException, IOException, DuplicatedEntryException {
        KeysSequenceBindingsStorage keysSequenceBindingsStorage = new KeysSequenceBindingsStorage(mMockFileReader, mMockFileWriter);
        keysSequenceBindingsStorage.read();

        Map<List<Integer>, KeysSequenceBinding> keysSequenceBindings = keysSequenceBindingsStorage.getKeysSequenceBindings();
        assertEquals(mKeysSequenceBindings.size(), keysSequenceBindings.size());

        boolean exceptionCaught = false;
        try {
            keysSequenceBindingsStorage.insert(new KeysSequenceBinding(Arrays.asList(1, 2, 5), KeysSequenceBinding.TARGET_TYPE_ACTIONS_SEQUENCE, "binding1"));
        } catch(DuplicatedEntryException e) {
            exceptionCaught = true;
        }

        assertTrue(exceptionCaught);
    }

    @Test
    public void test_Insert() throws JSONException, IOException, DuplicatedEntryException {
        KeysSequenceBinding keysSequenceBinding = new KeysSequenceBinding(Arrays.asList(5, 4, 7), KeysSequenceBinding.TARGET_TYPE_ACTION, "action5");
        mKeysSequenceBindings.add(keysSequenceBinding);

        KeysSequenceBindingsStorage keysSequenceBindingsStorage = new KeysSequenceBindingsStorage(mMockFileReader, mMockFileWriter);
        keysSequenceBindingsStorage.read();
        keysSequenceBindingsStorage.insert(keysSequenceBinding);

        mKeysSequenceBindingsArray.put(keysSequenceBinding.toJson());

        JSONObject keysSequenceBindingsJson = new JSONObject();
        keysSequenceBindingsJson.put(KeysSequenceBindingsStorage.ROOT_ARRAY_NAME, mKeysSequenceBindingsArray);

        Map<List<Integer>, KeysSequenceBinding> keysSequenceBindings = keysSequenceBindingsStorage.getKeysSequenceBindings();
        assertEquals(mKeysSequenceBindings.size(), keysSequenceBindings.size());

        for(int i = 0; i < mKeysSequenceBindings.size(); ++i) {
            KeysSequenceBinding expectedKeysSequenceBinding = mKeysSequenceBindings.get(i);
            KeysSequenceBinding actualKeysSequenceBinding = keysSequenceBindings.get(expectedKeysSequenceBinding.getKeysSequence());
            assertEquals(actualKeysSequenceBinding.toJson().toString(), expectedKeysSequenceBinding.toJson().toString());
        }

        verify(mMockFileWriter, times(1)).write(keysSequenceBindingsJson.toString(), KeysSequenceBindingsStorage.STORAGE_FILE_NAME, "UTF-8");
    }

    @Test
    public void test_Remove() throws JSONException, IOException, DuplicatedEntryException {
        KeysSequenceBindingsStorage keysSequenceBindingsStorage = new KeysSequenceBindingsStorage(mMockFileReader, mMockFileWriter);
        keysSequenceBindingsStorage.read();

        keysSequenceBindingsStorage.remove(mKeysSequenceBindings.get(0));

        Map<List<Integer>, KeysSequenceBinding> keysSequenceBindings = keysSequenceBindingsStorage.getKeysSequenceBindings();
        assertEquals(1, keysSequenceBindings.size());

        KeysSequenceBinding expectedKeysSequenceBinding = mKeysSequenceBindings.get(1);
        KeysSequenceBinding actualKeysSequenceBinding = keysSequenceBindings.get(expectedKeysSequenceBinding.getKeysSequence());
        assertEquals(expectedKeysSequenceBinding.toJson().toString(), actualKeysSequenceBinding.toJson().toString());

        mKeysSequenceBindingsArray.remove(0);
        JSONObject keysSequenceBindingsJson = new JSONObject();
        keysSequenceBindingsJson.put(KeysSequenceBindingsStorage.ROOT_ARRAY_NAME, mKeysSequenceBindingsArray);
        verify(mMockFileWriter, times(1)).write(keysSequenceBindingsJson.toString(), KeysSequenceBindingsStorage.STORAGE_FILE_NAME, "UTF-8");
    }

    @Test
    public void test_Remove_NonExistent() throws JSONException, IOException, DuplicatedEntryException {
        KeysSequenceBindingsStorage keysSequenceBindingsStorage = new KeysSequenceBindingsStorage(mMockFileReader, mMockFileWriter);
        keysSequenceBindingsStorage.remove(mKeysSequenceBindings.get(0));
        keysSequenceBindingsStorage.remove(mKeysSequenceBindings.get(1));

        Mockito.verify(mMockFileWriter, never()).write(any(String.class), any(String.class), any(String.class));
    }

    @Mock
    FileReader mMockFileReader;

    @Mock
    FileWriter mMockFileWriter;

    List<KeysSequenceBinding> mKeysSequenceBindings;
    JSONObject mKeysSequenceBindingsJson;
    JSONArray mKeysSequenceBindingsArray;
}
