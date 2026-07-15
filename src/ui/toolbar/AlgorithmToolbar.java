package ui.toolbar;

import algorithm.FileSaver;
import ui.Application;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class AlgorithmToolbar extends JToolBar {
    public final JButton startButton = new JButton("Старт");
    public final JButton pauseButton = new JButton("Пауза");
    public final JButton stepButton = new JButton("Шаг");
    public final JButton backstepButton = new JButton("Назад");
    public final JButton restartButton = new JButton("Рестарт");

    public void reset() {
        startButton.setEnabled(false);
        pauseButton.setEnabled(false);
        stepButton.setEnabled(false);
        backstepButton.setEnabled(false);
        restartButton.setEnabled(false);
    }

    public void startChosen() {
        startButton.setEnabled(true);
        restartButton.setEnabled(true);
    }

    public void verticesDisappeared() {
        startButton.setEnabled(false);
        restartButton.setEnabled(false);
    }

    AlgorithmToolbar(Application application) {
        setFloatable(false);
        setBorderPainted(false);

        startButton.addActionListener(e -> {
            application.algorithm.run();
            try {
                new FileSaver().save(
                        "result.txt",
                        application.algorithm.getMst(),
                        application.algorithm.getWeight()
                );
            } catch (IOException ignored) {}
            application.repaint();
            application.setStatus("Минимальное остовное дерево построено! Суммарный вес: " + application.algorithm.getWeight());
        });
        startButton.setEnabled(false);
        add(startButton);

        pauseButton.setEnabled(false);
        add(pauseButton);

        stepButton.setEnabled(false);
        add(stepButton);

        backstepButton.setEnabled(false);
        add(backstepButton);

        restartButton.addActionListener(e -> {
            application.graphChanged();
            application.setStatus("Граф сброшен к начальному состоянию");
        });
        restartButton.setEnabled(false);
        add(restartButton);
    }
}
