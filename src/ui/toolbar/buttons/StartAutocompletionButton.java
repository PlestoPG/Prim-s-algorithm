package ui.toolbar.buttons;

import ui.toolbar.AlgorithmToolbar;
import ui.toolbar.AutoCompleter;

import javax.swing.*;

public class StartAutocompletionButton extends JButton {
    public StartAutocompletionButton(AutoCompleter autoCompleter, AlgorithmToolbar toolbar) {
        setText("Старт");
        addActionListener(action -> {
            autoCompleter.start();
            toolbar.editButtonsStatus();
        });
    }
}
