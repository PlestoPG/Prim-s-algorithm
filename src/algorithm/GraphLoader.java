package algorithm;
import model.Graph;
import model.Vertex;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
public class GraphLoader {
    public static Graph load(String path) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line = reader.readLine();
            if (line == null) {
                throw new IllegalArgumentException("Файл пуст");
            }
            int count;
            try {
                count = Integer.parseInt(line.trim());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Некорректный формат количества вершин");
            }
            if (count <= 0) {
                throw new IllegalArgumentException("Количество вершин должно быть положительным");
            }
            Graph graph = new Graph();
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                String[] parts = line.split("\\s+");
                if (parts.length != 3) {
                    throw new IllegalArgumentException("Некорректный формат ребра");
                }
                String n1 = parts[0];
                String n2 = parts[1];
                int weight;
                try {
                    weight = Integer.parseInt(parts[2]);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Вес ребра должен быть числом");
                }
                if (weight <= 0) {
                    throw new IllegalArgumentException("Вес ребра должен быть положительным");
                }
                Vertex v1 = null;
                for (Vertex v : graph.getVertices()) {
                    if (v.getName().equals(n1)) {
                        v1 = v;
                        break;
                    }
                }
                if (v1 == null) {
                    v1 = new Vertex(n1, 0, 0);
                    graph.addVertex(v1);
                }
                Vertex v2 = null;
                for (Vertex v : graph.getVertices()) {
                    if (v.getName().equals(n2)) {
                        v2 = v;
                        break;
                    }
                }
                if (v2 == null) {
                    v2 = new Vertex(n2, 0, 0);
                    graph.addVertex(v2);
                }
                graph.addEdge(n1, n2, weight);
            }
            return graph;
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Файл не найден: " + path);
        }
    }
}
