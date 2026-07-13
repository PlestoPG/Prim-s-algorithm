package ui;

import model.Graph;
import model.GraphParser;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public class Toolbar extends JToolBar {
    public final JButton clearButton = new JButton("Очистить");
    public final JButton loadButton = new JButton("Загрузить");
    public final JButton saveButton = new JButton("Сохранить");
    public final JButton startButton = new JButton("Старт");
    public final JButton pauseButton = new JButton("Пауза");
    public final JButton stepButton = new JButton("Шаг");
    public final JButton backstepButton = new JButton("Назад");
    public final JButton restartButton = new JButton("Рестарт");
    public final JButton helpButton = new JButton("Помощь");

    public void verticesAppeared() {
        clearButton.setEnabled(true);
        saveButton.setEnabled(true);
    }

    public void verticesDisappeared() {
        clearButton.setEnabled(false);
        saveButton.setEnabled(false);
    }

    public Toolbar(Application application) {
        clearButton.addActionListener(e -> {
            application.graph = new Graph();
            application.repaint();
            verticesDisappeared();
        });
        clearButton.setEnabled(false);
        add(clearButton);

        loadButton.addActionListener(e -> {
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
        add(loadButton);

        saveButton.addActionListener(e -> {
//            prototype.graph.save();
        });
        saveButton.setEnabled(false);
        add(saveButton);

        addSeparator();

        startButton.setEnabled(false);
        add(startButton);

        pauseButton.setEnabled(false);
        add(pauseButton);

        stepButton.setEnabled(false);
        add(stepButton);

        backstepButton.setEnabled(false);
        add(backstepButton);

        restartButton.addActionListener(e -> {
            application.graph.reset();
            application.repaint();
            application.setStatus("Граф сброшен к начальному состоянию");
        });
        restartButton.setEnabled(false);
        add(restartButton);

        addSeparator();

        helpButton.addActionListener(e -> JOptionPane.showMessageDialog(
                application,
    """
            Очистить - удаляет все вершины и рёбра текущего графа
            Загрузить - загрузить граф из файла или ввести вручную
                Загрузить граф также возможно перетаскиванием в окно приложения файла (в разработке)
            Сохранить - сохранить текущий граф в файл (в разработке)
            
            Старт - начать пошаговое выполнение алгоритма со скоростью, заданной ползунком в нижнем правом углу (в разработке)
            Пауза - остановить пошаговое выполнение алгоритма (в разработке)
            Шаг - выполнить один шаг алгоритма (в разработке)
            Назад - вернуться назад на один шаг алгоритма (в разработке)
            Рестарт - начать алгоритм сначала (в разработке)
            
            ПКМ по пустому полю - добавление вершины
            ПКМ по вершине - удаление выбранной вершины
            ЛКМ от одной вершины до другой - добавление ребра
            Control + ПКМ по вершине - схватить вершину
                Повторное нажатие ПКМ разместит вершину в выбранном месте
            
            Формат загрузки графа:
            Первая строка - количество вершин
            Последующие строки - вершина1 вершина2 вес
            Пример:
            4
            A B 5
            B C 3
            A C 7"""
        ));
        add(helpButton);

        setFloatable(false);
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