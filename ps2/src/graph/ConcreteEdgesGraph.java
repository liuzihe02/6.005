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
 * A valid implementation of Graph, using only
 * 
 * vertices which contain the labels of all the unique vertices
 * edges which contain all the edges, and is unordered
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
    // vertices and edges, so return defensive copies of them
    // to prevent rep exposure

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
        // doesnt contain and we want to add it
        if (!vertices.contains(vertex)) {
            vertices.add(vertex);
            return true;
        }
        // contains and we dont do anything
        else {
            return false;
        }
    }

    @Override
    public int set(String source, String target, int weight) {
        // weight is nonzero
        if (weight != 0) {
            // first add both source and target
            add(source);
            add(target);
            // check if this edge already exists

            for (Edge e : edges) {
                // the edge exists
                if (e.getSource().equals(source) && e.getTarget().equals(target)) {
                    // update this weight
                    int oldWeight = e.getWeight();
                    e.setWeight(weight);
                    return oldWeight;
                }
            }
            ;
            // if we get here, weight does not currently exist
            edges.add(new Edge(source, target, weight));
            return 0;
        }
        // weight is zero, we need to remove this weight
        // YOU CANNOT MODIFY A COLLECTION WHILE ITERATING OVER IT
        else {
            // try to find this weight to remove
            Iterator<Edge> iterator = edges.iterator();
            while (iterator.hasNext()) {
                Edge e = iterator.next();
                if (e.getSource().equals(source) && e.getTarget().equals(target)) {
                    int oldWeight = e.getWeight();
                    iterator.remove();
                    return oldWeight;
                }
            }
            // could not find weight to remove
            return 0;
        }
    }

    @Override
    public boolean remove(String vertex) {
        // first determine if vertex is in our set
        if (vertices.contains(vertex)) {
            // first remove vertex from the set, we can do this directly due to hashing
            vertices.remove(vertex);
            // next remove all mentions from our list of edges. cannot use for loop for this
            // this is an ArrayList method
            edges.removeIf(e -> e.getTarget().equals(vertex) || e.getSource().equals(vertex));
            ;
            return true;
        }
        // vertex not in our set
        else {
            return false;
        }
    }

    @Override
    public Set<String> vertices() {
        // defensive copying, also note that vertices is private!
        return new HashSet<>(vertices);
    }

    @Override
    public Map<String, Integer> sources(String target) {
        Map<String, Integer> sourceMap = new HashMap<>();

        // get all the sources for this target
        for (Edge e : edges) {
            // found the target
            if (e.getTarget().equals(target)) {
                sourceMap.put(e.getSource(), e.getWeight());
            }
        }
        ;
        return sourceMap;

    }

    @Override
    public Map<String, Integer> targets(String source) {
        Map<String, Integer> targetMap = new HashMap<>();

        // get all the sources for this target
        for (Edge e : edges) {
            // found the source
            if (e.getSource().equals(source)) {
                targetMap.put(e.getTarget(), e.getWeight());
            }
        }
        ;
        return targetMap;
    }

    // toString(), we override Object.toString() always!
    @Override
    public String toString() {
        List<String> edgeStrings = new ArrayList<>();
        for (Edge e : edges) {
            edgeStrings.add(e.getSource() + "->" + e.getTarget() + "(weight=" + e.getWeight() + ")\n");
        }
        String edgesString = String.join("    ", edgeStrings);
        return edgesString;
    }

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
    // we can reassign this weight
    private Integer weight;

    // Abstraction function:
    // AF(source, target, weight) = an edge in a directed graph with a
    // source and target vertices and weight

    // Representation invariant:
    // weight>0

    // Safety from rep exposure:
    // all fields are private and final
    // source and target are immutable types, while weight is Integer, so all fields
    // are immutable; can return directly!
    // ALL METHODS HERE ARE PACKAGE-PRIVATE (DEFAULT ACCESS)

    // constructor
    Edge(String source, String target, Integer weight) {
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
    String getSource() {
        return source;
    }

    String getTarget() {
        return target;
    }

    Integer getWeight() {
        return weight;
    }

    // set the weight field to a new Integer
    void setWeight(Integer newWeight) {
        this.weight = newWeight;
    }

    // toString(), we override Object.toString()
    @Override
    public String toString() {
        // automatically converts Integer to string representation
        return source + "->" + target + "(weight=" + weight + ")";
    }

}
