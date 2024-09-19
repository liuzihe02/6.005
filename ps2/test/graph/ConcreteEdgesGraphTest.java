/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests for ConcreteEdgesGraph.
 * 
 * This class runs the GraphInstanceTest tests against ConcreteEdgesGraph, as
 * well as tests for that particular implementation.
 * 
 * Tests against the Graph spec should be in GraphInstanceTest.
 */
public class ConcreteEdgesGraphTest extends GraphInstanceTest {

    private static final String vertex1 = "v1";
    private static final String vertex2 = "v2";
    private static final String vertex3 = "v3";

    private static final int weight1 = 1;
    private static final int weight2 = 2;

    /*
     * Provide a ConcreteEdgesGraph for tests in GraphInstanceTest.
     */
    @Override
    public Graph<String> emptyInstance() {
        return new ConcreteEdgesGraph();
    }

    /*
     * Testing ConcreteEdgesGraph...
     */

    // Testing strategy for ConcreteEdgesGraph.toString()
    // num vertices = 0, 1, n
    // num edges = 0, 1, n

    @Test
    public String testEdgestoString() {
        ConcreteEdgesGraph<String> ge = emptyInstance()
        
    }

    /*
     * Testing Edge...
     */

    // Testing strategy for Edge
    // TODO

    // TODO tests for operations of Edge

}
