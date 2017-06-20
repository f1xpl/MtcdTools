package android.microntek.f1x.mtcdtools.utils;

import android.microntek.f1x.mtcdtools.utils.SpeechParser;

import org.junit.Test;

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
        List<String> parsedTexts = speechParser.parse(text, "then");

        assertEquals(5, parsedTexts.size());

        assertEquals("spotify", parsedTexts.get(0));
        assertEquals("play", parsedTexts.get(1));
        assertEquals("pause", parsedTexts.get(2));
        assertEquals("triple word command", parsedTexts.get(3));
        assertEquals("stop", parsedTexts.get(4));

        assertEquals(0, countElements(parsedTexts, "then"));
    }

    @Test
    public void test_parse_without_duplicates() {
        String text = "this is first command this is second Command This is third command";

        SpeechParser speechParser = new SpeechParser();
        List<String> parsedTexts = speechParser.parse(text, " ");

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
        List<String> parsedTexts = speechParser.parse(text, "");
        assertTrue(parsedTexts.contains("triple word command"));
    }

    private int countElements(List<String> list, String element) {
        int count = 0;

        for(String value : list) {
            if(value.equals(element)) {
                ++count;
            }
        }

        return count;
    }
}
