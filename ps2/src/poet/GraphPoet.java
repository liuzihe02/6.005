/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package poet;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Set;
import java.util.LinkedHashSet;

import java.nio.file.Files;

import graph.Graph;

/**
 * A graph-based poetry generator.
 * 
 * <p>
 * GraphPoet is initialized with a corpus of text, which it uses to derive a
 * word affinity graph.
 * Vertices in the graph are words. Words are defined as non-empty
 * case-insensitive strings of non-space non-newline characters. They are
 * delimited in the corpus by spaces, NEWLINES, or the ends of the file.
 * Edges in the graph count adjacencies: the number of times "w1" is followed by
 * "w2" in the corpus is the weight of the edge from w1 to w2.
 * 
 * <p>
 * For example, given this corpus:
 * 
 * <pre>
 *     Hello, HELLO, hello, goodbye!
 * </pre>
 * <p>
 * the graph would contain two edges:
 * <ul>
 * <li>("hello,") -> ("hello,") with weight 2
 * <li>("hello,") -> ("goodbye!") with weight 1
 * </ul>
 * <p>
 * where the vertices represent CASE-INSENSITIVE {@code "hello,"} and
 * {@code "goodbye!"}.
 * 
 * <p>
 * Given an input string, GraphPoet generates a poem by attempting to
 * insert a bridge word between every adjacent pair of words in the input.
 * The bridge word between input words "w1" and "w2" will be some "b" such that
 * w1 -> b -> w2 is a two-edge-long path with maximum-weight weight among all
 * the two-edge-long paths from w1 to w2 in the affinity graph.
 * If there are no such paths, no bridge word is inserted.
 * In the output poem, INPUT WORDS RETAIN THEIR ORIGINAL CASE, while bridge
 * words are lower case. The whitespace between every word in the poem is a
 * single space.
 * 
 * Child vertexes have order - insertion order
 * 
 * <p>
 * For example, given this corpus:
 * 
 * <pre>
 *     This is a test of the Mugar Omni Theater sound system.
 * </pre>
 * <p>
 * on this input:
 * 
 * <pre>
 *     Test the system.
 * </pre>
 * <p>
 * the output poem would be:
 * 
 * <pre>
 *     Test of the system.
 * </pre>
 * 
 * <p>
 * PS2 instructions: this is a required ADT class, and you MUST NOT weaken
 * the required specifications. However, you MAY strengthen the specifications
 * and you MAY add additional methods.
 * You MUST use Graph in your rep, but otherwise the implementation of this
 * class is up to you.
 */
public class GraphPoet {

    private final Graph<String> graph = Graph.empty();

    // Abstraction function:
    // AF(graph) = a poetry generator

    // Representation invariant:
    // vertices of the graph are non-empty case-insensitive strings
    // of non-space non-newline characters

    // Safety from rep exposure:
    // graph field is private and final;

    // check rep invariant
    // check each vertex is a valid word
    private void checkRep() {
        for (String vertex : graph.vertices()) {
            String copy = vertex.toLowerCase().trim().replaceAll("\\s+", "");

            assert vertex.equals(copy);
            assert !vertex.equals("");
        }
    }

    /**
     * Create a new poet with the graph from corpus (as described above).
     * 
     * @param corpus text file from which to derive the poet's affinity graph
     * @throws IOException if the corpus file cannot be found or read
     */
    public GraphPoet(File corpus) throws IOException {
        List<String> wordList = createWordList(corpus);
        Set<String> distinctWords = new LinkedHashSet<>(wordList);

        // first add all my distinct words as vertexes
        for (String distinct : distinctWords) {
            graph.add(distinct);
        }

        // for each word - this is the parent node
        for (String referenceWord : distinctWords) {

            // we're gonna start adding children to each parent
            // traverse the List, up until the second to last word!
            for (int i = 0; i < wordList.size() - 1; i++) {
                // found the refernce
                if (wordList.get(i).equals(referenceWord)) {
                    // check if the nextWord we want to modify even exists in the children graph
                    Map<String, Integer> children = graph.targets(referenceWord);
                    String nextWord = wordList.get(i + 1);
                    if (children.get(nextWord) == null) {
                        // the child vertex doesnt yet exist
                        graph.set(referenceWord, nextWord, 1);
                    }
                    // child vertex already exists
                    else {
                        graph.set(referenceWord, nextWord, children.get(nextWord) + 1);
                    }
                }
            }
        }
        ;
        checkRep();
    }

    /**
     * Create a list of words from the given corpus file.
     * 
     * @param corpus The file containing the corpus text
     * @return A list of words extracted from the corpus
     * @throws IOException If there's an error reading the file
     */
    private List<String> createWordList(File corpus) throws IOException {
        List<String> words = new ArrayList<>();
        // Read all lines from the file
        List<String> lines = Files.readAllLines(corpus.toPath());

        // Process each line of the file
        for (String line : lines) {
            // Add all words from the current line to the list; here we make them all lower
            // case!
            words.addAll(processText(line, true));
        }
        return words;
    }

    /**
     * Process a text string into a list of words.
     * 
     * @param text        The input text to process
     * @param toLowercase If true, convert all words to lowercase; if false,
     *                    preserve original case
     * @return A list of processed words
     */
    private List<String> processText(String text, boolean toLowerCase) {
        List<String> words = new ArrayList<>();
        // Split the text into words using whitespace as delimiter
        String[] textWords = text.split("\\s+");
        for (String word : textWords) {
            // remove trailing white spaces
            word = word.trim();
            // decide whether to convert to lowercase
            word = toLowerCase ? word.toLowerCase() : word;
            // Only add non-empty words
            if (!word.isEmpty()) {
                words.add(word);
            }
        }
        return words;
    }

    /**
     * Generate a poem.
     * 
     * @param input string from which to create the poem
     * @return poem (as described above)
     * @throws IOException
     */
    public String poem(String input) throws IOException {
        // check if empty string
        if (input == null || input.trim().isEmpty()) {
            return "";
        }

        // Create a list of words from the input, heep the case here!
        List<String> inputWords = processText(input, false);
        StringBuilder poem = new StringBuilder();

        // Process each pair of adjacent words

        // go up to second last word
        for (int i = 0; i < inputWords.size() - 1; i++) {
            String currentWord = inputWords.get(i);
            String nextWord = inputWords.get(i + 1);

            // Add the current word to the poem (with original case)
            poem.append(currentWord).append(" ");

            // Find the best bridge word, source is current word, target is next word
            // WHEN SEARCHING, we need to make it lowercase so we can compare!
            String bridgeWord = findBestBridgeWord(currentWord.toLowerCase(), nextWord.toLowerCase());

            // If a bridge word is found, add it to the poem
            if (bridgeWord != null) {
                poem.append(bridgeWord.toLowerCase()).append(" ");
            }
        }

        // Add the last word; edge case!
        poem.append(inputWords.get(inputWords.size() - 1));
        // return this as a string object
        return poem.toString();
    }

    /**
     * finds the best bridge word given current and next word.
     * 
     * first we loop over all the children of current words to get possible bridges.
     * Then we loop over all the parents of next word to also find possible bridges.
     * check if the bridges match; if they do return the best one!
     * 
     * @param source the current word we're considering
     * @param target the next word we're considering
     * @return the best bridge word between current word and next word based on the
     *         graph
     */
    private String findBestBridgeWord(String source, String target) {
        String bestBridge = null;
        int maxWeight = 0;

        // this map contains all the children (possible bridge) of the currentWord which
        // is Source
        Map<String, Integer> currentWordToBridgeMap = graph.targets(source);

        // check all possible bridge words of this mapping
        for (Map.Entry<String, Integer> entry : currentWordToBridgeMap.entrySet()) {
            String bridge = entry.getKey();
            int currentWordToBridgeWeight = entry.getValue();

            // this map contains all the children (one of which may include nextWord) of the
            // bridge that we're considering
            Map<String, Integer> bridgeToNextWordMap = graph.targets(bridge);

            Integer bridgeToNextWordWeight = bridgeToNextWordMap.get(target);
            if (bridgeToNextWordWeight == null)
                // go to next possible bridge word, we didnt find a match
                continue;

            // do the update
            int totalWeight = currentWordToBridgeWeight + bridgeToNextWordWeight;
            if (totalWeight > maxWeight) {
                maxWeight = totalWeight;
                bestBridge = bridge;
            }
        }

        return bestBridge;
    }

}
