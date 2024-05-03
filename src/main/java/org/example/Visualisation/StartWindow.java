package org.example.Visualisation;

import org.example.Objects.Graph;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartWindow extends JFrame {
    private JTextField verticesField;
    private JTextField weightField;
    private JButton submitButton;

    private int numberOfVertices;
    private int edgeWeight;

    public int getNumberOfVertices() {
        return numberOfVertices;
    }

    public int getEdgeWeight() {
        return edgeWeight;
    }

    public StartWindow() {
        // Налаштування вікна
        setTitle("Graph Configuration");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Створення компонентів
        JLabel verticesLabel = new JLabel("Введіть кількість вершин : ");
        verticesField = new JTextField(10);

        JLabel weightLabel = new JLabel("Введіть максимальну вагу ребра : ");
        weightField = new JTextField(10);

        submitButton = new JButton("Submit");

        // Налаштування layout за допомогою GroupLayout
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(layout.createParallelGroup()
                .addComponent(verticesLabel)
                .addComponent(verticesField)
                .addComponent(weightLabel)
                .addComponent(weightField)
                .addComponent(submitButton)
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(verticesLabel)
                .addComponent(verticesField)
                .addComponent(weightLabel)
                .addComponent(weightField)
                .addComponent(submitButton)
        );

        // Додавання обробника подій для кнопки
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Зчитування введених значень
                    numberOfVertices = Integer.parseInt(verticesField.getText());
                    edgeWeight = Integer.parseInt(weightField.getText());

                    // Перевірка на обмеження
                    if (numberOfVertices <= 5 || edgeWeight <= 5 || numberOfVertices > 20 || edgeWeight > 30) {
                        throw new NumberFormatException();

                    }

                    // Додайте dispose() для закриття вікна після успішного введення
                    dispose();
                    Graph randomGraph = RandomGraphGenerator.generateRandomGraph(numberOfVertices,edgeWeight);
                    GraphVisualizer graphVisualizer = new GraphVisualizer(randomGraph);
                } catch (NumberFormatException ex) {
                    // Обробка помилок при некоректному введенні або виході за обмеження
                    JOptionPane.showMessageDialog(StartWindow.this, "Будь ласка, введіть правильні числа (5-20 для вершин, 5-30 для ваги ребра)", "Помилка", JOptionPane.ERROR_MESSAGE);
                }

            }
        });
    }

    public void start(){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                setVisible(true);
            }
        });
    }


}
