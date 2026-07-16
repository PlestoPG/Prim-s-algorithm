package ui.toolbar.buttons;

import ui.toolbar.AlgorithmToolbar;
import ui.toolbar.AutoCompleter;

import javax.swing.*;

public class PauseAutocompletionButton extends JButton {
    public PauseAutocompletionButton(AutoCompleter autoCompleter, AlgorithmToolbar toolbar) {
        setText("Стоп");
        addActionListener(action -> {
            autoCompleter.stop();
            toolbar.editButtonsStatus();
        });
    }
}
