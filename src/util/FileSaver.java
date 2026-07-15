package util;
import model.Edge;
import model.Graph;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
public class FileSaver {
    public static void save(String path, List<Edge> mst, int weight) throws IOException {
        try (FileWriter writer = new FileWriter(path)) {
            writer.write("Минимальное остовное дерево:\n");
            for (Edge e : mst) {
                writer.write(e.getV1().getName() + " - " + e.getV2().getName() + " (" + e.getWeight() + ")\n");
            }
            writer.write("Суммарный вес: " + weight + "\n");
        }
    }

    public static void save(String path, Graph graph) throws IOException {
        try (FileWriter writer = new FileWriter(path)) {
            writer.write(graph.getVertices().size() + "\n");
            for (Edge e : graph.getEdges())
                writer.write(e.getV1().getName() + " " + e.getV2().getName() + " " + e.getWeight() + "\n");
        }
    }
}
