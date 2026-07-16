package ui.toolbar.buttons;

import ui.Application;
import model.GraphParser;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public class LoadGraphButton extends JButton {
    public LoadGraphButton(Application application) {
         setText("Загрузить");
         addActionListener(action -> {
             String[] options = {"Из файла", "Ввести вручную", "Отмена"};
             int choice = JOptionPane.showOptionDialog(
                     application,
                     "Выберите способ загрузки графа:",
                     "Загрузка графа",
                     JOptionPane.DEFAULT_OPTION,
                     JOptionPane.QUESTION_MESSAGE,
                     null,
                     options,
                     options[0]
             );

             if (choice == 0) {
                 loadFromFile(application);
             } else if (choice == 1) {
                 loadFromManualInput(application);
             }
         });
    }

    private void loadFromFile(Application application) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Выберите файл с графом");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Текстовые файлы (*.txt)", "txt"));
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home") + "/Desktop"));

        int result = fileChooser.showOpenDialog(application);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            GraphParser.parseAndLoad(application, selectedFile);
        } else {
            application.setStatus("Загрузка отменена");
        }
    }

    private void loadFromManualInput(Application application) {
        JTextArea textArea = new JTextArea(12, 40);
        textArea.setLineWrap(false);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JPanel panel = new JPanel(new BorderLayout(5, 5));
        JLabel instruction = new JLabel(GraphParser.instruction);
        instruction.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.add(instruction, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        int result = JOptionPane.showConfirmDialog(
                application,
                panel,
                "Ручной ввод графа",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            String content = textArea.getText();
            if (content != null && !content.trim().isEmpty()) {
                GraphParser.parseAndLoad(application, content);
            } else {
                application.setStatus("Ввод пуст");
                JOptionPane.showMessageDialog(
                        application,
                        "Пожалуйста, введите данные графа.",
                        "Ошибка",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        } else {
            application.setStatus("Ввод отменен");
        }
    }
}
