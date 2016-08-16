package com.f1x.mtcdtools;

import com.f1x.mtcdtools.storage.FileWriterInterface;

import java.io.IOException;

/**
 * Created by COMPUTER on 2016-08-01.
 */
class MockFileWriter implements FileWriterInterface {
    public MockFileWriter() {
        mOutput = "";
    }

    @Override
    public void write(String output, String fileName, String charset) throws IOException {
        mOutput = output;
    }

    public String getOutput() {
        return mOutput;
    }

    private String mOutput;
}
