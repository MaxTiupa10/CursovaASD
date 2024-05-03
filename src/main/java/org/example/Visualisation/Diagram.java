package org.example.Visualisation;

import org.example.Objects.*;

import javax.swing.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.util.ArrayList;


public class Diagram {

    private Algorithms algorithms;
    private ArrayList<Graph> graphs;

    public Diagram() {
        algorithms = new Algorithms();

        // Створення ArrayList для збереження чисел
        ArrayList<Integer> numbersList = new ArrayList<>();
        for (int i = 5; i < 200; i+=2) {
            // Додавання чисел до ArrayList
            numbersList.add(i);
        }
        int maxWeight = 10;
        graphs = new ArrayList<>();
        for (int i : numbersList) {
            Graph graph = RandomGraphGenerator.generateRandomGraph(i, maxWeight);
            graphs.add(graph);
        }
    }

    public void start() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            frame.setTitle("Графік");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            XYSeries series1 = new XYSeries("Центральна Вершина За Ступенем");
            XYSeries series2 = new XYSeries("Центральна Вершина За Ексцентриситетом");
            XYSeries series3 = new XYSeries("Центральна Вершина За Найкоротшими шляхами");
            XYSeries series4 = new XYSeries("Центральна Вершина За Посередництвом");

            for (Graph graph : graphs) {
                series1.add(graph.getVertices().size(), algorithms.degreeTime(graph));
                series2.add(graph.getVertices().size(), algorithms.centrality(graph));
                series3.add(graph.getVertices().size(), algorithms.closenessTime(graph));
                series4.add(graph.getVertices().size(), algorithms.betweennessTime(graph));
            }

            XYSeriesCollection data = new XYSeriesCollection(series1);
            data.addSeries(series2);
            data.addSeries(series3);
            data.addSeries(series4);

            JFreeChart chart = ChartFactory.createXYLineChart(
                    "Графічний Аналіз Алгоритмів",
                    "Кількість Вершин",
                    "Час Виконання Алгоритму (мілісекунди)",
                    data,
                    PlotOrientation.VERTICAL,
                    true,
                    true,
                    false
            );

            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new java.awt.Dimension(1440, 900));

            // Додаємо можливість збільшення та зменшення масштабу
            chartPanel.setMouseWheelEnabled(true);

            frame.setContentPane(chartPanel);
            frame.pack();
            frame.setVisible(true);
        });
    }

    public static void main(String[] args) {
        Diagram diagram = new Diagram();
        diagram.start();
    }
}

