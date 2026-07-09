package ui;

import model.Edge;
import model.Vertex;

import javax.swing.*;
import java.awt.*;

public class Canvas extends JPanel {
    Application application;
    MouseController controller;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Edge edge : application.graph.getEdges()) {
            Color color;
            switch (edge.getState()) {
                case IN_MST ->  color = Color.GREEN;
                case CONSIDERED -> color = Color.CYAN;
                default -> color = Color.BLUE;
            }

            g.setColor(color);
            g.drawLine(edge.getV1().getX(), edge.getV1().getY(), edge.getV2().getX(), edge.getV2().getY());

            int midX = (edge.getV1().getX() + edge.getV2().getX()) / 2;
            int midY = (edge.getV1().getY() + edge.getV2().getY()) / 2;

            g.setColor(Color.BLACK);
            g.drawString(edge.getWeight() + "", midX - 4 * (edge.getWeight() + "").length(), midY);
        }

        if (controller.selectedVertex != null && controller.mousePosition != null) {
            g.setColor(Color.BLUE);
            g.drawLine(controller.selectedVertex.getX(), controller.selectedVertex.getY(), controller.mousePosition.x, controller.mousePosition.y);
        }

        for (Vertex vertex : application.graph.getVertices()) {
            Color color;
            switch (vertex.getState()) {
                case IN_MST ->  color = Color.GREEN;
                case ACTIVE -> color = Color.RED;
                default -> color = Color.BLUE;
            }

            g.setColor(color);
            g.fillOval(vertex.getX() - 15, vertex.getY() - 15, 30, 30);
            g.setColor(Color.WHITE);
            g.drawString(vertex.getName(), vertex.getX() - 4 * vertex.getName().length(), vertex.getY() + 5);
        }
    }

    Canvas(Application application, Toolbar toolbar) {
        this.application = application;

        setBackground(Color.WHITE);

        // Работа по холсту с мышкой
        this.controller = new MouseController(application, toolbar);
        addMouseListener(controller);
        addMouseMotionListener(controller);
    }
}
