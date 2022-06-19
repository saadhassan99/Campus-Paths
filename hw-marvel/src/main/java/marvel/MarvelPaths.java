package marvel;

import graph.Edge;
import graph.Graph;
import graph.Node;

import java.util.*;

public class MarvelPaths {

    public static Graph<String,String> buildGraph(String filename) {
        if (filename == null) {
            throw new IllegalArgumentException("filename can not be found");
        }

        Graph<String,String> marvelGraph = new Graph<>(false);
        HashSet<String> characters = new HashSet<>();
        HashMap<String, List<String>> books = new HashMap<>();

        MarvelParser.parseData(filename, characters, books);

        for (String hero: characters) {
            marvelGraph.addNode(new Node<>(hero));
        }

        for (String book : books.keySet()) {
            List<String> chars = books.get(book);
            int i = 1;
            for (String parent : chars) {
                List<String> children = chars.subList(i, chars.size());
                for (String child : children) {
                    if (!(parent.equals(child))) {
                        marvelGraph.addEdge(new Node<String>(parent), new Node<String>(child), book);
                        marvelGraph.addEdge(new Node<String>(child), new Node<String>(parent), book);
                    }
                }
                i++;
            }

        }
        System.out.println(marvelGraph);
        return marvelGraph;
    }

    /**
     * Finds the shortest path from one character to another character.
     *
     * @param marvelGraph: the graph used to find shortest path from start to end
     * @param start: start Node
     * @param dest: destination Node
     * @spec.requires graph != null and start != null and end != null
     *           start and end are both in marvelGraph
     * @return the shortest path from start to end, or null if
     *         no path exists from start to end
     */

    //Breadth First Search
    public static List<Edge<String,String>> BFS (Graph<String,String> marvelGraph, Node<String> start, Node<String> dest) {

        if (marvelGraph == null) throw new IllegalArgumentException("marvelGraph cannot be null");

        if (start == null || dest == null) throw new IllegalArgumentException("start and dest cannot be null");

        if (marvelGraph.containsNode(start) && marvelGraph.containsNode(dest)) {
            Queue<Node<String>> paths = new LinkedList<>();
            Map<Node<String>, List<Edge<String,String>>> nodeEdges = new HashMap<>();

            //put the start node in to the queue
            paths.add(start);
            // put the start Node and its corresponding empty set of edges into map
            nodeEdges.put(start, new ArrayList<>());
            while (!paths.isEmpty()) {
                Node<String> parent = paths.remove();
                //end Node found, return path
                if (parent.equals(dest)) {
                    List<Edge<String,String>> path = nodeEdges.get(parent);
                    return new ArrayList<>(path);
                }

                // end node not found yet
                HashSet<Edge<String,String>> edgeList = marvelGraph.getEdges(parent);
                // use comparator to get edge in alphabetical order
                // compare the childNode of edge first,
                // then compare the label of edge
                Set<Edge<String,String>> sortedEdges = new TreeSet<>((o1, o2) -> {
                    if (!(o1.getChildNode().equals(o2.getChildNode()))) {
                        return o1.getChildNode().getData().compareTo(o2.getChildNode().getData());
                    }
                    if (!(o1.getLabel().equals(o2.getLabel()))) {
                        return o1.getLabel().compareTo(o2.getLabel());
                    }
                    return 0;
                });

                sortedEdges.addAll(edgeList);
                // add childNodes of the Node currently pointing at
                // to the queue if it has not been checked

                for (Edge<String,String> e : sortedEdges) {
                    Node<String> child = e.getChildNode();
                    if (!nodeEdges.containsKey(child)) {
                        List<Edge<String,String>> path = nodeEdges.get(parent);
                        List<Edge<String,String>> newPath = new ArrayList<>(path);
                        newPath.add(e);
                        nodeEdges.put(child, newPath);
                        paths.add(child);
                    }
                }
            }
            // no path found from start Node to the dest Node.
            return null;
        }
        // start or end not in the graph
        return null;

    }

    /**
     * Allows user to type in two characters and find the
     * shortest path between two characters.
     *
     * @param args unused
     * @throws Exception if file cannot be found
     */
    public static void main(String[] args) throws Exception {
        Graph<String,String> marvelGraph = buildGraph("marvel.tsv");
        System.out.println("Find the shortest path between 2 Marvel characters");
        Scanner reader = new Scanner(System.in);
        boolean again = true;
        while (again) {
            System.out.println("Please type the starting character's name");
            String start = reader.nextLine();
            System.out.println("Please type the ending character's name");
            String end = reader.nextLine();
            if (!marvelGraph.containsNode(new Node<>(start)) ||
                    !marvelGraph.containsNode(new Node<>(end))) {
                System.out.println(start + "is an unknown character");
                System.out.println(end + "is an unknown character");
            } else if (!marvelGraph.containsNode(new Node<>(start))) {
                System.out.println(start + "is an unknown character");
            } else if (!marvelGraph.containsNode(new Node<>(end))) {
                System.out.println(end + "is an unknown character");
            } else {
                Node<String> current = new Node<String>(start);
                StringBuilder result = new StringBuilder("path from " + start + " to " + end + ":");
                List<Edge<String,String>> path = BFS(marvelGraph, new Node<>(start), new Node<>(end));
                if (path == null) {
                    result.append("\n" + "no path found");
                } else {
                    for (Edge<String,String> e: path) {
                        result.append("\n").append(current.getData()).append(" to ").append(e.getChildNode().getData()).append(" via ").append(e.getLabel());
                        current = e.getChildNode();
                    }
                }
                System.out.println(result);
            }
            System.out.println("Again?");
            String answer = reader.nextLine();
            answer = answer.toLowerCase();
            if (answer.length() == 0 || answer.charAt(0) != 'y') {
                again = false;
            }
        }
        System.out.println("Bye!");
        reader.close();
    }
}
