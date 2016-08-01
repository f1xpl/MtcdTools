package com.f1x.mtcdinput.keyinputs;

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
        byte[] inputBuffer = new byte[inputStream.available()];
        inputStream.read(inputBuffer);
        String inputString = new String(inputBuffer, KeyInputsStorage.CHARSET);

        return inputString;
    }

    private FileInputStream openFileInput() throws IOException {
        File inputFile = new File(mContext.getFilesDir(), KeyInputsStorage.STORAGE_FILE_NAME);
        if(!inputFile.exists()) {
            inputFile.createNewFile();
        }

        return mContext.openFileInput(inputFile.getName());
    }

    private final Context mContext;
}
