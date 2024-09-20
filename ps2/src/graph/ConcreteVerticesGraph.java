/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;

/**
 * An valid implementation of Graph, using only
 * 
 * vertices which contains all the information about the vertex
 * 
 * <p>
 * PS2 instructions: you MUST use the provided rep.
 */
public class ConcreteVerticesGraph implements Graph<String> {

    private final List<Vertex> vertices = new ArrayList<>();

    // Abstraction function:
    // AF (vertices): directed graph composed of
    // distinct vertices connected by weighted edges

    // Representation invariant:
    // edges have positive weight

    // Safety from rep exposure:
    // vertices field is private and final
    // vertices is a mutable list, so vertices() makes defensive copies
    // to avoid rep exposure

    // Empty constructor
    public ConcreteVerticesGraph() {
        checkRep();
    }

    // checkRep
    public void checkRep() {
        // TODO:
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
 * A vertex, containing its own label, and its parent and children vertexes
 * Mutable.
 * This class is internal to the rep of ConcreteVerticesGraph.
 * 
 * <p>
 * PS2 instructions: the specification and implementation of this class is
 * up to you.
 */
class Vertex {

    // fields
    private final String label;
    private final Map<Vertex, Integer> childEdges = new HashMap<>();

    // Abstraction function:
    // AF(label, inEdges, outEdges) = a labeled vertex with a set of incoming and
    // outcoming weighted edges
    //
    // Representation invariant:
    // all edges are positive nodes
    //
    // Safety from rep exposure:
    // all fields are private and final;
    // childenEdges are mutable Map, so getChildrenEdges()
    // make defensive copies to avoid sharing the rep with clients.

    // constructor
    Vertex(String label) {
        this.label = label;
        checkRep();
    }

    // checkRep
    private void checkRep() {
        for (Integer value : childEdges.values()) {
            if (value <= 0) {
                throw new AssertionError("weight must be positive!");
            }
        }
    }

    /**
     * if edge didnt already exist, then add a new edge
     * if it already existed, update the edge
     * 
     * @param vertex
     * @param weight must be more than 0
     * @return the old weight value if it previously existed. return 0 if it didnt
     *         previously exist
     */
    int setOutEdge(Vertex childVertex, Integer weight) {
        // check if valid weight
        if (weight <= 0) {
            throw new AssertionError("weight must be more than zero!");
        }
        // acceptable weight
        else {
            // check if vertex already exists
            if (childEdges.containsKey(childVertex)) {
                int oldWeight = childEdges.get(childVertex);
                childEdges.put(childVertex, weight);
                return oldWeight;
            }
            // doesnt contain child vertex
            else {
                childEdges.put(childVertex, weight);
                return weight;
            }

        }
    }

    /**
     * 
     * @param vertex
     * @return the old weight if it previously existed. return 0 if it didnt
     *         previously exist
     */
    int removeOutEdge(Vertex childVertex) {
        // check if vertex already exists
        if (childEdges.containsKey(childVertex)) {
            int oldWeight = childEdges.get(childVertex);
            childEdges.remove(childVertex)
            return oldWeight;
        }
        // doesnt contain child vertex
        else {
            childEdges.put(childVertex, weight);
            return weight;
        }
    }

    String getLabel() {
        // TODO
        return "";
    }

    // note that this method returns the mutable object directly, be careful with
    // this!
    Map<Vertex, Integer> getOutEdges() {
        return childEdges;
    }

    // TODO toString()
    @Override
    public String toString() {
        // TODO
    }

}
