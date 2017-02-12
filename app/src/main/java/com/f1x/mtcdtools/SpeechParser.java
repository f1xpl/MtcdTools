package com.f1x.mtcdtools;

import com.f1x.mtcdtools.named.objects.NamedObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by f1x on 2017-02-06.
 */

public class SpeechParser {
    public List<NamedObjectId> parse(List<String> texts, String separator) {
        List<NamedObjectId> extractedObjectsNames = new ArrayList<>();

        for(String text : texts) {
            if(!separator.isEmpty()) {
                parseText(extractedObjectsNames, text, separator);
            } else if(!contains(extractedObjectsNames, text)) {
                extractedObjectsNames.add(new NamedObjectId(text));
            }
        }

        return new ArrayList<>(extractedObjectsNames);
    }

    private void parseText(List<NamedObjectId> output, String text, String separator) {
        String[] parts = text.split(separator);

        for(String part : parts) {
            String trimmedPart = part.trim();

            if(!contains(output, trimmedPart)) {
                output.add(new NamedObjectId(trimmedPart));
            }
        }
    }

    private boolean contains(List<NamedObjectId> list, String item) {
        for(NamedObjectId value : list) {
            if(value.equals(new NamedObjectId(item))) {
                return true;
            }
        }

        return false;
    }
}
