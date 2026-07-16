package ui.toolbar;

import ui.Application;
import ui.toolbar.buttons.*;

import javax.swing.*;

public class Toolbar extends JToolBar {
    private final JButton clearButton;
    private final JButton saveButton;
    private final JButton chooseVertexButton;
    private final int index;
    private final AlgorithmToolbar algorithmToolbar;

    public enum ToolbarMode {
        BUILDING,
        ALGORITHM,
        CHECK_RESULT
    }

    public void graphChanged(boolean isEmpty) {
        clearButton.setEnabled(!isEmpty);
        saveButton.setEnabled(!isEmpty);
        chooseVertexButton.setEnabled(!isEmpty);
        changeMode(ToolbarMode.BUILDING);
    }

    public void loadedGraphWithResult() {
        clearButton.setEnabled(true);
        saveButton.setEnabled(true);
        changeMode(ToolbarMode.CHECK_RESULT);
    }

    public void changeMode(ToolbarMode mode) {
        remove(index);
        switch (mode) {
            case BUILDING -> {
                chooseVertexButton.setSelected(false);
                add(chooseVertexButton, index);
            }
            case ALGORITHM -> {
                algorithmToolbar.reset();
                add(algorithmToolbar, index);
            }
            case CHECK_RESULT -> {
                algorithmToolbar.resultOnly();
                add(algorithmToolbar, index);
            }
        }
        repaint();
    }

    public void startChosen() {
        changeMode(ToolbarMode.ALGORITHM);
        algorithmToolbar.editButtonsStatus();
    }

    public Toolbar(Application application) {
        setFloatable(false);

        clearButton = new ClearGraphButton(application);
        add(clearButton);

        add(new LoadGraphButton(application));

        saveButton = new SaveGraphButton(application);
        add(saveButton);

        addSeparator();

        chooseVertexButton = new ChooseVertexButton(application);
        add(chooseVertexButton);
        index = getComponentIndex(chooseVertexButton);

        algorithmToolbar = new AlgorithmToolbar(application);

        addSeparator();

        add(new HelpButton(application));
    }
}