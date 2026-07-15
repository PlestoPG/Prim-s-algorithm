package ui;

import model.Vertex;
import util.FindVertex;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StartMouseController extends MouseAdapter {
    Application application;
    Canvas canvas;

    @Override
    public void mouseClicked(MouseEvent event) {
        Vertex vertex = FindVertex.byPoint(application.graph, event.getPoint());
        if (vertex == null)
            return;
        application.setStartVertex(vertex);
        canvas.resetMouseController();
    }

    StartMouseController(Application application, Canvas canvas) {
        this.application = application;
        this.canvas = canvas;
    }
}
