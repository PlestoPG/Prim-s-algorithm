package ui.toolbar.buttons;

import ui.Application;

import javax.swing.*;

public class RestartButton extends JButton {
    public RestartButton(Application application) {
        setText("Рестарт");
        addActionListener(e -> {
            application.graphChanged();
            application.setStatus("Граф и выбор начальной вершины сброшены к начальному состоянию");
        });
    }
}
