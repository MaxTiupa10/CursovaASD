package org.example.Objects;
import java.util.*;

public class Graph {
    private Set<Integer> vertices;
    private Map<Integer, Map<Integer, Integer>> edges;

    public Graph() {
        this.vertices = new HashSet<>();
        this.edges = new HashMap<>();
    }

    public void addVertex(int vertex) {
        vertices.add(vertex);
        edges.putIfAbsent(vertex, new HashMap<>());
    }

    public void addEdge(int vertex1, int vertex2, int weight) {
        addVertex(vertex1);
        addVertex(vertex2);
        edges.get(vertex1).put(vertex2, weight);
        edges.get(vertex2).put(vertex1, weight);
    }

    public Set<Integer> getVertices() {
        return vertices;
    }

    public Map<Integer, Map<Integer, Integer>> getEdges() {
        return edges;
    }

    public Set<Integer> getNeighbors(int vertex) {
        Map<Integer, ?> neighborsMap = edges.get(vertex);
        return (neighborsMap != null) ? neighborsMap.keySet() : Collections.emptySet();
    }

    public Map<Integer, Integer> getEdges(int vertex) {
        return edges.get(vertex);
    }


    public void displayGraph() {
        System.out.println("Vertices: " + vertices);
        System.out.println("Edges:");

        for (int vertex : edges.keySet()) {
            System.out.print(vertex + ": ");
            Map<Integer, Integer> vertexEdges = edges.get(vertex);
            for (Map.Entry<Integer, Integer> entry : vertexEdges.entrySet()) {
                System.out.print("(" + entry.getKey() + ", " + entry.getValue() + ") ");
            }
            System.out.println();
        }
    }
}