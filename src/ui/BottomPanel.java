package ui;

import java.awt.*;
import javax.swing.*;

public class BottomPanel extends JPanel {
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
        aboutButton.addActionListener(e -> {
            JScrollPane scrollPane = new JScrollPane(new AboutDevsWindow());
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            scrollPane.setPreferredSize(new Dimension(Math.min(screenSize.width, screenSize.height) - 100, screenSize.height - 200));
            JOptionPane.showMessageDialog(application, scrollPane, "О разработчиках", JOptionPane.INFORMATION_MESSAGE);
        });
        controlPanel.add(aboutButton, BorderLayout.WEST);

        JSlider speedSlider = new JSlider(0, 100, 50);
        controlPanel.add(speedSlider, BorderLayout.EAST);

        add(statusPanel);
        add(controlPanel);
    }
}
