package com.f1x.mtcdtools.storage;

import java.io.IOException;

/**
 * Created by COMPUTER on 2016-08-01.
 */
public interface FileWriterInterface {
    void write(String output, String fileName, String charset) throws IOException;
}
