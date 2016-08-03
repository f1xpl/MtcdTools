package com.f1x.mtcdtools.keys.storage;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by COMPUTER on 2016-08-01.
 */
public class KeyInputsFileWriter implements KeyInputsWriterInterface {
    public KeyInputsFileWriter(Context context) {
        mContext = context;
    }

    @Override
    public void write(String output) throws IOException {
        FileOutputStream outputStream = openFileOutput();
        String outputString = new String(output.getBytes(), KeyInputsStorage.CHARSET);
        outputStream.write(outputString.getBytes());
    }

    private FileOutputStream openFileOutput() throws IOException {
        File outputFile = new File(mContext.getFilesDir(), KeyInputsStorage.STORAGE_FILE_NAME);

        if(!outputFile.exists()) {
            if(!outputFile.createNewFile()) {
                throw new IOException("Could not create file: " + outputFile.getName());
            }
        }

        return mContext.openFileOutput(outputFile.getName(), Context.MODE_PRIVATE);
    }

    private final Context mContext;
}
