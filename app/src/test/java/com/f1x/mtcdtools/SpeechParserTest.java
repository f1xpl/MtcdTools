package com.f1x.mtcdtools;

import com.f1x.mtcdtools.storage.SpeechParser;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertTrue;

/**
 * Created by f1x on 2017-02-06.
 */

public class SpeechParserTest {
    @Test
    public void test_parse() {
        List<String> texts = new ArrayList<>();

        texts.add("spotify then play then pause then triple word command then stop");
        texts.add("poweramp then stop");
        texts.add("google maps then nagivate home");

        SpeechParser speechParser = new SpeechParser();
        List<String> parsedTexts = speechParser.parse(texts, "then");

        assertTrue(parsedTexts.contains("spotify"));
        assertTrue(parsedTexts.contains("play"));
        assertTrue(parsedTexts.contains("pause"));
        assertTrue(parsedTexts.contains("triple word command"));
        assertTrue(parsedTexts.contains("stop"));
    }

    @Test
    public void test_parse_without_delimiter() {
        List<String> texts = new ArrayList<>();

        texts.add("triple word command");

        SpeechParser speechParser = new SpeechParser();
        List<String> parsedTexts = speechParser.parse(texts, "");
        assertTrue(parsedTexts.contains("triple word command"));
    }
}
