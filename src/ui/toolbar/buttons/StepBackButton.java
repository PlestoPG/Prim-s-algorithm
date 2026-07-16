package ui.toolbar.buttons;

import ui.Application;
import ui.toolbar.AlgorithmToolbar;

import javax.swing.*;

public class StepBackButton extends JButton {
    public StepBackButton(Application application, AlgorithmToolbar toolbar) {
        setText("Назад");
        addActionListener(action -> {
            application.algorithm.stepBack();
            toolbar.editButtonsStatus();
        });
    }
}
