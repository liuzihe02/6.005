/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package poet;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

/**
 * Tests for GraphPoet.
 */
public class GraphPoetTest {

    @Test(expected = AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    // Testing strategy for poem():
    // num bridge words to insert - 0, 1, n
    // multiple bridges weight - same, different
    // case-sensitivity
    // multiple lines

    // Test with simple corpus
    @Test
    public void testSimplePoem() throws IOException {
        GraphPoet poet = new GraphPoet(new File("test/poet/simple-corpus.txt"));
        String input = "This is the world poetry";
        String expected = "This is the world of poetry";
        assertEquals(expected, poet.poem(input));
    }

    // Test with no bridge words
    @Test
    public void testNoBridgeWords() throws IOException {
        GraphPoet poet = new GraphPoet(new File("test/poet/simple-corpus.txt"));
        String input = "poetry hello";
        String expected = "poetry hello";
        assertEquals(expected, poet.poem(input));
    }

    // Test with same weight, insertion order, choose the word that came first
    @Test
    public void testRepeatingWords() throws IOException {
        GraphPoet poet = new GraphPoet(new File("test/poet/same-weight.txt"));
        String input = "the brown fox";
        String expected = "the quick brown fox";
        assertEquals(expected, poet.poem(input));
    }

    // Test with multiple possible bridge words (choose highest weight)
    @Test
    public void testMultipleBridgeWords() throws IOException {
        GraphPoet poet = new GraphPoet(new File("test/poet/different-weight.txt"));
        String input = "the brown fox";
        String expected = "the slow brown fox";
        assertEquals(expected, poet.poem(input));
    }

    // Test case sensitivity
    @Test
    public void testCaseSensitivity() throws IOException {
        GraphPoet poet = new GraphPoet(new File("test/poet/case-sensitive.txt"));
        String input = "The Sat";
        String expected = "The Cat Sat";
        assertEquals(expected, poet.poem(input));
    }

    // Test with empty input
    @Test
    public void testEmptyInput() throws IOException {
        GraphPoet poet = new GraphPoet(new File("test/poet/simple-corpus.txt"));
        String input = "";
        String expected = "";
        assertEquals(expected, poet.poem(input));
    }

    // Test with input not in corpus
    @Test
    public void testInputNotInCorpus() throws IOException {
        GraphPoet poet = new GraphPoet(new File("test/poet/simple-corpus.txt"));
        String input = "graph theory";
        String expected = "graph theory";
        assertEquals(expected, poet.poem(input));
    }

}
