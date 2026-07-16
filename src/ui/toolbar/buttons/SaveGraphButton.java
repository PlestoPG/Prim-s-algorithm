package ui.toolbar.buttons;

import ui.Application;
import util.FileSaver;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class SaveGraphButton extends JButton {
    public SaveGraphButton(Application application) {
        setText("Сохранить");
        addActionListener(action -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Выберите куда сохранить граф");
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home") + "/Desktop"));

            int result = fileChooser.showSaveDialog(application);

            if (result == JFileChooser.APPROVE_OPTION) {
                String path = fileChooser.getSelectedFile().getAbsolutePath();
                try {
                    FileSaver.save(path, application.graph);
                    application.setStatus("Граф был сохранён в файл " + path);
                } catch (IOException e) {
                    application.setStatus("Не удалось сохранить граф в файл " + path);
                }
            } else {
                application.setStatus("Сохранение отменено");
            }
        });
        setEnabled(false);
    }
}
