package ui;

import model.Graph;
import model.GraphParser;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;

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
        startButton.setEnabled(true);
        restartButton.setEnabled(true);
    }

    public void verticesDisappeared() {
        clearButton.setEnabled(false);
        saveButton.setEnabled(false);
        startButton.setEnabled(false);
        restartButton.setEnabled(false);
    }

    public Toolbar(Application application) {
        clearButton.addActionListener(e -> {
            application.graph = new Graph();
            application.repaint();
            verticesDisappeared();
            application.setStatus("Граф очищен");
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
            application.setStatus("Сохранение графа (в разработке)");
            JOptionPane.showMessageDialog(
                    application,
                    "Функция сохранения будет доступна в следующих версиях",
                    "В разработке",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });
        saveButton.setEnabled(false);
        add(saveButton);

        addSeparator();

        startButton.addActionListener(e -> {
            application.setStatus("Запуск алгоритма (в разработке)");
            JOptionPane.showMessageDialog(
                    application,
                    "Функция запуска алгоритма будет доступна в следующих версиях",
                    "В разработке",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });
        startButton.setEnabled(false);
        add(startButton);

        pauseButton.addActionListener(e -> {
            application.setStatus("Пауза (в разработке)");
        });
        pauseButton.setEnabled(false);
        add(pauseButton);

        stepButton.addActionListener(e -> {
            application.setStatus("Шаг алгоритма (в разработке)");
        });
        stepButton.setEnabled(false);
        add(stepButton);

        backstepButton.addActionListener(e -> {
            application.setStatus("Шаг назад (в разработке)");
        });
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
                        Сохранить - сохранить текущий граф в файл (в разработке)
                        
                        Старт - начать пошаговое выполнение алгоритма (в разработке)
                        Пауза - остановить пошаговое выполнение алгоритма (в разработке)
                        Шаг - выполнить один шаг алгоритма (в разработке)
                        Назад - вернуться назад на один шаг алгоритма (в разработке)
                        Рестарт - начать алгоритм сначала
                        
                        ПКМ по пустому полю - добавление вершины
                        ПКМ по вершине - удаление выбранной вершины
                        ЛКМ от одной вершины до другой - добавление ребра
                        
                        Формат загрузки графа:
                        Первая строка - количество вершин
                        Последующие строки - вершина1 вершина2 вес
                        Пример:
                        4
                        A B 5
                        B C 3
                        A C 7""",
                "Помощь",
                JOptionPane.INFORMATION_MESSAGE
        ));
        add(helpButton);

        setFloatable(false);
    }

    private void loadFromFile(Application application) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Выберите файл с графом");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Текстовые файлы (*.txt)", "txt"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Все файлы", "*"));
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home") + "/Desktop"));

        int result = fileChooser.showOpenDialog(application);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            try {
                String content = Files.readString(selectedFile.toPath());

                if (content.trim().isEmpty()) {
                    application.setStatus("Файл пуст");
                    JOptionPane.showMessageDialog(
                            application,
                            "Файл пуст! Пожалуйста, выберите файл с данными графа.",
                            "Ошибка",
                            JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                loadGraph(application, content, "файла: " + selectedFile.getName());

            } catch (Exception ex) {
                application.setStatus("Ошибка: " + ex.getMessage());
                JOptionPane.showMessageDialog(
                        application,
                        "Не удалось загрузить граф:\n" + ex.getMessage(),
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE
                );
                ex.printStackTrace();
            }
        } else {
            application.setStatus("Загрузка отменена");
        }
    }

    private void loadFromManualInput(Application application) {
        JTextArea textArea = new JTextArea(12, 40);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        textArea.setLineWrap(false);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JPanel panel = new JPanel(new BorderLayout(5, 5));
        JLabel instruction = new JLabel(
                "<html><body style='width: 350px;'>" +
                        "<b>Введите граф в формате:</b><br>" +
                        "Первая строка - количество вершин<br>" +
                        "Последующие строки - вершина1 вершина2 вес<br><br>" +
                        "<b>Пример:</b><br>" +
                        "4<br>" +
                        "A B 1<br>" +
                        "A C 4<br>" +
                        "B C 2<br>" +
                        "B D 5<br>" +
                        "</body></html>"
        );
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
                loadGraph(application, content, "ручного ввода");
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

    private void loadGraph(Application application, String content, String source) {
        try {
            GraphParser parser = new GraphParser();
            Graph newGraph = parser.parse(content);

            application.graph = newGraph;
            application.repaint();

            int vertexCount = application.graph.getVertices().size();
            int edgeCount = application.graph.getEdges().size();
            application.setStatus("Граф загружен из " + source +
                    ". Вершин: " + vertexCount + ", рёбер: " + edgeCount);

            if (vertexCount > 0) {
                verticesAppeared();
            }

            JOptionPane.showMessageDialog(
                    application,
                    "Граф успешно загружен!\n" +
                            "Вершин: " + vertexCount + "\n" +
                            "Рёбер: " + edgeCount,
                    "Успех",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (GraphParser.ParseException ex) {
            application.setStatus("Ошибка загрузки графа: " + ex.getMessage());
            JOptionPane.showMessageDialog(
                    application,
                    "Ошибка загрузки графа:\n" + ex.getMessage() +
                            "\n\nФормат должен быть:\n" +
                            "Первая строка - количество вершин\n" +
                            "Последующие строки - вершина1 вершина2 вес\n" +
                            "Например:\n" +
                            "4\n" +
                            "A B 1\n" +
                            "A C 4\n" +
                            "B C 2\n" +
                            "B D 5",
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (Exception ex) {
            application.setStatus("Ошибка: " + ex.getMessage());
            JOptionPane.showMessageDialog(
                    application,
                    "Не удалось загрузить граф:\n" + ex.getMessage(),
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE
            );
            ex.printStackTrace();
        }
    }
}