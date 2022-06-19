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

package pathfinder;

import graph.Edge;
import graph.Graph;
import graph.Node;
import pathfinder.datastructures.Path;
import pathfinder.datastructures.Point;
import pathfinder.parser.CampusBuilding;
import pathfinder.parser.CampusPath;
import pathfinder.parser.CampusPathsParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CampusMap implements ModelAPI {

    List<CampusBuilding> campusBuildings;
    List<CampusPath> campusPaths;

    // a map of building's names that maps
    // building's full name to abbreviated name
    Map<String, String> shortMap;

    // a map of building's names that maps
    // building's abbreviated name to full name
    Map<String, String> fullMap;

    // a graph contains campus paths
    Graph<Point, Double> graph;

    // a map of building's locations that maps
    // building's abbreviated name to its location
    Map<String, Point> locMap;

    public CampusMap() {
        campusBuildings = CampusPathsParser.parseCampusBuildings("campus_buildings.tsv");
        campusPaths = CampusPathsParser.parseCampusPaths("campus_paths.tsv");

        shortMap = new HashMap<>();
        fullMap = new HashMap<>();
        locMap = new HashMap<>();

        graph = new Graph<>(false);

        for (CampusBuilding campusBuilding: campusBuildings) {
            shortMap.put(campusBuilding.getLongName(), campusBuilding.getShortName());
            fullMap.put(campusBuilding.getShortName(), campusBuilding.getLongName());
            locMap.put(campusBuilding.getShortName(), new Point(campusBuilding.getX(), campusBuilding.getY()));
        }

        for (CampusPath campusPath: campusPaths) {
            Point startPoint = new Point(campusPath.getX1(), campusPath.getY1());
            Point endPoint = new Point(campusPath.getX2(), campusPath.getY2());

            graph.addEdge(new Node<>(startPoint), new Node<>(endPoint), campusPath.getDistance());
        }

    }

    @Override
    public boolean shortNameExists(String shortName) {
        // TODO: Implement this method exactly as it is specified in ModelAPI
        // throw new RuntimeException("Not Implemented Yet")
        return fullMap.containsKey(shortName);
    }

    @Override
    public String longNameForShort(String shortName) {
        // TODO: Implement this method exactly as it is specified in ModelAPI
        //throw new RuntimeException("Not Implemented Yet");
        return shortMap.get(shortName);
    }

    @Override
    public Map<String, String> buildingNames() {
        // TODO: Implement this method exactly as it is specified in ModelAPI
        //throw new RuntimeException("Not Implemented Yet");
        return fullMap;
    }

    @Override
    public Path<Point> findShortestPath(String startShortName, String endShortName) {
        // TODO: Implement this method exactly as it is specified in ModelAPI
        // throw new RuntimeException("Not Implemented Yet");
        Node<Point> startNode = new Node<>(locMap.get(startShortName));
        Node<Point> endNode = new Node<>(locMap.get(endShortName));

        List<Edge<Point, Double>> route = CampusPaths.dijistra(startNode, endNode, graph);

        if (route == null) {
            return null;
        }

        Path<Point> shortestPath = new Path<>(locMap.get(startShortName));

        for (Edge<Point, Double> path: route) {
            shortestPath = shortestPath.extend(path.getChildNode().getData(), path.getLabel());
        }

        return shortestPath;
    }

}
