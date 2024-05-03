package org.example.Visualisation;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import org.example.Objects.*;

public class Windows extends JFrame {

    private JTextArea textArea;
    Algorithms algorithms;

    private long timeD = 0;
    private long timeC;
    private long timeCl;
    private long timeB;

    public Windows() {
        setSize(400, 150);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        algorithms = new Algorithms();

        textArea = new JTextArea();  // Перенесено з initializeComponents
        textArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);
    }


    public int degreeWindow(Graph graph){
        setTitle("Центральність за ступенем");
        long startTime =  System.nanoTime();
        int result = algorithms.degreeCentrality(graph);
        long endTime = System.nanoTime();

        timeD = endTime - startTime;
        System.out.println(timeD);
        String resultText = "\tResult: " + result + "\n\tExecution time: " + timeD + " nanoseconds"+
                "\n\tNumber of comparison operations : "+algorithms.getDegreeComparation()+
                "\n\tNumber of assignment operations : "+algorithms.getDegreeAssignment();
        textArea.setText(resultText);
        displayWindow();
        return  result;
    }

    public int  centralityWindow(Graph graph){
        setTitle("Метод централіті");
        long startTime =  System.nanoTime();
        int result = algorithms.centrality(graph);
        long endTime = System.nanoTime();

        timeC = endTime - startTime;
        String resultText = "\tResult: " + result + "\n\tExecution time: " + timeC + " nanoseconds"+
                "\n\tNumber of comparison operations : "+algorithms.getCentralityComparation()+
                "\n\tNumber of assignment operations : "+algorithms.getCentralityAssignment();
        textArea.setText(resultText);
        displayWindow();
        return  result;
    }
    public int  closenessWindow(Graph graph){
        setTitle("Центральність за найкоротшими шляхами");
        long startTime =  System.nanoTime();
        int result = algorithms.closenessCentrality(graph);
        long endTime = System.nanoTime();

        timeCl = endTime - startTime;
        String resultText = "\tResult: " + result + "\n\tExecution time: " + timeCl + " nanoseconds"+
                "\n\tNumber of comparison operations : "+algorithms.getClosenessComparation()+
                "\n\tNumber of assignment operations : "+algorithms.getClosenessComparation();
        textArea.setText(resultText);
        displayWindow();
        return  result;
    }

    public int  betweennessWindow(Graph graph){
        setTitle("Центральність за посередництвом");
        long startTime =  System.nanoTime();
        int result = algorithms.betweennessCentrality(graph);
        long endTime = System.nanoTime();

        timeB = endTime - startTime;
        String resultText = "\tResult: " + result + "\n\tExecution time: " + timeB + " nanoseconds"+
                "\n\tNumber of comparison operations : "+algorithms.getBetweennessComparation()+
                "\n\tNumber of assignment operations : "+algorithms.getBetweennessAssignment();
        textArea.setText(resultText);
        displayWindow();
        return  result;
    }
    public void displayTable(double[][] tableData) {
        setTitle("Аналіз Алгоритмів");

        // Convert int[][] to Object[][]
        Object[][] data = new Object[tableData.length][tableData[0].length];
        for (int i = 0; i < tableData.length; i++) {
            for (int j = 0; j < tableData[i].length; j++) {
                data[i][j] = tableData[i][j];
            }
        }
        setSize(1300, 220);
        // Назви рядків (рядкові заголовки)
        String[] rowHeaders = {"Центральність \nза ступенем", "Центральність \nза ексцентриситетом", "Центральність за \nнайкоротшими шляхами", "Центральність \nза посередництвом"};

        // Створення DefaultTableModel з назвами рядків та назвами стовпців
        DefaultTableModel tableModel = new DefaultTableModel(data,
                new String[]{" ", "Центральна вершина", "Час в мілісекундах", "Кількість операцій порівняння", "Кількість операцій присвоєння"});

        // Встановлення назв рядків у перший стовпець
        for (int i = 0; i < rowHeaders.length; i++) {
            tableModel.setValueAt(rowHeaders[i], i, 0);
        }

        // Create a JTable with the model
        JTable table = new JTable(tableModel);

        // Set some properties for the table
        table.setFillsViewportHeight(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // Задаємо висоту для кожного рядка
        int rowHeight = 40; // Змініть 30 на бажану висоту
        table.setRowHeight(rowHeight);

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setBackground(new Color(112, 195, 224)); // Світло-синій колір




        // Встановлення рендерера для кожного заголовка стовпця, крім першого
        for (int i = 1; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }
        // Встановлення рендерера для заголовків рядків
        table.getTableHeader().setDefaultRenderer(headerRenderer);

        // Create a scroll pane for the table
        JScrollPane scrollPane = new JScrollPane(table);

        // Add the scroll pane to the frame
        add(scrollPane, BorderLayout.CENTER);

        // Display the window
        displayWindow();
    }

    public void displayTable(Graph graph) {
        double[][] tableData = {
                {0,algorithms.degreeCentrality(graph), algorithms.degreeTime(graph) , algorithms.getDegreeComparation(),algorithms.getDegreeAssignment()},
                {0,algorithms.centrality(graph), algorithms.centralityTime(graph), algorithms.getCentralityComparation(), algorithms.getCentralityAssignment()},
                {0,algorithms.closenessCentrality(graph), algorithms.closenessTime(graph), algorithms.getClosenessComparation(), algorithms.getClosenessAssignment()},
                {0,algorithms.betweennessCentrality(graph),algorithms.betweennessTime(graph),algorithms.getBetweennessComparation(), algorithms.getBetweennessAssignment()}
        };

        displayTable(tableData);
    }


    public void displayWindow() {
        SwingUtilities.invokeLater(() -> {
            setLocationRelativeTo(null);
            setAlwaysOnTop(true);
            setVisible(true);

        });
    }

}



