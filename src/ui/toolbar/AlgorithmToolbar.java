package ui.toolbar;

import ui.Application;
import ui.toolbar.buttons.*;

import javax.swing.*;

public class AlgorithmToolbar extends JToolBar {
    private final Application application;
    private final JButton endButton;
    private final JButton startButton;
    private final JButton pauseButton;
    private final JButton stepButton;
    private final JButton backstepButton;
    private final JButton resultButton;
    private final AutoCompleter autoCompleter;

    public void reset() {
        startButton.setSelected(false);
        pauseButton.setSelected(true);
        autoCompleter.stop();
    }

    public void resultOnly() {
        endButton.setEnabled(false);
        startButton.setEnabled(false);
        pauseButton.setEnabled(false);
        stepButton.setEnabled(false);
        backstepButton.setEnabled(false);
        resultButton.setEnabled(true);
    }

    public void editButtonsStatus() {
        startButton.setSelected(autoCompleter.isRunning());
        pauseButton.setSelected(!autoCompleter.isRunning());
        if (application.algorithm.isDone()) {
            endButton.setEnabled(false);
            startButton.setEnabled(false);
            pauseButton.setEnabled(false);
            stepButton.setEnabled(false);
            backstepButton.setEnabled(true);
            resultButton.setEnabled(true);
            autoCompleter.stop();
        } else if (application.algorithm.getMst().isEmpty()) {
            endButton.setEnabled(true);
            startButton.setEnabled(true);
            pauseButton.setEnabled(true);
            stepButton.setEnabled(true);
            backstepButton.setEnabled(false);
            resultButton.setEnabled(false);
        } else {
            endButton.setEnabled(true);
            startButton.setEnabled(true);
            pauseButton.setEnabled(true);
            stepButton.setEnabled(true);
            backstepButton.setEnabled(true);
            resultButton.setEnabled(false);
        }
        application.setStatus(application.algorithm.getMsg());
        application.repaint();
    }

    AlgorithmToolbar(Application application) {
        this.application = application;
        setFloatable(false);
        setBorderPainted(false);
        setBorder(null);
        setOpaque(false);

        endButton = new EndAlgorithmButton(application, this);
        add(endButton);

        autoCompleter = new AutoCompleter(application, this);

        startButton = new StartAutocompletionButton(autoCompleter, this);
        add(startButton);

        pauseButton = new PauseAutocompletionButton(autoCompleter, this);
        add(pauseButton);

        stepButton = new StepForwardButton(application, this);
        add(stepButton);

        backstepButton = new StepBackButton(application, this);
        add(backstepButton);

        add(new RestartButton(application));

        resultButton = new ResultButton(application);
        add(resultButton);

        editButtonsStatus();
    }
}
