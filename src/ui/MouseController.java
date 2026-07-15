package ui;

import model.Vertex;
import ui.toolbar.Toolbar;
import util.FindVertex;
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
    Toolbar toolbar;
    DragState dragState;

    private String placeVertex(int x, int y) {
        String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        for (Character symbol1 : ALPHABET.toCharArray())
            for (Character symbol2 : ALPHABET.toCharArray())
                try {
                    String name = (symbol1 == '0' ? "" : symbol1) + "" + symbol2;
                    application.graph.addVertex(new Vertex(name, x, y));
                    return name;
                } catch (Exception ignored) {}
        application.setStatus("Диапазон возможных названий вершин закончился");
        return null;
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        if (SwingUtilities.isRightMouseButton(event)) {
            Vertex vertex = FindVertex.byPoint(application.graph, event.getPoint());
            if (vertex != null && event.isControlDown() && dragState != DragState.VERTEX) {
                dragState = DragState.VERTEX;
                selectedVertex = vertex;
                mousePosition = event.getPoint();
                application.setStatus("Взята вершина " + selectedVertex.getName());
                application.canvasRepaint();
            } else if (vertex != null && dragState != DragState.VERTEX) {
                String name = vertex.getName();
                application.graph.removeVertex(name);
                application.setStatus("Удалена вершина " + name);
                application.graphChanged();
            } else if (dragState == DragState.VERTEX) {
                dragState = DragState.NONE;
                application.setStatus("Поставлена вершина " + selectedVertex.getName());
                selectedVertex = null;
                mousePosition = null;
                application.canvasRepaint();
            } else {
                String name = placeVertex(event.getPoint().x, event.getPoint().y);
                application.setStatus("Добавлена вершина " + name);
                application.graphChanged();
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mousePosition = e.getPoint();
        application.canvasRepaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (dragState == DragState.VERTEX) {
            selectedVertex.setX(e.getX());
            selectedVertex.setY(e.getY());
        }
        application.canvasRepaint();
    }

    @Override
    public void mousePressed(MouseEvent event) {
        if (SwingUtilities.isLeftMouseButton(event)) {
            selectedVertex = FindVertex.byPoint(application.graph, event.getPoint());
            if (selectedVertex != null) {
                dragState = DragState.EDGE;
                mousePosition = event.getPoint();
                application.canvasRepaint();
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (selectedVertex != null) {
            Vertex target = FindVertex.byPoint(application.graph, e.getPoint());
            if (dragState == DragState.EDGE) {
                if (target != null && target != selectedVertex) {
                    String result = JOptionPane.showInputDialog(
                            application,
                            "Введите вес ребра:"
                    );
                    try {
                        int weight = Integer.parseInt(result);
                        if (weight < 0)
                            throw new Exception();
                        application.graph.addEdge(selectedVertex.getName(), target.getName(), weight);
                        application.graphChanged();
                        application.setStatus("Добавлено ребро с весом " + weight + " между вершинами " + selectedVertex.getName() + " и " + target.getName());
                    } catch (Exception ex) {
                        application.setStatus("Вес должен быть неотрицательным числом");
                    }
                }
                dragState = DragState.NONE;
                selectedVertex = null;
                mousePosition = null;
                application.repaint();
            }
        }
    }

    MouseController(Application application, Toolbar toolbar) {
        this.dragState = DragState.NONE;
        this.application = application;
        this.toolbar = toolbar;
    }
}
