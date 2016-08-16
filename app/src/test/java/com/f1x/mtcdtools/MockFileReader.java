package com.f1x.mtcdtools;

import com.f1x.mtcdtools.storage.FileReaderInterface;

import java.io.IOException;

/**
 * Created by COMPUTER on 2016-08-01.
 */
class MockFileReader implements FileReaderInterface {
    MockFileReader() {
        mInput = "";
    }

    @Override
    public String read(String fileName, String charset) throws IOException {
        return mInput;
    }

    public void setInput(String input) {
        mInput = input;
    }

    private String mInput;
}
