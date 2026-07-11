package ui;

import model.Graph;
import javax.swing.*;
import java.awt.*;

public class Application extends JFrame {
    protected final JLabel statusLabel = new JLabel();
    public Graph graph = new Graph();
    private final Toolbar toolbar;

    public Application() {
        setTitle("Визуализатор алгоритма Прима (Прототип)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        toolbar = new Toolbar(this);
        add(toolbar, BorderLayout.NORTH);

        Canvas canvas = new Canvas(this, toolbar);
        add(canvas, BorderLayout.CENTER);

        add(new BottomPanel(this), BorderLayout.SOUTH);

        setTransferHandler(new DragNDropHandler(this));

        setDropTarget(null);

        setVisible(true);
    }

    public void setStatus(String status) {
        statusLabel.setText(status);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Application::new);
    }
}