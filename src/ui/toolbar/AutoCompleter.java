package ui.toolbar;

import ui.Application;

import javax.swing.*;

public class AutoCompleter extends Timer {
    private final Application application;

    private void updateDelay() {
        setDelay(application.getStepDelay());
    }

    @Override
    public void start() {
        updateDelay();
        super.start();
    }

    public AutoCompleter(Application application, AlgorithmToolbar toolbar) {
        super(0, null);
        this.application = application;
        addActionListener(event -> {
            application.algorithm.step();
            toolbar.editButtonsStatus();
            updateDelay();
        });
    }
}
