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
import java.util.LinkedHashMap;

/**
 * An valid implementation of Graph, using only
 * 
 * vertices which contains the parent vertex
 * each Vertex object containing all its children Vertexes, as pointers
 * 
 * <p>
 * PS2 instructions: you MUST use the provided rep.
 */
public class ConcreteVerticesGraph<L> implements Graph<L> {

    private final List<Vertex<L>> vertices = new ArrayList<>();

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
        // will access checkRep method for ConcreteVerticesGraph
        checkRep();
    }

    // checkRep
    public void checkRep() {
        for (Vertex<L> vertex : vertices) {
            vertex.checkRep();
        }
    }

    @Override
    public boolean add(L vertex) {
        if (inGraph(vertex)) {
            return false;
        }
        ;
        // current vertex doesn't exist
        // create a new Vertex object
        Vertex<L> vertexObj = new Vertex<>(vertex);
        vertices.add(vertexObj);
        return true;
    }

    @Override
    public int set(L source, L target, int weight) {
        // add source if its not in list
        add(source);
        // add target if its not in list
        add(target);

        // find source
        Vertex<L> sourceObj = findVertex(source);
        Vertex<L> targetObj = findVertex(target);
        return sourceObj.setOutEdge(targetObj, weight);
    }

    @Override
    public boolean remove(L vertex) {
        if (inGraph(vertex)) {
            // remove the actual vertex
            Vertex<L> toRemove = findVertex(vertex);
            vertices.remove(toRemove);
            // loop through remaining vertices and remove all of its mentions as child
            for (Vertex<L> remain : vertices) {
                // remove it if the key exists
                remain.setOutEdge(toRemove, 0);
            }
            ;
            return true;
        }
        // didnt exist
        else {
            return false;
        }
    }

    @Override
    public Set<L> vertices() {
        Set<L> labels = new HashSet<>();
        for (Vertex<L> vertexObj : vertices) {
            labels.add(vertexObj.getLabel());
        }
        return labels;
    }

    @Override
    /**
     * target is basically the child. we want to get all the parents of this child
     * 
     * @param target
     * @return
     */
    public Map<L, Integer> sources(L target) {
        Map<L, Integer> parents = new HashMap<>();

        for (Vertex<L> sourceObj : vertices) {
            Map<L, Integer> childEdges = sourceObj.getOutEdges();
            if (childEdges.containsKey(target)) {
                // add the label and integer weight
                parents.put(sourceObj.getLabel(), childEdges.get(target));
            }
        }
        ;
        return parents;
    }

    @Override
    public Map<L, Integer> targets(L source) {
        // source not even found
        if (inGraph(source)) {
            Vertex<L> sourceObj = findVertex(source);
            return sourceObj.getOutEdges();
        } else {
            return new HashMap<>();
        }

    }

    /**
     * pass in a label, not the actual Vertex object!
     * 
     * @param vertex
     * @return whether the Vertex object corresponding to this label exists in the
     *         graph
     */
    private boolean inGraph(L vertex) {
        for (Vertex<L> vertexObj : vertices) {
            if (vertexObj.getLabel().equals(vertex)) {
                return true;
            }
        }
        ;
        return false;
    }

    /**
     * Get the actual Vertex object
     * 
     * remember, returning null in java is generally not good practice!
     * 
     * @param vertex The label of the vertex to find
     * @return The Vertex object with the given label
     * @throws AssertionError if the vertex is not in the graph
     */
    private Vertex<L> findVertex(L vertex) {
        for (Vertex<L> vertexObj : vertices) {
            if (vertexObj.getLabel().equals(vertex)) {
                return vertexObj;
            }
        }
        // If we get here, the vertex is in the graph but wasn't found
        // This should never happen if inGraph() is implemented correctly
        throw new AssertionError("Vertex not found!");
    }

    // toString(), we override Object.toString() always!
    @Override
    public String toString() {
        List<String> edgeStrings = new ArrayList<>();
        for (Vertex<L> v : vertices) {
            // iterate over edges
            for (Map.Entry<L, Integer> entry : v.getOutEdges().entrySet()) {
                edgeStrings.add(v.getLabel() + "->" + entry.getKey() + "(weight=" + entry.getValue() + ")\n");
            }
        }
        String edgesString = String.join("    ", edgeStrings);
        return edgesString;
    }

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
class Vertex<L> {

    // fields, L here stands for label
    private final L label;
    // insertion order
    // this maps the IDENTIFIER of the childvertex to its weight
    // most important, we can directly do comparison with this identifier; hashable
    // id of v1 == id of v2
    private final Map<L, Integer> childEdges = new LinkedHashMap<>();

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
    Vertex(L label) {
        this.label = label;
        checkRep();
    }

    // checkRep
    void checkRep() {
        for (Integer value : childEdges.values()) {
            if (value <= 0) {
                throw new AssertionError("weight must be positive!");
            }
        }
    }

    /**
     * Add a new edge or update an existing one.
     * 
     * @param childVertex The target vertex
     * @param weight      Must be non-negative
     * @return The old weight if the edge existed, 0 otherwise
     */
    int setOutEdge(Vertex<L> childVertex, Integer weight) {
        if (weight < 0) {
            throw new AssertionError("Weight must be non-negative!");
        }

        L childLabel = childVertex.getLabel();
        // get the oldWeight, note that if its null it means it didnt exist before
        Integer oldWeight = childEdges.get(childLabel);

        if (weight == 0) {
            childEdges.remove(childLabel);
        } else {
            childEdges.put(childLabel, weight);
        }

        return oldWeight != null ? oldWeight : 0;
    }

    L getLabel() {
        return label;
    }

    /*
     * note this method returns a defensive copy, but is a little subtle. the
     * references to the object inside is still the same!
     * in other words we can still mutate the objects inside this defensive copy
     * 
     * but we removing or adding elements to this defensive copy does not modify the
     * original copy
     */
    Map<L, Integer> getOutEdges() {
        return new HashMap<>(childEdges);
    }

    @Override
    public String toString() {
        List<String> edgeStrings = new ArrayList<>();
        for (Map.Entry<L, Integer> e : childEdges.entrySet()) {
            edgeStrings.add(label + "->" + e.getKey() + "(weight=" + e.getValue() + ")\n");
        }
        // four spaces here!
        String edgesString = String.join("    ", edgeStrings);
        return edgesString;
    }

}
