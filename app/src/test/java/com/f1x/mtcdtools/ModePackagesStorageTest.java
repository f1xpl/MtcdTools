package com.f1x.mtcdtools;

import com.f1x.mtcdtools.storage.ModePackagesStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by COMPUTER on 2016-08-16.
 */
public class ModePackagesStorageTest {
    public ModePackagesStorageTest() throws JSONException {
        mMockFileReader = new MockFileReader();
        mMockFileWriter = new MockFileWriter();
        mModePackagesStorage = new ModePackagesStorage(mMockFileReader, mMockFileWriter);

        mSampleModePackages =  new ArrayList<>();
        mSampleModePackages.add("com.test.package1");
        mSampleModePackages.add("com.test.package2");
        mSampleModePackages.add("com.test.package3");
        mSampleModePackages.add("com.test.package4");
    }

    @Test
    public void testEmptyFileInput() throws IOException, JSONException {
        mMockFileReader.setInput("");
        mModePackagesStorage.read();
        assertTrue(mModePackagesStorage.getPackages().isEmpty());
    }

    @Test
    public void testEmptyList() throws IOException, JSONException {
        mSampleModePackages.clear();
        JSONObject testJsonObject = createTestJsonObject(mSampleModePackages);

        mMockFileReader.setInput(testJsonObject.toString());
        mModePackagesStorage.read();
        assertTrue(mModePackagesStorage.getPackages().isEmpty());
    }

    @Test
    public void parseJsonString() throws JSONException, IOException {
        JSONObject testJsonObject = createTestJsonObject(mSampleModePackages);

        mMockFileReader.setInput(testJsonObject.toString());
        mModePackagesStorage.read();
        assertEquals(mSampleModePackages, mModePackagesStorage.getPackages());
    }

    @Test
    public void testSetNewPackages() throws IOException, JSONException {
        mSampleModePackages.remove(0);
        mSampleModePackages.remove(2);
        mModePackagesStorage.setPackages(new ArrayList<>(mSampleModePackages));

        JSONObject testJsonObject = createTestJsonObject(mSampleModePackages);
        assertEquals(testJsonObject.toString(), mMockFileWriter.getOutput());
    }

    private static JSONObject createTestJsonObject(List<String> sampleModePackages) throws JSONException {
        JSONArray inputsArray = new JSONArray();
        for (String packageName : sampleModePackages) {
            inputsArray.put(packageName);
        }

        JSONObject testJson = new JSONObject();
        testJson.put("packages", inputsArray);

        return testJson;
    }

    private final MockFileReader mMockFileReader;
    private final MockFileWriter mMockFileWriter;
    private final ModePackagesStorage mModePackagesStorage;
    private final List<String> mSampleModePackages;
}
