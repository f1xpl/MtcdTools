package com.f1x.mtcdinput;

import com.f1x.mtcdinput.keyinputs.KeyInputsWriterInterface;

import java.io.IOException;

/**
 * Created by COMPUTER on 2016-08-01.
 */
class MockKeyInputsWriter implements KeyInputsWriterInterface {
    public MockKeyInputsWriter() {
        mOutput = "";
    }

    @Override
    public void write(String output) throws IOException {
        mOutput = output;
    }

    public String getOutput() {
        return new String(mOutput);
    }

    private String mOutput;
}
