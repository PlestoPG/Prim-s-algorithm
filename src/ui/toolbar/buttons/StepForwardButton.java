package ui.toolbar.buttons;

import ui.Application;
import ui.toolbar.AlgorithmToolbar;

import javax.swing.*;

public class StepForwardButton extends JButton {
    public StepForwardButton(Application application, AlgorithmToolbar toolbar) {
        setText("Шаг");
        addActionListener(action -> {
            application.algorithm.step();
            toolbar.editButtonsStatus();
        });
    }
}
