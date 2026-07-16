package util;

import model.Graph;
import model.Vertex;
import ui.Canvas;

import java.awt.*;

public class FindVertex {
    public static Vertex byPoint(Canvas canvas, Graph graph, Point point) {
        for (Vertex v : graph.getVertices()) {
            Point vertexPoint = new Point(v.getX() + canvas.offsetX, v.getY() + canvas.offsetY);
            double distance = vertexPoint.distance(point);
            if (distance <= Canvas.VERTEX_RADIUS * canvas.scale)
                return v;
        }
        return null;
    }
}
