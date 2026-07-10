package ui;

import model.Edge;
import model.Vertex;

import javax.swing.*;
import java.awt.*;

public class Canvas extends JPanel {
    Application application;
    MouseController controller;

    int VERTEX_RADIUS = 20;
    int EDGE_WIDTH = 10;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        Stroke defaultStroke = g2.getStroke();
        g2.setStroke(new BasicStroke(EDGE_WIDTH, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));


        Font defaultFont = g.getFont();
        g.setFont(new Font(defaultFont.getName(), Font.BOLD, VERTEX_RADIUS));
        for (Edge edge : application.graph.getEdges()) {
            String text = edge.getWeight() + "";
            g.setColor(Color.BLUE);
            g.drawLine(
                    edge.getV1().getX(),
                    edge.getV1().getY(),
                    edge.getV2().getX(),
                    edge.getV2().getY()
            );

            int midX = (edge.getV1().getX() + edge.getV2().getX()) / 2;
            int midY = (edge.getV1().getY() + edge.getV2().getY()) / 2;

            FontMetrics fm = g.getFontMetrics();
            int textX = midX - fm.stringWidth(text) / 2;
            int textY = midY + (fm.getAscent() - fm.getDescent()) / 2;
            g.drawRect(textX, midY - fm.getHeight() / 2, fm.stringWidth(text), fm.getHeight());
            g.fillRect(textX, midY - fm.getHeight() / 2, fm.stringWidth(text), fm.getHeight());
            g.setColor(Color.WHITE);
            g.drawString(text, textX, textY);
        }

        if (controller.dragState == DragState.EDGE) {
            g.setColor(Color.BLUE);
            g.drawLine(
                    controller.selectedVertex.getX(),
                    controller.selectedVertex.getY(),
                    controller.mousePosition.x,
                    controller.mousePosition.y
            );
            g.setColor(Color.WHITE);
        }

        g2.setStroke(defaultStroke);
        for (Vertex vertex : application.graph.getVertices()) {
            g.setColor(Color.BLUE);
            g.fillOval(
                    vertex.getX() - VERTEX_RADIUS,
                    vertex.getY() - VERTEX_RADIUS,
                    VERTEX_RADIUS * 2,
                    VERTEX_RADIUS * 2
            );
            g.setColor(Color.WHITE);

            FontMetrics fm = g.getFontMetrics();
            int textX = vertex.getX() - fm.stringWidth(vertex.getName()) / 2;
            int textY = vertex.getY() + (fm.getAscent() - fm.getDescent()) / 2;
            g.drawString(vertex.getName(), textX, textY);
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
