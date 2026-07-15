package util;

import model.Graph;
import model.Vertex;
import ui.Canvas;

import java.awt.*;

public class FindVertex {
    public static Vertex byPoint(Graph graph, Point point) {
        for (Vertex v : graph.getVertices()) {
            Point vertexPoint = new Point(v.getX(), v.getY());
            double distance = vertexPoint.distance(point);
            if (distance <= Canvas.VERTEX_RADIUS)
                return v;
        }
        return null;
    }
}
