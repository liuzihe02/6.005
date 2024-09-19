/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * An implementation of Graph.
 * 
 * <p>
 * PS2 instructions: you MUST use the provided rep.
 */
public class ConcreteEdgesGraph implements Graph<String> {

    private final Set<String> vertices = new HashSet<>();
    private final List<Edge> edges = new ArrayList<>();

    // Abstraction function:
    // AF (vertices, edges) = directed graph of distinct vertices connected by
    // weighted edges

    // Representation invariant:
    // edges have positive weight

    // Safety from rep exposure:
    // all fields are private and final
    // vertices and edges is a mutable set, so return defensive copies to avoid
    // sharing true objects with clients

    // Empty constructor
    public ConcreteEdgesGraph() {
        checkRep();
    }

    // checkRep
    public void checkRep() {
        for (Edge e : edges) {
            if (e.getWeight() < 0) {
                throw new AssertionError("weights be zero or more!");
            }
        }
    }

    @Override
    public boolean add(String vertex) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public int set(String source, String target, int weight) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public boolean remove(String vertex) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Set<String> vertices() {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Map<String, Integer> sources(String target) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Map<String, Integer> targets(String source) {
        throw new RuntimeException("not implemented");
    }

    // TODO toString()

}

/**
 * An edge between two vertices.
 * Immutable.
 * This class is internal to the rep of ConcreteEdgesGraph.
 * 
 * you are unable to create an empty edge. All edges must be initialized with
 * source, target, weight
 * 
 * we use generics here
 *
 */
class Edge {

    // fields
    private final String source;
    private final String target;
    private final Integer weight;

    // Abstraction function:
    // AF(source, target, weight) = an edge in a directed graph with a
    // source and target vertices and weight

    // Representation invariant:
    // weight>0

    // Safety from rep exposure:
    // all fields are private and final
    // source and target are immutable types, while weight is Integer, so all fields
    // are immutable; can return directly!

    // constructor
    public Edge(String source, String target, Integer weight) {
        this.source = source;
        this.target = target;
        this.weight = weight;
        checkRep();
    }

    // checkRep
    private void checkRep() {
        assert (weight > 0) : "Edge weight must be positive!";
    }

    // methods
    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }

    public Integer getWeight() {
        return weight;
    }

    // toString(), we override Object.toString()
    @Override
    public String toString() {
        // automatically converts Integer to string representation
        return source + "->" + target + "(weight=" + weight + ")";
    }

}
