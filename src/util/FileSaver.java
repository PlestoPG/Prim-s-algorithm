package util;
import model.Edge;
import model.Graph;
import java.io.FileWriter;
import java.io.IOException;

public class FileSaver {
    public static void save(String path, Graph graph, Boolean isDone) throws IOException {
        try (FileWriter writer = new FileWriter(path)) {
            writer.write(graph.getVertices().size() + "\n");
            for (Edge edge : graph.getEdges()) {
                String row = edge.getV1().getName() + " " +  edge.getV2().getName() + " " + edge.getWeight();
                if (isDone) {
                    Edge.State state = edge.getState();
                    row += " " + state;
                }
                writer.write(row + "\n");
            }
        }
    }
}
