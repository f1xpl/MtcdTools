package com.microntek.f1x.mtcdtools.service.storage;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by f1x on 2016-08-01.
 */
public class FileWriter {
    public FileWriter(Context context) {
        mContext = context;
    }

    public void write(String output, String fileName, String charset) throws IOException {
        FileOutputStream outputStream = openFileOutput(fileName);
        String outputString = new String(output.getBytes(), charset);
        outputStream.write(outputString.getBytes());
    }

    private FileOutputStream openFileOutput(String fileName) throws IOException {
        File outputFile = new File(mContext.getFilesDir(), fileName);

        if(!outputFile.exists()) {
            if(!outputFile.createNewFile()) {
                throw new IOException("Could not create file: " + outputFile.getName());
            }
        }

        return mContext.openFileOutput(outputFile.getName(), Context.MODE_PRIVATE);
    }

    private final Context mContext;
}
