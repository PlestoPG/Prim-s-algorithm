package ui;

import model.Graph;
import model.Vertex;
import model.Edge;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.nio.file.Files;
import java.util.List;


public class Application extends JFrame {
    private final JPanel canvas;
    protected final JLabel statusLabel = new JLabel();
    public Graph graph = new Graph();
    private Point mousePosition;
    private Vertex selectedVertex;


    private Vertex findVertex(Point p) {
        for (Vertex v : graph.getVertices()) {
            Point vertexPoint = new Point(v.getX(), v.getY());
            double distance = vertexPoint.distance(p);
            if (distance <= 20)
                return v;
        }
        return null;
    }

    public Application() {
        setTitle("Визуализатор алгоритма Прима (Прототип)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        // Панель инструментов
        Toolbar toolbar = new Toolbar(this);
        add(toolbar, BorderLayout.NORTH);

        // Холст
        canvas = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                for (Edge edge: graph.getEdges()) {
                    g.setColor(Color.BLUE);
                    g.drawLine(edge.getV1().getX(), edge.getV1().getY(), edge.getV2().getX(), edge.getV2().getY());
                    g.setColor(Color.BLACK);

                    int midX = (edge.getV1().getX() + edge.getV2().getX()) / 2;
                    int midY = (edge.getV1().getY() + edge.getV2().getY()) / 2;

                    g.drawString(edge.getWeight() + "", midX, midY);
                }

                if (selectedVertex != null && mousePosition != null) {
                    g.setColor(Color.BLUE);
                    g.drawLine(selectedVertex.getX(), selectedVertex.getY(), mousePosition.x, mousePosition.y);
                    g.setColor(Color.WHITE);
                }

                for (Vertex vertex: graph.getVertices()) {
                    g.setColor(Color.BLUE);
                    g.fillOval(vertex.getX() - 15, vertex.getY() - 15, 30, 30);
                    g.setColor(Color.WHITE);
                    g.drawString(vertex.getName(), vertex.getX() - 4 * vertex.getName().length(), vertex.getY() + 5);
                }
            }
        };
        canvas.setBackground(Color.WHITE);
        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                if (SwingUtilities.isRightMouseButton(event)) {
                    Vertex vertex = findVertex(event.getPoint());
                    if (vertex != null) {
                        graph.removeVertex(vertex.getName());
                        statusLabel.setText("Удалена вершина в (" + event.getX() + ", " + event.getY() + ")");
                    } else {
                        int x = event.getPoint().x,  y = event.getPoint().y;
                        char symbol = '1';
                        while (symbol <= 'Z') {
                            try {
                                graph.addVertex(new Vertex(symbol + "", x, y));
                                break;
                            } catch (Exception e) { symbol++;}
                        }
                        statusLabel.setText("Добавлена вершина в (" + event.getX() + ", " + event.getY() + ")");
                    }
                    if (graph.getVertices().isEmpty()) {
                        toolbar.verticesDisappeared();
                    } else {
                        toolbar.verticesAppeared();
                    }
                    canvas.repaint();
                }
            }
        });

        canvas.addMouseMotionListener(new MouseAdapter() {

            @Override
            public void mouseDragged(MouseEvent e) {

                mousePosition = e.getPoint();

                canvas.repaint();
            }
        });

        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    selectedVertex = findVertex(e.getPoint());

                    if (selectedVertex != null) {
                        mousePosition = e.getPoint();
                        canvas.repaint();
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (selectedVertex != null) {
                    Vertex target = findVertex(e.getPoint());

                    // Если отпустили на другую вершину
                    if (target != null && target != selectedVertex) {
                        String result = JOptionPane.showInputDialog(
                            Application.this,
                            "Введите вес ребра:"
                        );
                        try {
                            int weight = Integer.parseInt(result);
                            graph.addEdge(selectedVertex.getName(), target.getName(), weight);
                            statusLabel.setText("Добавлено ребро с весом " + weight + " между вершинами " + selectedVertex.getName() + " и " + target.getName());
                        } catch(Exception ex) {
                            statusLabel.setText("Вес должен быть неотрицательным числом");
                        }
                    }
                    selectedVertex = null;
                    mousePosition = null;
                    canvas.repaint();
                }
            }
        });

        add(canvas, BorderLayout.CENTER);

        add(new BottomPanel(this), BorderLayout.SOUTH);

        setVisible(true);

        setTransferHandler(new TransferHandler() {
            @Override
            public boolean canImport(TransferSupport support) {
                return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor) ||
                        support.isDataFlavorSupported(DataFlavor.stringFlavor);
            }

            @Override
            public boolean importData(TransferSupport support) {
                try {
                    String text;
                    Transferable transferable = support.getTransferable();
                    if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                        List<File> files = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
                        File file = files.getFirst();
                        text = Files.readString(file.toPath());
                    } else {
                        text = (String) transferable.getTransferData(DataFlavor.stringFlavor);
                    }
                    // GraphParser parser = new GraphParser();
                    // prototype.graph = parser.parse(string);
                    setStatus("Граф загружен");
                    repaint();
                    return true;
                } catch (Exception ex) {
                    setStatus("Не удалось загрузить граф");
                    return false;
                }
            }
        });
    }

    public void setStatus(String status) {
        statusLabel.setText(status);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Application::new);
    }
}
