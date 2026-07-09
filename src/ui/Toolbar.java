package ui;

import model.Graph;

import javax.swing.*;

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
            String string = JOptionPane.showInputDialog(
                    application,
                """
                        Введите граф в формате:
                        вершина1 вершина2 вес"""
            );
//            GraphParser parser = new GraphParser();
            try {
//                prototype.graph = parser.parse(string);
                application.repaint();
            } catch (Exception ex) {
                application.setStatus("Не удалось загрузить граф");
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

        restartButton.addActionListener(e -> application.graph.reset());
        restartButton.setEnabled(false);
        add(restartButton);

        addSeparator();

        helpButton.addActionListener(e -> JOptionPane.showMessageDialog(
                application,
    """
            Очистить - удаляет все вершины и рёбра текущего графа
            Загрузить - загрузить граф из текста (в работе)
                Загрузить граф также возможно перетаскиванием в окно приложения файла (в работе)
            Сохранить - сохранить текущий граф в файл graph.txt (в работе)
            
            Старт - начать пошаговое выполнение алгоритма со скоростью, заданной ползунком в нижнем правом углу (в работе)
            Пауза - остановить пошаговое выполнение алгоритма (в работе)
            Шаг - выполнить один шаг алгоритма (в работе)
            Назад - вернуться назад на один шаг алгоритма (в работе)
            Рестарт - начать алгоритм сначала (в работе)
            
            ПКМ по пустому полю - добавление вершины
            ПКМ по вершине - удаление выбранной вершины
            ЛКМ от одной вершины до другой - добавление ребра"""
        ));
        add(helpButton);

        setFloatable(false);
    }
}