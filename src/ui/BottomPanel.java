package ui;

import java.awt.*;
import javax.swing.*;

public class BottomPanel extends JPanel {
    private final JSlider speedSlider;

    public static int DEFAULT_DELAY = 1000;

    public int getStepDelay() { return speedSlider.getValue(); }

    BottomPanel(Application application) {
        setLayout(new GridLayout(2, 1));

        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(
                BorderFactory.createEmptyBorder(0, 10, 0, 10)
        );
        application.setStatus("Прототип. Реализованный функционал доступен по кнопке \"Помощь\"");
        statusPanel.add(application.statusLabel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel(new BorderLayout());

        JButton aboutButton = new JButton("Информация о разработчиках");
        aboutButton.addActionListener(e -> new AboutDevsWindow(application));
        controlPanel.add(aboutButton, BorderLayout.WEST);

        speedSlider = new JSlider(0, 5000, DEFAULT_DELAY);
        controlPanel.add(speedSlider, BorderLayout.EAST);

        add(statusPanel);
        add(controlPanel);
    }
}
