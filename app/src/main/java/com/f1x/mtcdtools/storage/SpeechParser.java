package com.f1x.mtcdtools.storage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by COMPUTER on 2017-02-06.
 */

public class SpeechParser {
    public List<String> parse(List<String> texts, String separator) {
        List<String> extractedObjectsNames = new ArrayList<>();

        for(String text : texts) {
            if(!separator.isEmpty()) {
                extractedObjectsNames.addAll(parseText(text, separator));
            } else {
                extractedObjectsNames.add(text);
            }
        }

        return extractedObjectsNames;
    }

    private List<String> parseText(String text, String separator) {
        List<String> partsList = new ArrayList<>();
        String[] parts = text.split(separator);

        for(String part : parts) {
            partsList.add(part.trim());
        }

        return partsList;
    }
}
