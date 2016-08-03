package com.f1x.mtcdtools.keys.storage;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by COMPUTER on 2016-08-01.
 */
public class KeyInputsFileReader implements KeyInputsReaderInterface {
    public KeyInputsFileReader(Context context) {
        mContext = context;
    }

    @Override
    public String read() throws IOException  {
        FileInputStream inputStream = openFileInput();
        final int totalBytes = inputStream.available();
        byte[] inputBuffer = new byte[totalBytes];

        int bytesRead = inputStream.read(inputBuffer, 0, totalBytes);
        while(bytesRead < totalBytes) {
            bytesRead += inputStream.read(inputBuffer, bytesRead, totalBytes);
        }

        return new String(inputBuffer, KeyInputsStorage.CHARSET);
    }

    private FileInputStream openFileInput() throws IOException {
        File inputFile = new File(mContext.getFilesDir(), KeyInputsStorage.STORAGE_FILE_NAME);
        if(!inputFile.exists()) {
            if(!inputFile.createNewFile()) {
                throw new IOException("Could not create file: " + inputFile.getName());
            }
        }

        return mContext.openFileInput(inputFile.getName());
    }

    private final Context mContext;
}
