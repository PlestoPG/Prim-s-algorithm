package ui.toolbar.buttons;

import ui.Application;

import javax.swing.*;

public class ChooseVertexButton extends JButton {
    public ChooseVertexButton(Application application) {
        setText("Выбрать стартовую вершину");
        addActionListener(action -> {
            setSelected(true);
            application.changeModeToAlgorithmStart();
        });
        setEnabled(false);
    }
}
