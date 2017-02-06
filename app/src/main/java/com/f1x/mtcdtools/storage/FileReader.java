package com.f1x.mtcdtools.storage;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by f1x on 2016-08-01.
 */
public class FileReader {
    public FileReader(Context context) {
        mContext = context;
    }

    public String read(String fileName, String charset) throws IOException  {
        FileInputStream inputStream = openFileInput(fileName);
        final int totalBytes = inputStream.available();
        byte[] inputBuffer = new byte[totalBytes];

        int bytesRead = inputStream.read(inputBuffer, 0, totalBytes);
        while(bytesRead < totalBytes) {
            bytesRead += inputStream.read(inputBuffer, bytesRead, totalBytes);
        }

        return new String(inputBuffer, charset);
    }

    private FileInputStream openFileInput(String fileName) throws IOException {
        File inputFile = new File(mContext.getFilesDir(), fileName);
        if(!inputFile.exists()) {
            if(!inputFile.createNewFile()) {
                throw new IOException("Could not create file: " + inputFile.getName());
            }
        }

        return mContext.openFileInput(inputFile.getName());
    }

    private final Context mContext;
}
