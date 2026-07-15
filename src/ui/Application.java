package ui;

import model.Graph;
import algorithm.Prim;
import model.Vertex;
import ui.toolbar.Toolbar;

import javax.swing.*;
import java.awt.*;

import static ui.BottomPanel.DEFAULT_DELAY;

public class Application extends JFrame {
    protected final JLabel statusLabel = new JLabel();
    private final Canvas canvas;
    public Graph graph = new Graph();
    private final Toolbar toolbar;
    private final BottomPanel bottomPanel;
    public Prim algorithm;

    public Application() {
        setTitle("Визуализатор алгоритма Прима (Альфа)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        toolbar = new Toolbar(this);
        add(toolbar, BorderLayout.NORTH);

        canvas = new Canvas(this, toolbar);
        add(canvas, BorderLayout.CENTER);

        bottomPanel = new BottomPanel(this);
        add(bottomPanel, BorderLayout.SOUTH);

        setTransferHandler(new DragNDropHandler(this, toolbar));

        setVisible(true);
    }

    public void chooseStartVertex() {
        setStatus("Выберите начальную вершину");
        canvas.chooseStartVertexMode();
    }

    public void setStartVertex(Vertex vertex) {
        algorithm.start(vertex);
        toolbar.startChosen();
        setStatus("Начало работы. Стартовая вершина: " + vertex.getName());
        canvas.repaint();
    }

    public void canvasRepaint() {
        canvas.repaint();
    }

    public void graphChanged() {
        if (graph.getVertices().isEmpty()) {
            toolbar.verticesDisappeared();
        } else {
            toolbar.verticesAppeared();
        }
        graph.reset();
        canvas.resetMouseController();
        algorithm = new Prim(graph);
        repaint();
    }

    public void setStatus(String status) {
        statusLabel.setText(status);
    }

    public int getStepDelay() {
        if (bottomPanel == null)
            return DEFAULT_DELAY;
        return bottomPanel.getStepDelay();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Application::new);
    }
}
