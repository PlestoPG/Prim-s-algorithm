package ui;

import model.Graph;
import javax.swing.*;
import java.awt.*;

public class Application extends JFrame {
    protected final JLabel statusLabel = new JLabel();
    public Graph graph = new Graph();

    public Application() {
        setTitle("Визуализатор алгоритма Прима (Прототип)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        // Панель инструментов
        Toolbar toolbar = new Toolbar(this);
        add(toolbar, BorderLayout.NORTH);

        // Холст с графом
        add(new Canvas(this, toolbar), BorderLayout.CENTER);

        // Панель снизу с ползунком и кнопкой "о разработчиках"
        add(new BottomPanel(this), BorderLayout.SOUTH);

        // Обработка переноса файла или текста в окно приложения
        setTransferHandler(new DragNDropHandler(this));

        setVisible(true);
    }

    public void setStatus(String status) {
        statusLabel.setText(status);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Application::new);
    }
}
