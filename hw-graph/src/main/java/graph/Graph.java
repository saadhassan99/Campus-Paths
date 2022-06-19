package graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This is the top level class of our graph ADT. It represents a an object with
 * nodes and labeled edges
 * A Node is an Object with a data stored in it
 * An Edge is an object with a label stored in it and pointing to a child Node
 */

public class Graph<T,E> {

    // Rep invariant:
    //     graph != null
    //     The graph does not contain any node or edge of null type.
    //     The graph must contain Node n, if n is attached to any edge in the graph.

    // Abstract function:
    //    AF(this) = directed graph g such that
    //    {} g is an empty graph
    //    All Nodes in this = this.graph.keySet()
    //    All Edges starting from Node n = this.graph.getKey(n)

    private final Map<Node<T>, HashSet<Edge<T,E>>> graph;
    boolean checkRep;

    /**
     * Constructor that initiates a new graph with no nodes or edges.
     *
     * @spec.effects creates an empty graph
     * @param checkRep boolean flag, if true will turn on the checkrep, false otherwise
     */
    public Graph(boolean checkRep) {
        this.graph = new HashMap<>();
        this.checkRep = checkRep;
        checkRep();
    }

    /**
     * Add a node to the graph and return true if the node gets added
     * successfully, otherwise return false
     *
     * @param data: node to be added to the graph
     * @return boolean: returns true if node is successfully added. false otherwiese
     * @spec.requires data is not null
     * @spec.modifies this
     * @spec.effects adds a new node to the graph if it is not already in the graph
     */

    public boolean addNode(Node<T> data) {
        if (data == null) throw new IllegalArgumentException("data can not be null");

        if (!graph.containsKey(data)) {
            graph.put(data, new HashSet<>());
            checkRep();
            return true;
        }
        return false;
    }

    /**
     * Create an edge between two nodes in the graph with a String label if the
     * parent node is already in the graph. Otherwise create the parent node and
     * then create the edge between the parent node and child node.
     *
     * @param parent the parent node where the edge will begin
     * @param child  the child node to which parent node will be connect through the edge
     * @param label  the label of the edge being added
     * @spec.requires start, child and label != null
     * @spec.effects adds a new edge to the graph that connects <var>parent</var> to <var>child</var>
     * if either is null, then <code>throw new IllegalArgumentException()</code>
     * @return boolean: returns true is the edge is successfully added. false otherwise
     */

    public boolean addEdge(Node<T> parent, Node<T> child, E label) {
        if (parent == null || child == null) {
            throw new IllegalArgumentException("Node can not be null");
        }
        if (label == null) {
            throw new IllegalArgumentException("label can not be null");
        }
        //add the parent node to the graph
        addNode(parent);
        //create the new edge with the child node
        Edge<T,E> newEdge = new Edge<>(child, label);
        //add the new edge to the graph if this edge doesnt already exist
        if (!graph.get(parent).contains(newEdge)) {
            HashSet<Edge<T,E>> edges = graph.get(parent);
            edges.add(newEdge);
            checkRep();
            return true;
        }
        checkRep();
        return false;

    }

    /**
     * Remove a given node n and all edges related to it. Return true if the node
     * can be removed and false other wise
     *
     * @param n the node 'n' that needs to be removed from the graph
     * @return boolean: true if specified node successfully gets removed, false otherwise
     * @spec.requires n != null
     * @spec.modifies this
     * @spec.effects delete n from the graph if it is in the graph
     */

    public boolean removeNode(Node<T> n) {
        if (n == null) throw new IllegalArgumentException("n can not be null");
        if (graph.containsKey(n)) {
            graph.keySet().remove(n);
            for (Node<T> x: graph.keySet()) {
                for (Edge<T,E> e: graph.get(x)) {
                    if (e.getChildNode().equals(n)) {
                        removeEdge(x, e.getChildNode(), e.getLabel());
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Remove an edge along with its label starting from parent node and
     * ending at child node, if the edge exists.
     *
     * @param parent starting node where the edge begins
     * @param child  ending node where the edge ends
     * @param label  the label of the edge to be removed
     * @return boolean: true if the edge successfully gets removed, false otherwise
     * @spec.modifies this
     * @spec.requires parent != null, child != null, and label != null
     * @spec.effects remove the edge from the graph indicated by the label, starting node and ending node.
     */

    public boolean removeEdge(Node<T> parent, Node<T> child, E label) {
        if (parent == null || child == null) {
            throw new IllegalArgumentException("node can not be null");
        }
        if (label == null) {
            throw new IllegalArgumentException("label can not be null");
        }
        if (!graph.containsKey(parent)) {
            return false;
        }
        Edge<T,E> edge = new Edge<>(child, label);
        if (graph.get(parent).contains(edge)) {
            HashSet<Edge<T,E>> edges = graph.get(parent);
            edges.remove(edge);
            //checkRep();
            return true;
        }
        checkRep();
        return false;
    }

    /**
     * returns the entire graph
     *
     * @return this
     */

    public Map<Node<T>, HashSet<Edge<T,E>>> getGraph() {
        return graph;
    }

    /**
     * Return the set of all node this
     *
     * @return set of nodes in this
     */

    public Set<Node<T>> getNodes() {
        checkRep();
        return new HashSet<>(graph.keySet());
    }

    /**
     * Given a Node n, get a set of all edges connected to it
     *
     * @param n Node n to get the edges from
     * @return returns a set of edges for <var>n</var>
     * @throws IllegalArgumentException if n is null or not in the graph
     */

    public HashSet<Edge<T,E>> getEdges(Node<T> n) {
        checkRep();
        return new HashSet<>(graph.get(n));
    }

    /**
     * Return data stored in a given Node
     *  @param n Node to get data of
     *  @spec.requires n is not null and is in the Graph
     *  @return String: the data stored in n
     */
    public E getData(Node<E> n) {
        if (n == null) {
            throw new IllegalArgumentException("Node cannot be null");
        }
        if (!graph.containsKey(n)) {
            throw new IllegalArgumentException("Node is not in the graph");
        }
        return n.getData();
    }

    /**
     * Given a Node n, return all of its children nodes
     *
     * @param n, a node
     * @return a set of all child nodes of Node <var>n</var>
     * @throws IllegalArgumentException if the Node <var>n</var> is not in this.nodes
     * @spec.requires n != null
     */

    public HashSet<Node<T>> getChildren(Node<T> n) {
        if (n == null) throw new IllegalArgumentException("n can not be null");
        if (!graph.containsKey(n)) throw new IllegalArgumentException("n is not in the graph");
        HashSet<Node<T>> set = new HashSet<>();
        for (Edge<T,E> e: graph.get(n)) {
            set.add(e.getChildNode());
        }
        return set;

    }

    /**
     * Returns true is the graph is empty
     *
     * @return boolean: true is graph is empty, false otherwise
     */

    public boolean isEmpty() {
        return graph.isEmpty();
    }

    /**
     * Returns true if Node <var>n</var> is in the graph
     *
     * @param n the Node to find in the graph
     * @return boolean: true if Node <var>n</var> is in the graph
     * @throws IllegalArgumentException if the Node <var>n</var> is null.
     */
    public boolean containsNode(Node<T> n) {
        if (n == null) throw new IllegalArgumentException("n cannot be null");
        return graph.containsKey(n);
    }

    /**
     * returns the number of nodes in the graph
     *
     * @return number of nodes in the graph
     */

    public int size() {
        checkRep();
        return graph.size();
    }

    /**
     * check if the representation invariant holds
     *
     * @throws RuntimeException if the representation invariant violates
     */


    private void checkRep() throws RuntimeException {
        if (checkRep) {
            if (graph == null) throw new RuntimeException("Graph cannot be null");

            for (Node<T> n : graph.keySet()) {
                if (n == null) {
                    throw new RuntimeException("node cannot be null");
                }
                if (graph.get(n) == null) {
                    throw new RuntimeException("Edge set can not be null");
                }
                for (Edge<T,E> e : graph.get(n)) {
                    if (e == null) throw new RuntimeException("edge cannot be null");
                }
            }
        }
    }
}
