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

package marvel.scriptTestRunner;

import graph.Edge;
import graph.Graph;
import graph.Node;
import marvel.MarvelPaths;

import java.io.*;
import java.util.*;

/**
 * This class implements a testing driver which reads test scripts from
 * files for testing Graph, the Marvel parser, and your BFS algorithm.
 */
public class MarvelTestDriver {

    public static void main(String[] args) {
        // You only need a main() method if you choose to implement
        // the 'interactive' test driver, as seen with GraphTestDriver's sample
        // code. You may also delete this method entirely if you don't want to
        // use the interactive test driver.
        try {
            if (args.length > 1) {
                printUsage();
                return;
            }

            MarvelTestDriver mtd;

            if (args.length == 0) {
                mtd = new MarvelTestDriver(new InputStreamReader(System.in),
                        new OutputStreamWriter(System.out));
            } else {

                String fileName = args[0];
                File tests = new File (fileName);

                if (tests.exists() || tests.canRead()) {
                    mtd = new MarvelTestDriver(new FileReader(tests),
                            new OutputStreamWriter(System.out));
                } else {
                    System.err.println("Cannot read from " + tests.toString());
                    printUsage();
                    return;
                }
            }

            mtd.runTests();

        } catch (IOException e) {
            System.err.println(e.toString());
            e.printStackTrace(System.err);
        }
    }

    private static void printUsage() {
        System.err.println("Usage:");
        System.err.println("to read from a file: java hw-marvel.test.MarvelTestDriver <name of input script>");
        System.err.println("to read from standard in: java hw-marvel.test.MarvelTestDriver");
    }

    /** String -> Graph: maps the names of graphs to the actual graph **/
    private final Map<String, Graph<String,String>> graphs = new HashMap<>();
    private final PrintWriter output;
    private final BufferedReader input;

    // Leave this constructor public
    public MarvelTestDriver(Reader r, Writer w) {
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
                case "LoadGraph":
                    loadGraph(arguments);
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
            throw new CommandException("Bad arguments to CreateGraph: " + arguments);
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
        Graph<String,String> g = graphs.get(graphName);
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
        String edgeLabel = arguments.get(3);

        addEdge(graphName, parentName, childName, edgeLabel);
    }

    private void addEdge(String graphName, String parentName, String childName,
                         String edgeLabel) {
        Graph<String,String> g = graphs.get(graphName);
        g.addEdge(new Node<>(parentName), new Node<>(childName), edgeLabel);
        output.println("added edge " + edgeLabel + " from " + parentName +
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
        Graph<String,String> g = graphs.get(graphName);
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
        Graph<String,String> g = graphs.get(graphName);
        StringBuilder result = new StringBuilder("the children of " + parentName + " in " + graphName + " are:");
        TreeSet<Edge<String,String>> sortEdges =
                new TreeSet<>((e1, e2) -> {
                    if (!(e1.getChildNode().equals(e2.getChildNode()))) {
                        return e1.getChildNode().getData().compareTo(e2.getChildNode().getData());
                    }
                    if (!(e1.getLabel().equals(e2.getLabel()))) {
                        return e1.getLabel().compareTo(e2.getLabel());
                    }
                    return 0;
                });
        HashSet<Edge<String,String>> edges = g.getEdges(new Node<>(parentName));
        sortEdges.addAll(edges);
        for (Edge<String,String> e: sortEdges) {
            result.append(" ").append(e.getChildNode().getData()).append("(").append(e.getLabel()).append(")");
        }
        output.println(result);
    }

    private void loadGraph(List<String> arguments) throws Exception {
        if (arguments.size() != 2) {
            throw new CommandException("Bad arguments to loadGraph: " + arguments);
        }
        String graphName = arguments.get(0);
        String filename = arguments.get(1);
        loadGraph(graphName, filename);
    }

    private void loadGraph(String graphName, String filename) throws Exception {
        //filename = "hw-marvel/src/main/resources/data/" + filename;
        graphs.put(graphName, MarvelPaths.buildGraph(filename));
        System.out.println(graphs);
        output.println("loaded graph " + graphName);

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
        Graph<String,String> g = graphs.get(graphName);
        List<Edge<String,String>> path = MarvelPaths.BFS(g, new Node<>(start), new Node<>(end));
        String result = "";
        if (!g.containsNode(new Node<>(start))
                && !g.containsNode(new Node<>(end))) {
            result += "unknown character " + start;
            result += "\n" + "unknown character " + end;
        } else if (!g.containsNode(new Node<>(start))) {
            result += "unknown character " + start;
        } else if (!g.containsNode(new Node<>(end))) {
            result += "unknown character " + end;
        } else {
            result = "path from " + start + " to " + end + ":";
            Node<String> current = new Node<>(start);
            if (path == null) {
                result += "\n" + "no path found";
            } else {
                for (Edge<String,String> e: path) {
                    result += "\n" + current.getData() + " to " +
                            e.getChildNode().getData() + " via " + e.getLabel();
                    current = e.getChildNode();
                }
            }
        }
        output.println(result);
    }

    /**
     * This exception results when the input file cannot be parsed properly
     **/
    static class CommandException extends RuntimeException {

        public CommandException() {
            super();
        }
        public CommandException(String s) {
            super(s);
        }

        public static final long serialVersionUID = 3495;
    }
}
