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
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Панель инструментов
        Toolbar toolbar = new Toolbar(this);
        add(toolbar, BorderLayout.NORTH);

        // Холст с графом
        Canvas canvas = new Canvas(this, toolbar);
        JScrollPane scrollPane = new JScrollPane(canvas);
        canvas.setJScrollPane(scrollPane);
        canvas.setPreferredSize(new Dimension(getWidth() * 2, getHeight() * 2));
        add(scrollPane, BorderLayout.CENTER);

        // Панель снизу с ползунком и кнопкой "о разработчиках"
        add(new BottomPanel(this), BorderLayout.SOUTH);

        // Обработка переноса файла или текста в окно приложения
        setTransferHandler(new DragNDropHandler(this));

        setVisible(true);
        SwingUtilities.invokeLater(canvas::initiateViewPoint);
    }

    public void setStatus(String status) {
        statusLabel.setText(status);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Application::new);
    }
}
