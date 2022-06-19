/*
 * Copyright (C) 2020 Kevin Zatloukal.  All rights reserved.  Permission is
 * hereby granted to students registered for University of Washington
 * CSE 331 for use solely during Autumn Quarter 2020 for purposes of
 * the course.  No other use, copying, distribution, or modification
 * is permitted without prior written consent. Copyrights for
 * third-party components of this work must be honored.  Instructors
 * interested in reusing these course materials should contact the
 * author.
 */

package pathfinder.scriptTestRunner;

import graph.Edge;
import graph.Graph;
import graph.Node;
import pathfinder.CampusPaths;

import java.io.*;
import java.util.*;

/**
 * This class implements a test driver that uses a script file format
 * to test an implementation of Dijkstra's algorithm on a graph.
 */
public class PathfinderTestDriver {

    public static void main(String[] args) {
        // You only need a main() method if you choose to implement
        // the 'interactive' test driver, as seen with GraphTestDriver's sample
        // code. You may also delete this method entirely and just
    }

    /** String -> Graph: maps the names of graphs to the actual graph **/
    private final Map<String, Graph<String,Double>> graphs = new HashMap<>();
    private final PrintWriter output;
    private final BufferedReader input;

    // Leave this constructor public
    public PathfinderTestDriver(Reader r, Writer w) {
        // TODO: Implement this, reading commands from `r` and writing output to `w`.
        // See GraphTestDriver as an example.
        input = new BufferedReader(r);
        output = new PrintWriter(w);
    }

    // Leave this method public
    public void runTests() throws IOException {
        // TODO: Implement this.
        String inputLine;
        while ((inputLine = input.readLine()) != null) {
            if ((inputLine.trim().length() == 0) ||
                    (inputLine.charAt(0) == '#')) {
                // echo blank and comment lines
                output.println(inputLine);
            }
            else
            {
                // separate the input line on white space
                StringTokenizer st = new StringTokenizer(inputLine);
                if (st.hasMoreTokens()) {
                    String command = st.nextToken();

                    List<String> arguments = new ArrayList<String>();
                    while (st.hasMoreTokens()) {
                        arguments.add(st.nextToken());
                    }

                    executeCommand(command, arguments);
                }
            }
            output.flush();
        }
    }

    private void executeCommand(String command, List<String> arguments) {
        try {
            switch (command) {
                case "CreateGraph":
                    createGraph(arguments);
                    break;
                case "AddNode":
                    addNode(arguments);
                    break;
                case "AddEdge":
                    addEdge(arguments);
                    break;
                case "ListNodes":
                    listNodes(arguments);
                    break;
                case "ListChildren":
                    listChildren(arguments);
                    break;
                case "FindPath":
                    findPath(arguments);
                    break;
                default:
                    output.println("Unrecognized command: " + command);
                    break;
            }
        } catch (Exception e) {
            output.println("Exception: " + e.toString());
        }
    }

    private void createGraph(List<String> arguments) {
        if (arguments.size() != 1) {
            throw new CommandException ("Bad arguments to CreateGraph: " + arguments);
        }

        String graphName = arguments.get(0);
        createGraph(graphName);
    }

    private void createGraph(String graphName) {
        graphs.put(graphName, new Graph<>(false));
        output.println("created graph " + graphName);
    }

    private void addNode(List<String> arguments) {
        if (arguments.size() != 2) {
            throw new CommandException("Bad arguments to addNode: " + arguments);
        }

        String graphName = arguments.get(0);
        String nodeName = arguments.get(1);

        addNode(graphName, nodeName);
    }

    private void addNode(String graphName, String nodeName) {
        Graph<String,Double> g = graphs.get(graphName);
        g.addNode(new Node<>(nodeName));
        output.println("added node " + nodeName + " to " + graphName);
    }

    private void addEdge(List<String> arguments) {
        if (arguments.size() != 4) {
            throw new CommandException("Bad arguments to addEdge: " + arguments);
        }

        String graphName = arguments.get(0);
        String parentName = arguments.get(1);
        String childName = arguments.get(2);
        double edgeLabel = Double.parseDouble(arguments.get(3));
        addEdge(graphName, parentName, childName, edgeLabel);
    }

    private void addEdge(String graphName, String parentName, String childName,
                         Double edgeLabel) {
        Graph<String,Double> g = graphs.get(graphName);
        g.addEdge(new Node<>(parentName), new Node<>(childName), edgeLabel);
        output.println("added edge " + String.format("%.3f",edgeLabel) + " from " + parentName +
                " to " + childName + " in " + graphName);
    }

    private void listNodes(List<String> arguments) {
        if (arguments.size() != 1) {
            throw new CommandException("Bad arguments to listNodes: " + arguments);
        }

        String graphName = arguments.get(0);
        listNodes(graphName);
    }

    private void listNodes(String graphName) {
        Graph<String,Double> g = graphs.get(graphName);
        StringBuilder result = new StringBuilder(graphName + " contains:");
        TreeSet<Node<String>> sortNodes =
                new TreeSet<>((n1, n2) -> {
                    if (!n1.getData().equals(n2.getData())) {
                        return n1.getData().compareTo(n2.getData());
                    }
                    return 0;
                });
        sortNodes.addAll(g.getNodes());
        for (Node<String> n: sortNodes) {
            result.append(" ").append(n.getData());
        }
        output.println(result);
    }

    private void listChildren(List<String> arguments) {
        if (arguments.size() != 2) {
            throw new CommandException("Bad arguments to listChildren: " + arguments);
        }
        String graphName = arguments.get(0);
        String parentName = arguments.get(1);
        listChildren(graphName, parentName);
    }

    private void listChildren(String graphName, String parentName) {
        Graph<String,Double> g = graphs.get(graphName);
        StringBuilder result = new StringBuilder("the children of " + parentName + " in " + graphName + " are:");
        TreeSet<Edge<String,Double>> sortEdges =
                new TreeSet<>((e1, e2) -> {
                    if (!(e1.getChildNode().equals(e2.getChildNode()))) {
                        return e1.getChildNode().getData().compareTo(e2.getChildNode().getData());
                    }
                    if (!(e1.getLabel().equals(e2.getLabel()))) {
                        return e1.getLabel().compareTo(e2.getLabel());
                    }
                    return 0;
                });
        HashSet<Edge<String,Double>> edges = g.getEdges(new Node<>(parentName));
        sortEdges.addAll(edges);
        for (Edge<String,Double> e: sortEdges) {
            result.append(" ").append(e.getChildNode().getData()).append("(").append(String.format("%.3f", e.getLabel())).append(")");
        }
        output.println(result);
    }

    private void findPath(List<String> arguments) {
        if (arguments.size() != 3) {
            throw new CommandException("Bad arguments to findPath: " + arguments);
        }
        String graphName = arguments.get(0);
        String node1Name = arguments.get(1).replace('_', ' ');
        String node2Name = arguments.get(2).replace('_', ' ');
        findPath(graphName, node1Name, node2Name);
    }

    private void findPath(String graphName, String start, String end) {
        Graph<String, Double> g = graphs.get(graphName);
        List<Edge<String, Double>> path = CampusPaths.dijistra(new Node<>(start), new Node<>(end), g);
        StringBuilder result = new StringBuilder();
        if (!g.containsNode(new Node<>(start))
                && !g.containsNode(new Node<>(end))) {
            result.append("unknown character ").append(start);
            result.append("\n" + "unknown character ").append(end);
        } else if (!g.containsNode(new Node<>(start))) {
            result.append("unknown character ").append(start);
        } else if (!g.containsNode(new Node<>(end))) {
            result.append("unknown character ").append(end);
        } else {
            result = new StringBuilder("path from " + start + " to " + end + ":");
            Node<String> current = new Node<>(start);
            if (path == null) {
                result.append("\n" + "no path found");
            } else {
                double totalCost = 0;
                for (Edge<String,Double> e: path) {
                    if (!e.getChildNode().equals(current)) {
                        result.append("\n").append(current.getData()).append(" to ").append(e.getChildNode().getData()).append(" with weight ").append(String.format("%.3f", e.getLabel()));
                        totalCost += e.getLabel();
                        current = e.getChildNode();
                    }
                }
                result.append("\n" + "total cost: ").append(String.format("%.3f", totalCost));
            }
        }
        output.println(result);
    }

    /**
     * This exception results when the input file cannot be parsed properly
     **/
    static class CommandException extends RuntimeException {

        public CommandException(String s) {
            super(s);
        }

        public static final long serialVersionUID = 3495;
    }
}
