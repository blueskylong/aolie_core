package com.ranranx.aolie.application.sysconfig.model;

/**
 * 有向图, 摘自 https://www.tuicool.com/articles/ERfiym
 *
 * @author xxl
 * @version V0.0.1
 * @date 2021/2/20 0020 15:19
 **/

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

public final class DirectedGraph<T> implements Iterable<T> {

    private final Map<T, Set<T>> mGraphChildren = new HashMap<T, Set<T>>();
    private final Map<T, Set<T>> mGraphParent = new HashMap<T, Set<T>>();

    /**
     * Adds a new node to the graph. If the node already exists, this function is a no-op.
     *
     * @param node The node to add.
     * @return Whether or not the node was added.
     */
    public boolean addNode(T node) {
        /* If the node already exists, don't do anything. */
        if (mGraphChildren.containsKey(node)) {
            return false;
        }

        /* Otherwise, add the node with an empty set of outgoing edges. */
        mGraphChildren.put(node, new HashSet<T>());
        mGraphParent.put(node, new HashSet<T>());
        return true;
    }

    /**
     * Given a start node, and a destination, adds an arc from the start node to the destination. If an arc already exists, this operation is a no-op.
     * If either endpoint does not exist in the graph, throws a NoSuchElementException.
     *
     * @param start The start node.
     * @param dest  The destination node.
     * @throws NoSuchElementException If either the start or destination nodes do not exist.
     */
    public void addEdge(T start, T dest) {
        /* Confirm both endpoints exist. */
        if (!mGraphChildren.containsKey(start)) {
            throw new NoSuchElementException("The start node does not exist in the graph.");
        } else if (!mGraphChildren.containsKey(dest)) {
            throw new NoSuchElementException("The destination node does not exist in the graph.");
        }
        /* Add the edge. */
        mGraphChildren.get(start).add(dest);
        mGraphParent.get(dest).add(start);
    }

    /**
     * Removes the edge from start to dest from the graph. If the edge does not exist, this operation is a no-op. If either endpoint does not exist,
     * this throws a NoSuchElementException.
     *
     * @param start The start node.
     * @param dest  The destination node.
     * @throws NoSuchElementException If either node is not in the graph.
     */
    public void removeEdge(T start, T dest) {
        /* Confirm both endpoints exist. */
        if (!mGraphChildren.containsKey(start)) {
            throw new NoSuchElementException("The start node does not exist in the graph.");
        } else if (!mGraphChildren.containsKey(dest)) {
            throw new NoSuchElementException("The destination node does not exist in the graph.");
        }
        mGraphChildren.get(start).remove(dest);
        mGraphChildren.get(dest).remove(start);
    }

    /**
     * Given two nodes in the graph, returns whether there is an edge from the first node to the second node. If either node does not exist in the
     * graph, throws a NoSuchElementException.
     *
     * @param start The start node.
     * @param end   The destination node.
     * @return Whether there is an edge from start to end.
     * @throws NoSuchElementException If either endpoint does not exist.
     */
    public boolean edgeExists(T start, T end) {
        /* Confirm both endpoints exist. */
        if (!mGraphChildren.containsKey(start)) {
            throw new NoSuchElementException("The start node does not exist in the graph.");
        } else if (!mGraphChildren.containsKey(end)) {
            throw new NoSuchElementException("The end node does not exist in the graph.");
        }
        return mGraphChildren.get(start).contains(end);
    }

    /**
     * Given a node in the graph, returns an immutable view of the edges leaving that node as a set of endpoints.
     *
     * @param node The node whose edges should be queried.
     * @return An immutable view of the edges leaving that node.
     * @throws NoSuchElementException If the node does not exist.
     */
    public Set<T> edgesFrom(T node) {
        /* Check that the node exists. */
        Set<T> arcs = mGraphChildren.get(node);
        if (arcs == null) {
            throw new NoSuchElementException("Source node does not exist.");
        }
        return Collections.unmodifiableSet(arcs);
    }
    /**
     * Given a node in the graph, returns an immutable view of the edges from that node as a set of startpoints.
     *
     * @param node The node whose edges should be queried.
     * @return An immutable view of the edges leaving that node.
     * @throws NoSuchElementException If the node does not exist.
     */
    public Set<T> edgesTo(T node) {
        /* Check that the node exists. */
        Set<T> arcs = mGraphParent.get(node);
        if (arcs == null) {
            throw new NoSuchElementException("Source node does not exist.");
        }
        return Collections.unmodifiableSet(arcs);
    }

    /**
     * Returns an iterator that can traverse the nodes in the graph.
     *
     * @return An iterator that traverses the nodes in the graph.
     */
    @Override
    public Iterator<T> iterator() {
        return mGraphChildren.keySet().iterator();
    }

    /**
     * Returns the number of nodes in the graph.
     *
     * @return The number of nodes in the graph.
     */
    public int size() {
        return mGraphChildren.size();
    }

    /**
     * Returns whether the graph is empty.
     *
     * @return Whether the graph is empty.
     */
    public boolean isEmpty() {
        return mGraphChildren.isEmpty();
    }
}
