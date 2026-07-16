package ui.toolbar.buttons;

import model.Edge;
import ui.Application;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class ResultButton extends JButton {
    public ResultButton(Application application) {
        setText("Результат");
        addActionListener(action -> {
            String answer;
            if (application.algorithm.getMst().isEmpty()) {
                answer = "Не удалось построить минимальное остовное дерево: отсутствуют рёбра, либо начальная вершина не соединена ни с одной другой";
            } else {
                List<String> edges = new ArrayList<>();
                for (Edge edge : application.algorithm.getMst())
                    edges.add(edge.getV1().getName() + " - " + edge.getV2().getName() + " (" + edge.getWeight() + ")");
                answer = "Вес минимального остовного дерева: " + application.algorithm.getWeight() + "\n" +
                        "Использованные рёбра:\n" + String.join("\n", edges);
            }
            JOptionPane.showMessageDialog(
                    application,
                    answer
            );
        });
    }
}
