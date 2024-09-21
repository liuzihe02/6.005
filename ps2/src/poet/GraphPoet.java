/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package poet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.LinkedHashSet;

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
 * where the vertices represent case-insensitive {@code "hello,"} and
 * {@code "goodbye!"}.
 * 
 * <p>
 * Given an input string, GraphPoet generates a poem by attempting to
 * insert a bridge word between every adjacent pair of words in the input.
 * The bridge word between input words "w1" and "w2" will be some "b" such that
 * w1 -> b -> w2 is a two-edge-long path with maximum-weight weight among all
 * the two-edge-long paths from w1 to w2 in the affinity graph.
 * If there are no such paths, no bridge word is inserted.
 * In the output poem, input words retain their original case, while bridge
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
        // for each word - this is the parent node
        for (String referenceWord : distinctWords) {
            // we're gonna start adding children to each parent
            // traverse the List, up until the second to last word!
            for (int i = 0; i < wordList.size() - 1; i++) {
                // found the refernce
                if (wordList.get(i).equals(referenceWord)) {
                    // if new vertex doesnt even exist in the graph
                    if (graph.add(referenceWord)) {
                        graph.set(referenceWord, wordList.get(i + 1), 1);
                    }
                    // vertex already exists in the graph, then we update the weight
                    else {
                        int newWeight = graph.targets(referenceWord).get(wordList.get(i + 1));
                        graph.set(referenceWord, wordList.get(i + 1), newWeight + 1);
                    }
                }
            }
        }
        ;
        checkRep();
    }

    /**
     * creates a list of words from a File object
     */
    private List<String> createWordList(File corpus) throws IOException {
        List<String> words = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(corpus))) {
            // declare line
            String line;
            // Read each line of the file until we reach the end (null)
            while ((line = reader.readLine()) != null) {
                // Split the line into words using whitespace as the delimiter
                // `\\s+` means one or more whitespace
                String[] lineWords = line.split("\\s+");
                // Process each word in the line
                for (String word : lineWords) {
                    // Convert the word to lowercase and remove leading/trailing whitespace
                    word = word.toLowerCase().trim();
                    // Only add non-empty words to the set
                    if (!word.isEmpty()) {
                        words.add(word);
                    }
                }
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
        // Create a list of words from the input
        List<String> inputWords = createWordList(new File(input));
        StringBuilder poem = new StringBuilder();
    }

    // TODO toString()

}
