package ui.toolbar.buttons;

import ui.Application;
import ui.toolbar.AlgorithmToolbar;

import javax.swing.*;

public class EndAlgorithmButton extends JButton {
    public EndAlgorithmButton(Application application, AlgorithmToolbar toolbar) {
        setText("В конец");
        addActionListener(action -> {
            application.algorithm.run();
            toolbar.editButtonsStatus();
        });
    }
}
