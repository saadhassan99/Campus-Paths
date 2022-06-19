package graph;

import java.util.Objects;

/**
 * This class represents the edge with a string label value within a graph
 * along the childnode connected to this edge.
 */

public class Edge<T,E> {

    // Rep invariant:
    // childNode != null && label != null

    // Abstract function:
    // AF(this) = a labeled edge without origin, arg, such that
    //            arg.destination = this.dest
    //            arg.label = this.label

    private final Node<T> childNode;
    private final E label;

    /**
     * creates a labeled edge connected to a childNode
     *
     * @param childNode the node this edge is pointing to
     * @param label the label of the edge
     *
     * @spec.requires childNode != null and label != null
     * @spec.effects constructs a labeled edge with <var>childNode</var> as destination
     * and label <var>label</var>
     */

    public Edge(Node<T> childNode, E label) {
        this.childNode = childNode;
        this.label = label;
        checkRep();
    }

    /**
     * returns the child node this edge is connected to
     * @return childNode of this edge
     */

    public Node<T> getChildNode() {
        return childNode;
    }

    /**
     * return the String value as label of this edge
     * @return label of this edge
     */

    public E getLabel() {
        return label;
    }

    /**
     *
     * @param o The edge being compared
     * @return boolean, true if the two edges are same, false otherwise
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge<?,?> edges = (Edge<?,?>) o;
        return childNode.equals(edges.childNode) &&
                label.equals(edges.label);
    }

    /**
     * returns the hashcode of this edge
     * @return hashcode of this edge
     */

    @Override
    public int hashCode() {
        return Objects.hash(childNode, label);
    }

    /**
     * Method to check if the representation invariant holds
     * @throws RuntimeException if representation invariant is violated
     */

    private void checkRep() throws RuntimeException {
        if (childNode == null) throw new RuntimeException("child node can not be null");
        if (label == null) throw new RuntimeException("label can not be null");
    }

}
