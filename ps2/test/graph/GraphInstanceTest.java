/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * Tests for INSTANCE methods of Graph.
 * 
 * <p>
 * PS2 instructions: you MUST NOT add constructors, fields, or non-@Test
 * methods to this class, or change the spec of {@link #emptyInstance()}.
 * Your tests MUST only obtain Graph instances by calling emptyInstance().
 * Your tests MUST NOT refer to specific concrete implementations.
 */
public abstract class GraphInstanceTest {

    /*
     * TESTING STRATEGY
     * for each instance method, for each parameter/key variable, vary the possible
     * values
     *
     * add:
     * vertex to be added: exists, does not exist
     * graph has: 0, 1, n vertexes
     * 
     * set:
     * source, target: in graph, not in graph
     * edge to be added: new, modified
     * weight: 0, n
     * graph size: 0,1,n
     * 
     * remove:
     * - vertex: in graph, not in graph
     * - graph size: 0,1,n
     * 
     * vertices:
     * - graph size: 0,1,n
     * 
     * sources:
     * - target: in graph, not in graph
     * - num sources of the target: 0,n
     * - graph size: 0,1,n
     * 
     * targets
     * - source: in graph, not in graph
     * - num targets of the source: 0,n
     * - graph size: 0,1,n
     */

    private static final String vertex1 = "v1";
    private static final String vertex2 = "v2";
    private static final String vertex3 = "v3";

    private static final int weight0 = 0;
    private static final int weight1 = 1;
    private static final int weight2 = 2;

    /**
     * Overridden by implementation-specific test classes.
     * 
     * @return a new empty graph of the particular implementation being tested
     */
    public abstract Graph<String> emptyInstance();

    @Test(expected = AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    @Test
    public void testInitialVerticesEmpty() {
        assertEquals("expected new graph to have no vertices",
                Collections.emptySet(), emptyInstance().vertices());
    }

    // test add

    // vertex not in graph, graph size 0
    @Test
    public void testAddNewVertex() {
        Graph<String> g = emptyInstance();
        assertTrue("Adding a new vertex should return true", g.add(vertex1));
        assertTrue("Graph should contain", g.vertices().contains(vertex1));
    }

    // vertex in graph, graph size 1
    @Test
    public void testAddExistingVertex() {
        Graph<String> g = emptyInstance();
        g.add(vertex1);
        assertFalse("Adding an existing vertex should return false", g.add(vertex1));
        assertEquals("Graph should still have 1 vertex after adding duplicate", 1, g.vertices().size());
    }

    // vertex not in graph, graph size 2
    @Test
    public void testAddMultipleVertices() {
        Graph<String> g = emptyInstance();
        g.add(vertex1);
        g.add(vertex2);
        assertTrue("Adding third vertex should return true", g.add(vertex3));
        assertEquals("Graph should have 3 vertices after adding third", 3, g.vertices().size());
        assertTrue("Graph should contain all added vertices",
                // this will check with a List type
                g.vertices().containsAll(Arrays.asList(vertex1, vertex2, vertex3)));
    }

    // test set, sources, targets altogether

    /*
     * source in graph, target not in graph
     * new edge to be added
     * weight 1
     * graph size 1
     * 
     * num targets of the source: 1
     */
    @Test
    public void testSetSourceExists() {
        Graph<String> g = emptyInstance();
        g.add(vertex1);
        assertEquals("Setting new edge should return 0", 0, g.set(vertex1, vertex2, weight1));
        assertEquals("Graph should have 2 vertices", 2, g.vertices().size());

        Map<String, Integer> targets = new HashMap<>();
        targets.put(vertex2, weight1);

        assertEquals("expected Graph containing edge", targets, g.targets(vertex1));
    }

    /*
     * target in graph, source not in graph
     * new edge to be added
     * weight 1
     * graph size 2
     * 
     * num sources of target: 1
     */
    @Test
    public void testSetTargetExists() {
        Graph<String> g = emptyInstance();
        g.add(vertex1);
        g.add(vertex2);
        assertEquals("Setting new edge should return 0", 0, g.set(vertex3, vertex2, weight1));
        assertEquals("Graph should have 3 vertices", 3, g.vertices().size());

        Map<String, Integer> sources = new HashMap<>();
        sources.put(vertex3, weight1);

        assertEquals("expected Graph containing edge", sources, g.sources(vertex2));
    }

    /*
     * target not in graph, source not in graph
     * new edge
     * weight 2
     * graph size 0
     * 
     * num sources of target: 1
     */
    @Test
    public void testSetEmptyGraph() {
        Graph<String> g = emptyInstance();
        assertEquals("Setting new edge should return 0", 0, g.set(vertex1, vertex2, weight2));
        assertEquals("Graph should have 2 vertices", 2, g.vertices().size());

        Map<String, Integer> sources = new HashMap<>();
        sources.put(vertex2, weight2);

        assertEquals("expected Graph containing edge", sources, g.sources(vertex2));
    }

    /*
     * target in graph, source in graph
     * modified edge
     * weight 1
     * graph size 3
     */

    @Test
    public void testSetBothExist() {
        Graph<String> g = emptyInstance();
        g.add(vertex1);
        g.add(vertex2);
        assertEquals("Setting new edge should return 0", 0, g.set(vertex1, vertex2, weight1));
        assertEquals("Modifying edge should return previous weight", weight1, g.set(vertex1, vertex2, weight2));
        assertEquals("Graph should have this many vertices", 2, g.vertices().size());

        Map<String, Integer> sources = new HashMap<>();
        sources.put(vertex2, weight2);

        assertEquals("expected Graph containing edge", sources, g.sources(vertex2));
    }

    /*
     * target in graph, source in graph
     * modified edge
     * weight 0
     * graph size 3
     * 
     */
    @Test
    public void testSetZeroWeight() {
        Graph<String> g = emptyInstance();
        g.add(vertex1);
        g.add(vertex2);
        assertEquals("Setting new edge should return 0", 0, g.set(vertex1, vertex2, weight1));
        assertEquals("Removing edge should return previous weight", weight1, g.set(vertex1, vertex2, weight0));
        assertEquals("Graph should have this many vertices", 2, g.vertices().size());

        assertEquals("expected Graph containing zero edges", Collections.emptyMap(), g.targets(vertex1));
        assertEquals("expected Graph containing zero edges", Collections.emptyMap(), g.sources(vertex2));
    }

    // test remove

    // vertex in graph
    // graph size 2
    @Test
    public void testRemoveVertexExists() {
        Graph<String> g = emptyInstance();
        g.add(vertex1);
        g.add(vertex2);
        assertEquals("Setting new edge should return 0", 0, g.set(vertex1, vertex2, weight1));
        assertEquals("Graph should have this many vertices", 2, g.vertices().size());

        assertTrue("removed vertex", g.remove(vertex1));
        assertEquals("expected Graph containing zero edges", Collections.emptyMap(), g.sources(vertex2));
    }

    // vertex not in graph
    // graph size 0
    @Test
    public void testRemoveVertexNotExists() {
        Graph<String> g = emptyInstance();
        assertFalse("removed vertex", g.remove(vertex1));
    }

    // Test for vertices()

    @Test
    public void testVerticesEmptyGraph() {
        Graph<String> g = emptyInstance();
        assertEquals("Empty graph should have no vertices", 0, g.vertices().size());
    }

    @Test
    public void testVerticesMultipleVertices() {
        Graph<String> g = emptyInstance();
        g.add(vertex1);
        g.add(vertex2);
        g.add(vertex3);
        assertEquals("Graph should have three vertices", 3, g.vertices().size());
        assertTrue("Graph should contain all added vertices",
                g.vertices().containsAll(Arrays.asList(vertex1, vertex2, vertex3)));
    }

    // Tests for sources(target)

    @Test
    public void testSourcesTargetNotInGraph() {
        Graph<String> g = emptyInstance();
        assertTrue("Sources of non-existent target should be empty", g.sources(vertex1).isEmpty());
    }

    @Test
    public void testSourcesTargetWithNoSources() {
        Graph<String> g = emptyInstance();
        g.add(vertex1);
        assertTrue("Target with no sources should return empty map", g.sources(vertex1).isEmpty());
    }

    @Test
    public void testSourcesTargetWithMultipleSources() {
        Graph<String> g = emptyInstance();
        g.set(vertex1, vertex3, weight1);
        g.set(vertex2, vertex3, weight2);
        Map<String, Integer> expectedSources = new HashMap<>();
        expectedSources.put(vertex1, weight1);
        expectedSources.put(vertex2, weight2);
        assertEquals("Target should have correct sources", expectedSources, g.sources(vertex3));
    }

    // Tests for targets(source)

    @Test
    public void testTargetsSourceNotInGraph() {
        Graph<String> g = emptyInstance();
        assertTrue("Targets of non-existent source should be empty", g.targets(vertex1).isEmpty());
    }

    @Test
    public void testTargetsSourceWithNoTargets() {
        Graph<String> g = emptyInstance();
        g.add(vertex1);
        assertTrue("Source with no targets should return empty map", g.targets(vertex1).isEmpty());
    }

    @Test
    public void testTargetsSourceWithMultipleTargets() {
        Graph<String> g = emptyInstance();
        g.set(vertex1, vertex2, weight1);
        g.set(vertex1, vertex3, weight2);
        Map<String, Integer> expectedTargets = new HashMap<>();
        expectedTargets.put(vertex2, weight1);
        expectedTargets.put(vertex3, weight2);
        assertEquals("Source should have correct targets", expectedTargets, g.targets(vertex1));
    }

}
