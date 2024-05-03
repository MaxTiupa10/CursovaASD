package org.example.Visualisation;

import javax.swing.*;
import java.awt.*;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;
import java.util.Map;
import org.example.Objects.*;



public class GraphVisualizer extends JFrame {

    private Graph graph;
    private int screen_width = 1717;
    private int screen_height = 960;
    private int menu_width = 340;
    private boolean draggingMenu = false;
    private Map<Integer, Point> vertexCoordinates;
    private Map<Edge, Integer> edgeWeights;
    private Map<Integer, Boolean> resultVertices;  //


    private static class Edge {
        int startVertex;
        int endVertex;

        Edge(int startVertex, int endVertex) {
            this.startVertex = startVertex;
            this.endVertex = endVertex;
        }

        @Override
        public int hashCode() {
            return Integer.hashCode(startVertex) ^ Integer.hashCode(endVertex);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Edge edge = (Edge) obj;
            return (startVertex == edge.startVertex && endVertex == edge.endVertex) ||
                    (startVertex == edge.endVertex && endVertex == edge.startVertex);
        }
    }



    public GraphVisualizer(Graph graph) {
        resultVertices = new HashMap<>();
        for (int vertex : graph.getVertices()) {
            resultVertices.put(vertex, false);
        }

        this.graph = graph;
        this.vertexCoordinates = calculateVertexCoordinates();
        this.edgeWeights = calculateEdgeWeights();

        setTitle("Graph Visualization");

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();

        if (gd.isFullScreenSupported()) {
            setUndecorated(true);  // вимкнення оформлення рамки та заголовка
            setResizable(false);    // вимкнення можливості зміни розміру
            gd.setFullScreenWindow(this);
        } else {
            System.err.println("Full-screen mode not supported");
            setSize(screen_width, screen_height);
            setLocationRelativeTo(null);
        }


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addMouseListener(new MyMouseListener());
        addMouseMotionListener(new MyMouseMotionListener());
    }


    private Map<Integer, Point> calculateVertexCoordinates() {
        Map<Integer, Point> coordinates = new HashMap<>();
        int numVertices = graph.getVertices().size();
        double center_x = (screen_width + menu_width) / 2.0;
        double center_y = screen_height / 2.0;
        double radius = Math.min(center_x, center_y) - 50;

        int i = 0;
        for (int vertex : graph.getVertices()) {
            double angle = (2 * Math.PI * i) / numVertices;
            int x = (int) (center_x + radius * Math.cos(angle));
            int y = (int) (center_y + radius * Math.sin(angle));
            coordinates.put(vertex, new Point(x, y));
            i++;
        }
        return coordinates;
    }
    public void setDefaultColorVertex(){
        for (int vertex : graph.getVertices()) {
            resultVertices.put(vertex, false);
        }
    }
    public void setResultVertex(int vertex, boolean isResult) {
        resultVertices.put(vertex, isResult);
        repaint();  // Перемалюйте графік для відображення змін
    }

    private Map<Edge, Integer> calculateEdgeWeights() {
        Map<Edge, Integer> weights = new HashMap<>();
        for (Map.Entry<Integer, Map<Integer, Integer>> entry : graph.getEdges().entrySet()) {
            int startVertex = entry.getKey();
            for (Map.Entry<Integer, Integer> edgeEntry : entry.getValue().entrySet()) {
                int endVertex = edgeEntry.getKey();
                int weight = edgeEntry.getValue();
                Edge edge = new Edge(startVertex, endVertex);
                weights.put(edge, weight);
            }
        }
        return weights;
    }


    @Override
    public void paint(Graphics g) {
        Image offScreenBuffer = createImage(getWidth(), getHeight());
        Graphics offScreenGraphics = offScreenBuffer.getGraphics();
        drawGraph(offScreenGraphics);
        g.drawImage(offScreenBuffer, 0, 0, this);
    }

    private void drawGraph(Graphics g) {

        g.setColor(new Color(255, 255, 255));
        g.fillRect(0, 0, getWidth(), getHeight());

        // Draw menu column
        g.setColor(new Color(200, 200, 200));
        g.fillRect(0, 0, menu_width, getHeight());

        // Draw top menu
        g.setColor(new Color(150, 150, 150));
        g.fillRect(0, 0, menu_width, 50);

        //Draw title menu
        g.setFont(new Font("Arial", Font.BOLD, 36));// Встановлюємо грубший шрифт
        g.setColor(new Color(0, 0, 0));
        g.drawString("Menu", menu_width/2-55, 35);

        g.setColor(new Color(0, 0, 0));
        g.drawRect(0, 0, menu_width, getHeight());

        // Draw vertices
        for (Map.Entry<Integer, Point> entry : vertexCoordinates.entrySet()) {
            int vertex = entry.getKey();
            Point point = entry.getValue();

            // Circle for the vertex

            if (resultVertices.get(vertex)) {
                g.setColor(new Color(0, 0, 255));  // Темно-синій колір
            } else {
                g.setColor(new Color(0, 255, 255));  // Зазвичайй синій колір
            }

            g.fillOval(point.x - 20, point.y - 20, 40, 40);

            // Display vertex number
            g.setColor(new Color(0, 0, 0));
            Font font = new Font("Arial", Font.BOLD, 28); // Встановлюємо грубший шрифт
            g.setFont(font);

            String vertexString = Integer.toString(vertex);
            FontMetrics fontMetrics = g.getFontMetrics();
            int stringWidth = fontMetrics.stringWidth(vertexString);
            int stringHeight = fontMetrics.getAscent();

            int x = point.x - stringWidth / 2 - 1;
            int y = point.y + (stringHeight / 2) - 4; // Уточнення положення тексту

            g.drawString(vertexString, x, y);
        }

        // Draw edges and display edge weights
        for (Map.Entry<Integer, Map<Integer, Integer>> entry : graph.getEdges().entrySet()) {
            int startVertex = entry.getKey();
            Point startPoint = vertexCoordinates.get(startVertex);

            for (Map.Entry<Integer, Integer> edgeEntry : entry.getValue().entrySet()) {
                int endVertex = edgeEntry.getKey();
                int weight = edgeEntry.getValue();
                Point endPoint = vertexCoordinates.get(endVertex);

                // Draw edge
                g.setColor(new Color(0, 0, 0));
                g.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);

                // Display edge weight
                g.setColor(new Color(0, 0, 0));
                g.drawString(Integer.toString(weight), (startPoint.x + endPoint.x) / 2, (startPoint.y + endPoint.y) / 2);
            }
        }

        int cornerRadius = 40;
        // Draw exit button
        g.setColor(new Color(255, 0, 0));
        g.fillRoundRect(10, getHeight() - 60, menu_width - 20, 50, cornerRadius, cornerRadius);

        g.setColor(new Color(0, 0, 0));
        g.drawString("Exit", menu_width / 2 - 30, getHeight() - 26);

        g.setFont(new Font("Arial", Font.BOLD, 26)); // Set a bolder font

        // Drawing the button for degree centrality
        g.setColor(new Color(255, 255, 0));
        g.fillRoundRect(10, 65, menu_width - 20, 70, cornerRadius, cornerRadius);

        g.setColor(new Color(0, 0, 0));
        g.drawString("Центральна вершина ", 33, 95);
        g.drawString("   за ступенем   ", 70, 120);

        // Drawing the button for closeness centrality
        g.setColor(new Color(255, 255, 0, 255));
        g.fillRoundRect(10, 145, menu_width - 20, 70, cornerRadius, cornerRadius);

        g.setFont(new Font("Arial", Font.BOLD, 22)); // Set a bolder font
        g.setColor(new Color(0, 0, 0));
        g.drawString(" Центральна вершина ", 50, 175);
        g.drawString("за найкоротшими шляхами", 25, 200);

        g.setFont(new Font("Arial", Font.BOLD, 26)); // Set a bolder font
        // Drawing the button for betweenness centrality
        g.setColor(new Color(255, 255, 0));
        g.fillRoundRect(10, 225, menu_width - 20, 70, cornerRadius, cornerRadius);

        g.setColor(new Color(0, 0, 0));
        g.drawString("Центральна вершина ", 33, 255);
        g.drawString("за посередництвом", 40, 280);

        // Drawing the button for eccentricity centrality
        g.setColor(new Color(255, 255, 0));
        g.fillRoundRect(10, 305, menu_width - 20, 70, cornerRadius, cornerRadius);

        g.setColor(new Color(0, 0, 0));
        g.drawString("Центральна вершина ", 33, 335);
        g.drawString("за ексцентриситетом", 40, 360);


        // Drawing the button for eccentricity centrality
        g.setColor(new Color(255, 255, 0));
        g.fillRoundRect(10, 385, menu_width - 20, 70, cornerRadius, cornerRadius);

        g.setColor(new Color(0, 0, 0));
        g.drawString("Аналіз Алгоритмів ", 50, 425);

        // Drawing the button for eccentricity centrality
        g.setColor(new Color(255, 255, 0));
        g.fillRoundRect(10, 465, menu_width - 20, 70, cornerRadius, cornerRadius);

        g.setColor(new Color(0, 0, 0));
        g.drawString("Графічний аналіз", 50, 505);


    }


    // Оголошення змінних
    private int lastX;
    private int lastY;
    private int draggingVertex = -1;
    private boolean isAnimating = false;

    private class MyMouseListener implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            // Not used
        }

        @Override
        public void mousePressed(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();

            // Check if the user clicked on a vertex
            for (Map.Entry<Integer, Point> entry : vertexCoordinates.entrySet()) {
                int vertex = entry.getKey();
                Point point = entry.getValue();
                if (Math.sqrt(Math.pow(x - point.x, 2) + Math.pow(y - point.y, 2)) < 20) {
                    // Vertex clicked
                    System.out.println("Vertex clicked: " + vertex);
                    lastX = x;
                    lastY = y;
                    draggingVertex = vertex;
                    return;
                }
            }

            // Check if the user clicked on the exit button
            if (10 <= x && x <= menu_width - 10 && getHeight() - 60 <= y && y <= getHeight() - 10) {
                System.out.println("Exit button clicked");
                dispose();
            }

            // Check if the user clicked on the extension area
            if (0 <= x && x <= menu_width && 0 <= y && y <= 50) {
                draggingMenu = true;
            }

            // Check if the clicked vertex is within the area of the "Центральна вершина за ступенем" button
            if (10 <= x && x <= menu_width - 10 && 65 <= y && y <= 135) {
                System.out.println("Button 'Центральна вершина за ступенем' clicked");
                setDefaultColorVertex();
                Windows window = new Windows();
                int res = window.degreeWindow(graph);  // Замініть `graph` на ваш граф
                setResultVertex(res , true);

            }

            // Check if the clicked vertex is within the area of the "Центральна вершина за найкоротшими шляхами" button
            if (10 <= x && x <= menu_width - 10 && 145 <= y && y <= 215) {
                System.out.println("Button 'Центральна вершина за найкоротшими шляхами' clicked");
                setDefaultColorVertex();
                Windows window = new Windows();
                int res = window.closenessWindow(graph);  // Замініть `graph` на ваш граф
                setResultVertex(res , true);

            }

            // Check if the clicked vertex is within the area of the "Центральна вершина за посередництвом" button
            if (10 <= x && x <= menu_width - 10 && 225 <= y && y <= 295) {
                System.out.println("Button 'Центральна вершина за посередництвом' clicked");
                setDefaultColorVertex();
                Windows window = new Windows();
                int res = window.betweennessWindow(graph);
                setResultVertex(res , true);

            }

            // Check if the clicked vertex is within the area of the "Центральна вершина за ексцентриситетом" button
            if (10 <= x && x <= menu_width - 10 && 305 <= y && y <= 375) {
                System.out.println("Button 'Центральна вершина за ексцентриситетом' clicked");
                setDefaultColorVertex();
                Windows window = new Windows();
                int res = window.centralityWindow(graph);
                setResultVertex(res , true);

            }

            if (10 <= x && x <= menu_width - 10 && 385 <= y && y <= 455) {
                System.out.println("Button 'Аналіз Алгоритмів' clicked");
                Windows windows = new Windows();
                windows.displayTable(graph);
            }
            if (10 <= x && x <= menu_width - 10 && 465 <= y && y <= 535) {
                System.out.println("Button 'Графічний Аналіз Алгоритмів' clicked");
                Diagram diagram = new Diagram();
                diagram.start();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            draggingMenu = false;
            draggingVertex = -1;
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            // Not used
        }

        @Override
        public void mouseExited(MouseEvent e) {
            // Not used
        }
    }



    private class MyMouseMotionListener implements MouseMotionListener {
        @Override
        public void mouseDragged(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();

            if (isAnimating) {
                return; // Ігнорувати події миші під час анімації
            }

            if (draggingMenu) {
                // Dragging the menu
                menu_width = Math.max(50, Math.min(screen_width - 50, x));
                repaint();
            } else if (draggingVertex != -1) {
                // Check if dragging a vertex
                Point point = vertexCoordinates.get(draggingVertex);
                // Update the vertex coordinates based on the mouse movement
                int deltaX = x - lastX;
                int deltaY = y - lastY;
                Point newPoint = new Point(point.x + deltaX, point.y + deltaY);
                vertexCoordinates.put(draggingVertex, newPoint);
                repaint();

                lastX = x;
                lastY = y;
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            // Not used
        }
    }
}




