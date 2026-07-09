package ui;

import model.Vertex;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseController extends MouseAdapter {
    Point mousePosition;
    Vertex selectedVertex;
    Application application;
    Toolbar toolbar;

    private Vertex findVertex(Point p) {
        for (Vertex v : application.graph.getVertices()) {
            Point vertexPoint = new Point(v.getX(), v.getY());
            double distance = vertexPoint.distance(p);
            if (distance <= 20)
                return v;
        }
        return null;
    }

    private void placeVertex(int x, int y) {
        String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        for (Character symbol1 : (' ' + ALPHABET).toCharArray())
            for (Character symbol2 : ALPHABET.toCharArray())
                try {
                    System.out.println(symbol1 + " " + symbol2);
                    application.graph.addVertex(new Vertex(symbol1 + "" + symbol2, x, y));
                    return;
                } catch (Exception ignored) {}
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        if (SwingUtilities.isRightMouseButton(event)) {
            Vertex vertex = findVertex(event.getPoint());
            if (vertex != null) {
                application.graph.removeVertex(vertex.getName());
                application.setStatus("Удалена вершина в (" + event.getX() + ", " + event.getY() + ")");
            } else {
                placeVertex(event.getPoint().x, event.getPoint().y);
                application.setStatus("Добавлена вершина в (" + event.getX() + ", " + event.getY() + ")");
            }
            if (application.graph.getVertices().isEmpty()) {
                toolbar.verticesDisappeared();
            } else {
                toolbar.verticesAppeared();
            }
            application.repaint();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mousePosition = e.getPoint();
        application.repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            selectedVertex = findVertex(e.getPoint());

            if (selectedVertex != null) {
                mousePosition = e.getPoint();
                application.repaint();
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
                    application,
                    "Введите вес ребра:"
                );
                try {
                    int weight = Integer.parseInt(result);
                    application.graph.addEdge(selectedVertex.getName(), target.getName(), weight);
                    application.setStatus("Добавлено ребро с весом " + weight + " между вершинами " + selectedVertex.getName() + " и " + target.getName());
                } catch(Exception ex) {
                    application.setStatus("Вес должен быть неотрицательным числом");
                }
            }
            selectedVertex = null;
            mousePosition = null;
            application.repaint();
        }
    }

    MouseController(Application application, Toolbar toolbar) {
        this.application = application;
        this.toolbar = toolbar;
    }
}
