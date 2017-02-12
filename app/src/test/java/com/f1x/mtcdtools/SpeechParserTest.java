package com.f1x.mtcdtools;

import com.f1x.mtcdtools.named.objects.NamedObjectId;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by f1x on 2017-02-06.
 */

public class SpeechParserTest {
    @Test
    public void test_parse() {
        String text = "spotify then play then pause then triple word command then stop";

        SpeechParser speechParser = new SpeechParser();
        List<NamedObjectId> parsedTexts = speechParser.parse(text, "then");

        assertEquals(5, parsedTexts.size());

        assertEquals(new NamedObjectId("spotify"), parsedTexts.get(0));
        assertEquals(new NamedObjectId("play"), parsedTexts.get(1));
        assertEquals(new NamedObjectId("pause"), parsedTexts.get(2));
        assertEquals(new NamedObjectId("triple word command"), parsedTexts.get(3));
        assertEquals(new NamedObjectId("stop"), parsedTexts.get(4));

        assertEquals(0, countElements(parsedTexts, "then"));
    }

    @Test
    public void test_parse_without_duplicates() {
        String text = "this is first command this is second Command This is third command";

        SpeechParser speechParser = new SpeechParser();
        List<NamedObjectId> parsedTexts = speechParser.parse(text, " ");

        assertEquals(1, countElements(parsedTexts, "this"));
        assertEquals(1, countElements(parsedTexts, "is"));
        assertEquals(1, countElements(parsedTexts, "first"));
        assertEquals(1, countElements(parsedTexts, "second"));
        assertEquals(1, countElements(parsedTexts, "third"));
        assertEquals(1, countElements(parsedTexts, "command"));
    }

    @Test
    public void test_parse_without_delimiter() {
        String text = "triple word command";

        SpeechParser speechParser = new SpeechParser();
        List<NamedObjectId> parsedTexts = speechParser.parse(text, "");
        assertTrue(parsedTexts.contains(new NamedObjectId("triple word command")));
    }

    private int countElements(List<NamedObjectId> list, String element) {
        int count = 0;

        for(NamedObjectId value : list) {
            if(value.equals(new NamedObjectId(element))) {
                ++count;
            }
        }

        return count;
    }
}
