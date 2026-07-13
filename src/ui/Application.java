package ui;

import model.Graph;
import algorithm.Prim;
import javax.swing.*;
import java.awt.*;

public class Application extends JFrame {
    protected final JLabel statusLabel = new JLabel();
    public Graph graph = new Graph();
    private final Toolbar toolbar;
    public Prim algorithm;

    public Application() {
        setTitle("Визуализатор алгоритма Прима (Прототип)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        toolbar = new Toolbar(this);
        add(toolbar, BorderLayout.NORTH);

        add(new Canvas(this, toolbar), BorderLayout.CENTER);

        add(new BottomPanel(this), BorderLayout.SOUTH);

        setTransferHandler(new DragNDropHandler(this, toolbar));

        setVisible(true);
    }

    public void graphChanged() {
        if (graph.getVertices().isEmpty()) {
            toolbar.verticesDisappeared();
        } else {
            toolbar.verticesAppeared();
        }
        graph.reset();
        algorithm = new Prim(graph);
        repaint();
    }

    public void setStatus(String status) {
        statusLabel.setText(status);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Application::new);
    }
}
