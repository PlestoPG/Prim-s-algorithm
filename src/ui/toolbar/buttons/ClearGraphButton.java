package ui.toolbar.buttons;

import model.Graph;
import ui.Application;

import javax.swing.*;

public class ClearGraphButton extends JButton {
    public ClearGraphButton(Application application) {
        setText("Очистить");
        addActionListener(action -> {
            application.graph = new Graph();
            application.graphChanged();
            application.setStatus("Холст был очищен");
        });
        setEnabled(false);
    }
}
