package ui;

import model.Edge;
import model.Vertex;

import javax.swing.*;
import java.awt.*;

public class Canvas extends JPanel {
    Application application;
    JScrollPane scrollPane;
    MouseController controller;
    int offsetX = 0;
    int offsetY = 0;
    double scale = 1;

    int VERTEX_RADIUS = 20;
    int EDGE_WIDTH = 10;
    int DISTANCE_FROM_BORDER_TO_RESIZE = 50;
    int RESIZE_QUANTITY = 100;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        Stroke defaultStroke = g2.getStroke();
        g2.setStroke(new BasicStroke((float) (EDGE_WIDTH * scale), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));


        Font defaultFont = g.getFont();
        g.setFont(new Font(defaultFont.getName(), Font.BOLD, (int) (VERTEX_RADIUS * scale)));
        for (Edge edge : application.graph.getEdges()) {
            String text = edge.getWeight() + "";
            g.setColor(Color.BLUE);
            g.drawLine(
                    edge.getV1().getX() + offsetX,
                    edge.getV1().getY() + offsetY,
                    edge.getV2().getX() + offsetX,
                    edge.getV2().getY() + offsetY
            );

            int midX = (edge.getV1().getX() + edge.getV2().getX()) / 2;
            int midY = (edge.getV1().getY() + edge.getV2().getY()) / 2;

            FontMetrics fm = g.getFontMetrics();
            int textX = midX - fm.stringWidth(text) / 2;
            int textY = midY + (fm.getAscent() - fm.getDescent()) / 2;
            g.drawRect(
                    textX + offsetX,
                    midY - fm.getHeight() / 2 + offsetY,
                    fm.stringWidth(text),
                    fm.getHeight()
            );
            g.fillRect(
                    textX + offsetX,
                    midY - fm.getHeight() / 2 + offsetY,
                    fm.stringWidth(text),
                    fm.getHeight()
            );
            g.setColor(Color.WHITE);
            g.drawString(text, textX + offsetX, textY + offsetY);
        }

        if (controller.dragState == DragState.EDGE) {
            g.setColor(Color.BLUE);
            g.drawLine(
                    controller.selectedVertex.getX() + offsetX,
                    controller.selectedVertex.getY() + offsetY,
                    controller.mousePosition.x,
                    controller.mousePosition.y
            );
            g.setColor(Color.WHITE);
        }

        g2.setStroke(defaultStroke);
        for (Vertex vertex : application.graph.getVertices()) {
            g.setColor(Color.BLUE);
            g.fillOval(
                    (int) (vertex.getX() - VERTEX_RADIUS * scale + offsetX),
                    (int) (vertex.getY() - VERTEX_RADIUS * scale + offsetY),
                    (int) (VERTEX_RADIUS * 2 * scale),
                    (int) (VERTEX_RADIUS * 2 * scale)
            );
            g.setColor(Color.WHITE);

            FontMetrics fm = g.getFontMetrics();
            int textX = vertex.getX() - fm.stringWidth(vertex.getName()) / 2;
            int textY = vertex.getY() + (fm.getAscent() - fm.getDescent()) / 2;
            g.drawString(vertex.getName(), textX + offsetX, textY + offsetY);
        }
    }

    public void increaseSize(int x, int y) {
        int width = getSize().width, height = getSize().height;
        boolean updateWidth = false, updateHeight = false;
        boolean offsetXChanged = false, offsetYChanged = false;

        if (x < DISTANCE_FROM_BORDER_TO_RESIZE) {
            offsetX += RESIZE_QUANTITY;
            offsetXChanged = true;
            updateWidth = true;
        } else if (x > width - DISTANCE_FROM_BORDER_TO_RESIZE) {
            updateWidth = true;
        }
        if (y < DISTANCE_FROM_BORDER_TO_RESIZE) {
            offsetY += RESIZE_QUANTITY;
            offsetYChanged = true;
            updateHeight = true;
        } else if (y > height - DISTANCE_FROM_BORDER_TO_RESIZE) {
            updateHeight = true;
        }
        if (updateWidth || updateHeight) {
            JViewport viewport = scrollPane.getViewport();
            Point viewPosition = viewport.getViewPosition();
            setPreferredSize(new Dimension(
                    getWidth() + (updateWidth ? RESIZE_QUANTITY : 0),
                    getHeight() + (updateHeight ? RESIZE_QUANTITY : 0)
            ));
            revalidate();
            viewport.setViewPosition(new Point(
                    viewPosition.x + (offsetXChanged ? RESIZE_QUANTITY : 0),
                    viewPosition.y + (offsetYChanged ? RESIZE_QUANTITY : 0)
            ));
        }
    }

    void setJScrollPane(JScrollPane scrollPane) {
        this.scrollPane = scrollPane;

        scrollPane.addMouseWheelListener(e -> {
            if (e.isControlDown()) {
                scrollPane.setWheelScrollingEnabled(false);
                System.out.println();
                if (e.getPreciseWheelRotation() < -0.1) {
                    scale *= 1.02;
                } else if (e.getPreciseWheelRotation() > 0.1) {
                    scale /= 1.02;
                }
                repaint();
            } else {
                scrollPane.setWheelScrollingEnabled(true);
            }
        });
    }

    void initiateViewPoint() {
        scrollPane.getViewport().setViewPosition(new Point(
                (int) (getPreferredSize().width * 0.25),
                (int) (getPreferredSize().height * 0.25)
        ));
    }

    Canvas(Application application, Toolbar toolbar) {
        this.application = application;

        setBackground(Color.WHITE);

        // Работа по холсту с мышкой
        this.controller = new MouseController(application, this, toolbar);
        addMouseListener(controller);
        addMouseMotionListener(controller);
    }
}
