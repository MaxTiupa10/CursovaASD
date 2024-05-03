package org.example.Objects;
import java.util.*;

public class Algorithms {

    private int degreeAssignment;
    private int degreeComparation;
    private int centralityAssignment;
    private int centralityComparation;
    private int closenessAssignment;
    private int closenessComparation;
    private int betweennessAssignment;
    private int betweennessComparation;

    private int comparisonCount;
    private int assignmentCount;


    public int getDegreeAssignment() {
        return degreeAssignment;
    }

    public int getDegreeComparation() {
        return degreeComparation;
    }

    public int getCentralityAssignment() {
        return centralityAssignment;
    }

    public int getCentralityComparation() {
        return centralityComparation;
    }

    public int getClosenessAssignment() {
        return closenessAssignment;
    }

    public int getClosenessComparation() {
        return closenessComparation;
    }

    public int getBetweennessAssignment() {
        return betweennessAssignment;
    }

    public int getBetweennessComparation() {
        return betweennessComparation;
    }


    public long degreeTime(Graph graph){
        long startTime =  System.currentTimeMillis();
        int result = degreeCentrality(graph);
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }
    public long centralityTime(Graph graph){
        long startTime =  System.currentTimeMillis();
        int result = centrality(graph);
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }
    public long closenessTime(Graph graph){
        long startTime =  System.currentTimeMillis();
        int result = closenessCentrality(graph);
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }
    public long betweennessTime(Graph graph){
        long startTime =  System.currentTimeMillis();
        int result = betweennessCentrality(graph);
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }
    public int degreeCentrality(Graph graph) {
        int assignmentOperations = 1; // Ініціалізація centralities
        int comparisonOperations = 0; // Ініціалізація порівнянь

        Map<Integer, Integer> centralities = new HashMap<>();
        for (int vertex : graph.getVertices()) {
            assignmentOperations++; // Присвоєння vertex
            assignmentOperations++; // Присвоєння centralities.put(...)
            centralities.put(vertex, graph.getEdges().get(vertex).size());
            comparisonOperations++; // Порівняння vertex
        }

        // Пошук максимального значення
        assignmentOperations++; // Присвоєння centralNode
        assignmentOperations++; // Виклик Collections.max
        comparisonOperations++; // Порівняння Collections.max

        degreeAssignment = assignmentOperations;
        degreeComparation = comparisonOperations;

        int centralNode = Collections.max(centralities.entrySet(), Map.Entry.comparingByValue()).getKey();
        return centralNode;
    }





    public int centrality(Graph graph) {
        Map<Integer, Integer> eccentricities = new HashMap<>();
        int assignmentOperations = 0;
        int comparisonOperations = 0;

        for (int vertex : graph.getVertices()) {
            Map<Integer, Integer> distances = shortestPathLengths(graph, vertex);
            assignmentOperations+= assignmentCount; // Assignment: Map<Integer, Integer> distances = shortestPathLengths(graph, vertex);
            comparisonOperations+= comparisonCount;
            int eccentricity = Collections.max(distances.values());
            assignmentOperations++; // Assignment: int eccentricity = Collections.max(distances.values());
            comparisonOperations++; // Comparison: eccentricity = Collections.max(distances.values());

            eccentricities.put(vertex, eccentricity);
            assignmentOperations++; // Assignment: eccentricities.put(vertex, eccentricity);
        }

        int minEccentricity = Collections.min(eccentricities.values());

        centralityAssignment = assignmentOperations + assignmentCount;
        centralityComparation = comparisonOperations + comparisonCount;

        return minEccentricity;
    }

    public int closenessCentrality(Graph graph) {
        Map<Integer, Double> closenessCentralities = new HashMap<>();
        int assignmentOperations = 0;
        int comparisonOperations = 0;

        for (int vertex : graph.getVertices()) {
            Map<Integer, Integer> distances = shortestPathLengths(graph, vertex);
            assignmentOperations+= assignmentCount;
            comparisonOperations+= comparisonCount;

            double meanDistance = distances.values()
                    .stream()
                    .mapToDouble(Integer::doubleValue)
                    .average()
                    .orElse(0.0);
            assignmentOperations += distances.size(); // Assignments: meanDistance, distances.size()
            comparisonOperations += distances.size(); // Comparisons: distances.size()

            closenessCentralities.put(vertex, 1 / meanDistance);
            assignmentOperations++; // Assignment: closenessCentralities.put(vertex, 1 / meanDistance);
        }

        Map.Entry<Integer, Double> maxEntry = Collections.max(closenessCentralities.entrySet(), Map.Entry.comparingByValue());
        comparisonOperations++; // Comparison: Collections.max(closenessCentralities.entrySet(), Map.Entry.comparingByValue())

        closenessAssignment = assignmentOperations + assignmentCount;
        closenessComparation = comparisonOperations + comparisonCount;


        return  maxEntry.getKey();
    }

    public int betweennessCentrality(Graph graph) {
        Map<Integer, Double> betweennessValues = new HashMap<>();
        int assignmentOperations = 0;
        int comparisonOperations = 0;

        for (int vertex : graph.getVertices()) {
            Stack<Integer> stack = new Stack<>();
            Map<Integer, List<Integer>> predecessors = new HashMap<>();
            Map<Integer, Integer> numPaths = new HashMap<>();
            Map<Integer, Integer> distance = new HashMap<>();
            Map<Integer, Integer> sigma = new HashMap<>();

            for (int v : graph.getVertices()) {
                predecessors.put(v, new ArrayList<>());
                numPaths.put(v, 0);
                distance.put(v, -1);
                sigma.put(v, 0);
                assignmentOperations += 4; // Assignments for initializing maps
            }

            distance.put(vertex, 0);
            sigma.put(vertex, 1);
            assignmentOperations += 2; // Assignments for initializing distance and sigma
            Queue<Integer> queue = new LinkedList<>();
            queue.add(vertex);
            assignmentOperations++; // Assignment: queue.add(vertex);

            while (!queue.isEmpty()) {
                int currentVertex = queue.poll();
                stack.push(currentVertex);
                assignmentOperations += 2; // Operations: queue.poll(), stack.push(currentVertex);

                for (int neighbor : graph.getEdges().get(currentVertex).keySet()) {
                    if (distance.get(neighbor) < 0) {
                        queue.add(neighbor);
                        distance.put(neighbor, distance.get(currentVertex) + 1);
                        assignmentOperations += 2; // Assignments: queue.add(neighbor), distance.put(neighbor, distance.get(currentVertex) + 1);
                    }

                    if (distance.get(neighbor) == distance.get(currentVertex) + 1) {
                        sigma.put(neighbor, sigma.get(neighbor) + sigma.get(currentVertex));
                        predecessors.get(neighbor).add(currentVertex);
                        assignmentOperations += 2; // Assignments: sigma.put(neighbor, sigma.get(neighbor) + sigma.get(currentVertex)), predecessors.get(neighbor).add(currentVertex);
                    }
                }
            }

            Map<Integer, Double> delta = new HashMap<>();
            for (int v : graph.getVertices()) {
                delta.put(v, 0.0);
                assignmentOperations++; // Assignment: delta.put(v, 0.0);
            }

            while (!stack.isEmpty()) {
                int currentVertex = stack.pop();
                assignmentOperations++; // Operation: stack.pop();

                for (int predecessor : predecessors.get(currentVertex)) {
                    delta.put(predecessor, delta.get(predecessor) + (sigma.get(predecessor) / sigma.get(currentVertex))
                            * (1 + delta.get(currentVertex)));
                    assignmentOperations += 2; // Assignments: delta.put(predecessor, ...), multiplication and addition
                }

                if (currentVertex != vertex) {
                    double currentBetweenness = betweennessValues.getOrDefault(currentVertex, 0.0);
                    betweennessValues.put(currentVertex, currentBetweenness + delta.get(currentVertex));
                    assignmentOperations += 2; // Assignments: betweennessValues.put(currentVertex, ...), addition
                }
            }
        }

        double maxBetweenness = Collections.max(betweennessValues.values());
        assignmentOperations++; // Assignment: double maxBetweenness = Collections.max(betweennessValues.values());

        int centralVertex = -1;
        double smallestIdentifier = Double.POSITIVE_INFINITY;

        for (Map.Entry<Integer, Double> entry : betweennessValues.entrySet()) {
            comparisonOperations++; // Comparison: entry.getValue() == maxBetweenness && entry.getKey() < smallestIdentifier
            if (entry.getValue() == maxBetweenness && entry.getKey() < smallestIdentifier) {
                centralVertex = entry.getKey();
                smallestIdentifier = entry.getKey();
                assignmentOperations += 2; // Assignments: centralVertex = entry.getKey(), smallestIdentifier = entry.getKey();
            }
        }

        betweennessAssignment = assignmentOperations;
        betweennessComparation = comparisonOperations;

        return centralVertex;
    }


    private int getMinDistanceVertex(Graph graph, Set<Integer> visited, Map<Integer, Integer> distances) {
        int minDistance = Integer.MAX_VALUE;
        int minVertex = -1;

        for (int vertex : graph.getVertices()) {
            comparisonCount++; // Count comparison operation
            if (!visited.contains(vertex) && distances.get(vertex) < minDistance) {
                minDistance = distances.get(vertex);
                minVertex = vertex;
                assignmentCount += 2; // Count two assignment operations
            }
        }

        return minVertex;
    }
    public Map<Integer, Integer> shortestPathLengths(Graph graph, int source) {
        assignmentCount = 0;
        comparisonCount= 0;
        Map<Integer, Integer> distances = new HashMap<>();
        for (int vertex : graph.getVertices()) {
            distances.put(vertex, Integer.MAX_VALUE);
            assignmentCount++; // Count assignment operation
        }
        distances.put(source, 0);
        assignmentCount++;

        Set<Integer> visited = new HashSet<>();

        while (visited.size() < graph.getVertices().size()) {
            int currentVertex = getMinDistanceVertex(graph, visited, distances);
            visited.add(currentVertex);

            for (int neighbor : graph.getNeighbors(currentVertex)) {
                int weight = graph.getEdges(currentVertex).get(neighbor);

                if (distances.get(currentVertex) + weight < distances.get(neighbor)) {
                    distances.put(neighbor, distances.get(currentVertex) + weight);
                    assignmentCount += 2; // Count two assignment operations
                    comparisonCount++;  // Count comparison operation
                }
            }
        }

        return distances;
    }




}