package graph;

import java.util.Objects;

/**
 * This class represents a single node of a graph.
 *
 * Specification Fields:
 * @spec.specfield data : String //Data of type String contained within this node
 *
 * Abstract Invariant:
 * The data stored in the node should not be null
 */

public class Node<T> {

     // Representation Invariant:
     // data != null and data is of type String

     // Abstraction Function:
     // AF(this) = a node n such that
     //            n.data = this.data


    private T data;

    /**
     * Constructor that creates a new node with specified data and empty edges
     *
     * @param data //data to be stored inside this node
     * @spec.requires data can not be null and data must be of type String.
     */

    public Node(T data) {
        if (data == null) throw new IllegalArgumentException("data can not be null");
        this.data = data;
        checkRep();
    }

    /**
     * A getter method to return the data contained in this node
     *
     * @return returns data stored in this node.
     */

    public T getData() {
        checkRep();
        return data;
    }

    /**
     * An equals method that returns true if this node contains same data
     * as the node being compared with
     * @param o //object to be compared with
     * @return true if o contains the same data as this.data (this node)
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node<?> node = (Node<?>) o;
        return data.equals(node.data);
    }

    /**
     * hashCode method to return the hashcode of this node
     * @return return hash code of this node
     */

    @Override
    public int hashCode() {
        return Objects.hash(getData());
    }

    /**
     * @throws RuntimeException //Throws an exception if representation invariant is violated
     */
    private void checkRep() throws RuntimeException {
        if (data == null) throw new RuntimeException("data can not be null");
    }
}
