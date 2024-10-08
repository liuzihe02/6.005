/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

/**
 * Tests for ConcreteVerticesGraph.
 * 
 * This class runs the GraphInstanceTest tests against ConcreteVerticesGraph, as
 * well as tests for that particular implementation.
 * 
 * Tests against the Graph spec should be in GraphInstanceTest.
 */
public class ConcreteVerticesGraphTest extends GraphInstanceTest {

    private static final String vertex1 = "v1";
    private static final String vertex2 = "v2";
    private static final String vertex3 = "v3";

    private static final int weight1 = 1;
    private static final int weight2 = 2;

    /*
     * Provide a ConcreteVerticesGraph for tests in GraphInstanceTest.
     */
    @Override
    public Graph<String> emptyInstance() {
        return new ConcreteVerticesGraph<>();
    }

    /*
     * Testing ConcreteVerticesGraph...
     */

    // Testing strategy for ConcreteVerticesGraph.toString()
    // num vertices = 0, 1, n
    // num edges = 0, 1, n

    // tests for ConcreteVerticesGraph.toString()

    // covers num vertices = 0
    // num edges = 0
    @Test
    public void testToStringZeroVerticesZeroEdges() {
        Graph<String> graph = emptyInstance();
        String expected = "";

        assertEquals("expected empty string", expected, graph.toString());
    }

    // covers num vertices = 1
    @Test
    public void testToStringOneVertex() {
        Graph<String> graph = emptyInstance();
        graph.add(vertex1);

        String expected = "";

        assertEquals("expected string", expected, graph.toString());
    }

    // covers num vertices = n
    // num edges = 1
    @Test
    public void testToStringNVerticesOneEdge() {
        Graph<String> graph = emptyInstance();
        graph.add(vertex1);
        graph.add(vertex2);
        graph.set(vertex1, vertex2, weight1);

        String expected = "v1->v2(weight=1)\n";

        assertEquals("expected string", expected, graph.toString());
    }

    // covers num edges = n
    @Test
    public void testToStringNEdges() {
        Graph<String> graph = emptyInstance();
        graph.add(vertex1);
        graph.add(vertex2);
        graph.add(vertex3);
        graph.set(vertex1, vertex2, weight1);
        graph.set(vertex1, vertex3, weight2);

        String expected = "v1->v2(weight=1)\n    v1->v3(weight=2)\n";

        assertEquals("expected string", expected, graph.toString());
    }

    /*
     * Testing Vertex...
     */

    // Testing strategy for Vertex
    // test getLabel()
    //
    // getOutEdges()
    // num edges - 0, 1, n
    //
    // setOutEdge()
    // edge - present, not present
    //
    // removeOutEdge():
    // edge - present, not present
    //
    // test toString()

    // test getLabel()
    @Test
    public void testGetLabel() {
        Vertex<String> v1 = new Vertex<>(vertex1);

        assertEquals("expected to get label 'v1'", vertex1, v1.getLabel());
    }

    // tests getOutEdges()

    // covers num edges - 0
    @Test
    public void testGetOutEdgesNone() {
        Vertex<String> v1 = new Vertex<>(vertex1);

        Map<String, Integer> outEdges = v1.getOutEdges();

        assertTrue("expected empty map", outEdges.isEmpty());
    }

    // covers num edges - 1
    @Test
    public void testGetOutEdgesOne() {
        Vertex<String> v1 = new Vertex<>(vertex1);
        Vertex<String> v2 = new Vertex<>(vertex2);
        v1.setOutEdge(v2, weight1);

        Map<String, Integer> outEdges = v1.getOutEdges();

        assertEquals("expected map size 1", 1, outEdges.size());
        assertTrue("expected map to contain key", outEdges.containsKey(v2.getLabel()));
        assertEquals("expected correct weight", weight1, (int) outEdges.get(v2.getLabel()));
    }

    // covers num edges - n
    @Test
    public void testGetOutEdgesMultiple() {
        Vertex<String> v1 = new Vertex<>(vertex1);
        Vertex<String> v2 = new Vertex<>(vertex2);
        Vertex<String> v3 = new Vertex<>(vertex3);
        v1.setOutEdge(v2, weight1);
        v1.setOutEdge(v3, weight2);

        Map<String, Integer> outEdges = v1.getOutEdges();

        assertEquals("expected map size 2", 2, outEdges.size());
        assertTrue("expected map to contain key", outEdges.containsKey(v2.getLabel()));
        assertTrue("expected map to contain key", outEdges.containsKey(v3.getLabel()));
        assertEquals("expected correct weight", weight1, (int) outEdges.get(v2.getLabel()));
        assertEquals("expected correct weight", weight2, (int) outEdges.get(v3.getLabel()));
    }

    // tests setOutEdge()

    // covers edge - not present before
    @Test
    public void testSetOutEdgeNotPresent() {
        Vertex<String> v1 = new Vertex<>(vertex1);
        Vertex<String> v2 = new Vertex<>(vertex2);

        v1.setOutEdge(v2, weight1);
        Map<String, Integer> outEdges = v1.getOutEdges();

        assertEquals("expected map size 1", 1, outEdges.size());
        assertTrue("expected map to contain key", outEdges.containsKey(v2.getLabel()));
        assertEquals("expected correct weight", weight1, (int) outEdges.get(v2.getLabel()));
    }

    // covers edge - present before, update the edge
    @Test
    public void testSetOutEdgePresent() {
        Vertex<String> v1 = new Vertex<>(vertex1);
        Vertex<String> v2 = new Vertex<>(vertex2);
        v1.setOutEdge(v2, weight1);

        v1.setOutEdge(v2, weight2);
        Map<String, Integer> outEdges = v1.getOutEdges();

        assertEquals("expected map size 1", 1, outEdges.size());
        assertTrue("expected map to contain key", outEdges.containsKey(v2.getLabel()));
        assertEquals("expected updated weight", weight2, (int) outEdges.get(v2.getLabel()));
    }

    // tests removeOutEdge()

    // covers edge - not present
    @Test
    public void testRemoveOutEdgeNotPresent() {
        Vertex<String> v1 = new Vertex<>(vertex1);
        Vertex<String> v2 = new Vertex<>(vertex2);

        int removed = v1.setOutEdge(v2, 0);

        assertEquals("expected removal to fail", 0, removed);
        assertTrue("expected empty outEdges", v1.getOutEdges().isEmpty());
    }

    // covers edge - present
    @Test
    public void testRemoveOutEdgePresent() {
        Vertex<String> v1 = new Vertex<>(vertex1);
        Vertex<String> v2 = new Vertex<>(vertex2);
        v1.setOutEdge(v2, weight1);

        assertEquals("expected map size of 1", 1, v1.getOutEdges().size());

        int removed = v1.setOutEdge(v2, 0);

        assertEquals("expected removal to succeed", 1, removed);
        assertTrue("expected empty outEdges", v1.getOutEdges().isEmpty());
    }

    // Test toString()
    @Test
    public void testVertexToString() {
        Vertex<String> v1 = new Vertex<>(vertex1);
        Vertex<String> v2 = new Vertex<>(vertex2);
        Vertex<String> v3 = new Vertex<>(vertex3);

        v1.setOutEdge(v2, weight1);
        v1.setOutEdge(v3, weight2);

        String expected = "v1->v2(weight=1)\n    v1->v3(weight=2)\n";

        assertEquals("expected string representation", expected, v1.toString());
    }
}
