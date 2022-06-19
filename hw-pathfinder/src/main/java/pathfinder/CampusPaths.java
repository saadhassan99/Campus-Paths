package pathfinder;

import graph.Edge;
import graph.Graph;
import graph.Node;

import java.util.*;

public class CampusPaths {

    /**
     * Finds the minimum-cost path from one character to another character.
     * @param g: the graph used to find shortest path from start to end
     * @param start: a character
     * @param end: another character
     * @param <T>: generic type
     * @spec.requires graph != null and start != null and end != null
     * @return the minimum-cost path from start to end, or null if
     * no path exists from start to end or start or end not in graph
     * @throws IllegalArgumentException if either start or end is
     * not in the graph
     */

    public static <T> List<Edge<T, Double>> dijistra (Node<T> start, Node<T> end, Graph<T, Double> g) {
        if (g == null) {
            throw new IllegalArgumentException("graph cannot be null.");
        }
        if (start == null || end == null) {
            throw new IllegalArgumentException("start and end cannot be null.");
        }
        if (g.containsNode(start) && g.containsNode(end)) {

            PriorityQueue<List<Edge<T, Double>>> active = new PriorityQueue<>((o1, o2) -> {
                double cost1 = 0;
                double cost2 = 0;
                for (Edge<T, Double> e1 : o1) {
                    cost1 += e1.getLabel();
                }
                for (Edge<T, Double> e2 : o2) {
                    cost2 += e2.getLabel();
                }
                int num = 0;
                if (cost1 - cost2 > 0) {
                    num = 1;
                } else if (cost1 - cost2 < 0) {
                    num = -1;
                }
                return num;
            });

            // finishes contains Nodes for which we have checked
            // the minimum-cost path from start

            Set<Node<T>> finished = new HashSet<>();

            // put the start Node into queue and a path with cost of 0.0
            // because there is no edge in the path
            List<Edge<T, Double>> selfPath = new ArrayList<>();
            selfPath.add(new Edge<>(start, 0.0));
            active.add(selfPath);

            while (!active.isEmpty()) {
                // minPath is the lowest-cost path in active and,
                // if minDest isn't already 'finished,' is the
                // minimum-cost path to the node minDest
                List<Edge<T, Double>> minPath = active.poll();
                // minDest is the end Node of minPath
                Node<T> minDest = minPath.get(minPath.size() - 1).getChildNode();
                // return minPath if the end Node of minPath
                // is equal to end passed in by client
                if (minDest.equals(end)) {
                    return minPath;

                } else if (!finished.contains(minDest)) {
                    for (Edge<T, Double> e: g.getEdges(minDest)) {
                        if (!finished.contains(e.getChildNode())) {
                            List<Edge<T, Double>> newPath = new ArrayList<>(minPath);
                            newPath.add(e);
                            active.add(newPath);
                        }
                    }
                    // add minDest to finished after checking it
                    finished.add(minDest);
                }
            }
            // no path exist from start to end
            return null;
        }
        // start or end not in graph
        return null;
    }
}
