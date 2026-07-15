package ui.toolbar;

import algorithm.FileSaver;
import ui.Application;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class AlgorithmToolbar extends JToolBar {
    public final JButton endButton = new JButton("В конец");
    public final JButton startButton = new JButton("Старт");
    public final JButton pauseButton = new JButton("Пауза");
    public final JButton stepButton = new JButton("Шаг");
    public final JButton backstepButton = new JButton("Назад");
    public final JButton restartButton = new JButton("Рестарт");

    public void reset() {
        endButton.setEnabled(false);
        startButton.setEnabled(false);
        pauseButton.setEnabled(false);
        stepButton.setEnabled(false);
        backstepButton.setEnabled(false);
        restartButton.setEnabled(false);
    }

    public void startChosen() {
        endButton.setEnabled(true);
//        startButton.setEnabled(true);
        stepButton.setEnabled(true);
//        backstepButton.setEnabled(true);
        restartButton.setEnabled(true);
    }

    public void algorithmEnded() {
        reset();
        restartButton.setEnabled(true);
    }

    AlgorithmToolbar(Application application) {
        setFloatable(false);
        setBorderPainted(false);

        endButton.addActionListener(e -> {
            application.algorithm.run();
            try {
                new FileSaver().save(
                        "result.txt",
                        application.algorithm.getMst(),
                        application.algorithm.getWeight()
                );
            } catch (IOException ignored) {}
            application.setStatus("Минимальное остовное дерево построено! Суммарный вес: " + application.algorithm.getWeight());
            algorithmEnded();
            application.repaint();
        });
        add(endButton);

        add(startButton);

        add(pauseButton);

        stepButton.addActionListener(e -> {
            application.algorithm.step();
            if (application.algorithm.isDone()) {
                application.setStatus("Минимальное остовное дерево построено! Суммарный вес: " + application.algorithm.getWeight());
                algorithmEnded();
            }
            application.repaint();
        });
        add(stepButton);

        add(backstepButton);

        restartButton.addActionListener(e -> {
            application.graphChanged();
            application.setStatus("Граф сброшен к начальному состоянию");
        });
        add(restartButton);

        reset();
    }
}
