package ui;

import model.Vertex;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

enum DragState {
    NONE,
    VERTEX,
    EDGE
}

public class MouseController extends MouseAdapter {
    Point mousePosition;
    Vertex selectedVertex;
    Application application;
    Canvas canvas;
    Toolbar toolbar;
    DragState dragState;

    private Vertex findVertex(Point p) {
        for (Vertex v : application.graph.getVertices()) {
            Point vertexPoint = new Point(v.getX() + canvas.offsetX, v.getY() + canvas.offsetY);
            double distance = vertexPoint.distance(p);
            if (distance <= 20)
                return v;
        }
        return null;
    }

    private void placeVertex(int x, int y) {
        String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        for (Character symbol1 : ALPHABET.toCharArray())
            for (Character symbol2 : ALPHABET.toCharArray())
                try {
                    application.graph.addVertex(new Vertex((symbol1 == '0' ? '\0' : symbol1) + "" + symbol2, x, y));
                    return;
                } catch (Exception ignored) {}
        application.setStatus("Диапазон возможных названий вершин закончился");
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        if (SwingUtilities.isRightMouseButton(event)) {
            Vertex vertex = findVertex(event.getPoint());
            if (vertex != null) {
                application.graph.removeVertex(vertex.getName());
                application.setStatus("Удалена вершина в (" + event.getX() + ", " + event.getY() + ")");
            } else {
                placeVertex(event.getPoint().x - canvas.offsetX, event.getPoint().y - canvas.offsetY);
                application.setStatus("Добавлена вершина в (" + event.getX() + ", " + event.getY() + ")");
                canvas.increaseSize(event.getPoint().x, event.getPoint().y);
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
        if (dragState == DragState.VERTEX) {
            selectedVertex.setX(e.getX() - canvas.offsetX);
            selectedVertex.setY(e.getY() - canvas.offsetY);
            canvas.increaseSize(e.getX(), e.getY());
        }
        application.repaint();
    }

    @Override
    public void mousePressed(MouseEvent event) {
        if (SwingUtilities.isLeftMouseButton(event)) {
            selectedVertex = findVertex(event.getPoint());
            if (selectedVertex != null) {
                dragState = DragState.EDGE;
                mousePosition = event.getPoint();
                application.repaint();
            }
        }  else if (SwingUtilities.isMiddleMouseButton(event)) {
            selectedVertex = findVertex(event.getPoint());
            if (selectedVertex != null) {
                dragState = DragState.VERTEX;
                mousePosition = event.getPoint();
                application.repaint();
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (selectedVertex != null) {
            Vertex target = findVertex(e.getPoint());
            if (dragState == DragState.EDGE && target != null && target != selectedVertex) {
                String result = JOptionPane.showInputDialog(
                    application,
                    "Введите вес ребра:"
                );
                try {
                    int weight = Integer.parseInt(result);
                    if (weight < 0)
                        throw new Exception();
                    application.graph.addEdge(selectedVertex.getName(), target.getName(), weight);
                    application.setStatus("Добавлено ребро с весом " + weight + " между вершинами " + selectedVertex.getName() + " и " + target.getName());
                } catch(Exception ex) {
                    application.setStatus("Вес должен быть неотрицательным числом");
                }
            }
            dragState = DragState.NONE;
            selectedVertex = null;
            mousePosition = null;
            application.repaint();
        }
    }

    MouseController(Application application, Canvas canvas, Toolbar toolbar) {
        this.dragState = DragState.NONE;
        this.application = application;
        this.canvas = canvas;
        this.toolbar = toolbar;
    }
}
