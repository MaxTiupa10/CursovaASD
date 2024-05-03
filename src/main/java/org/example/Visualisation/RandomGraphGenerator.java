package org.example.Visualisation;
import org.example.Objects.*;
import java.util.Random;

public class RandomGraphGenerator {

    public static Graph generateRandomGraph(int numVertices, int maxWeight) {
        Graph randomGraph = new Graph();
        Random random = new Random();

        // Add vertices
        for (int i = 1; i <= numVertices; i++) {
            randomGraph.addVertex(i);
        }

        // Add random edges with random weights
        for (int vertex1 : randomGraph.getVertices()) {
            int numEdges = random.nextInt(numVertices); // Random number of edges for each vertex
            for (int i = 0; i < numEdges; i++) {
                int vertex2 = random.nextInt(numVertices) + 1; // Random vertex for the edge
                if (vertex1 != vertex2 && !randomGraph.getEdges(vertex1).containsKey(vertex2)) {
                    int weight = random.nextInt(maxWeight) + 1; // Random weight between 1 and maxWeight
                    randomGraph.addEdge(vertex1, vertex2, weight);
                }
            }
        }

        return randomGraph;
    }




}

