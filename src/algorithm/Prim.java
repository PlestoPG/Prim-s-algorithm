package algorithm;
import model.Edge;
import model.Graph;
import model.Vertex;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
public class Prim {
    private final Graph graph;
    private final PriorityQueue<Edge> pq;
    private final Set<Vertex> visited;
    private final List<Edge> mst;
    private int weight;
    private boolean done;
    public Prim(Graph graph, Vertex start) {
        this.graph = graph;
        this.pq = new PriorityQueue<>(Comparator.comparingInt(Edge::getWeight));
        this.visited = new HashSet<>();
        this.mst = new ArrayList<>();
        this.weight = 0;
        this.done = false;
        this.graph.reset();
        if (start != null) {
            visited.add(start);
            start.setState(Vertex.State.ACTIVE);
            for (Edge e : graph.getAdjacent(start)) {
                pq.add(e);
                e.setState(Edge.State.CONSIDERED);
            }
        }
    }
    public void step() {
        if (done || pq.isEmpty()) {
            done = true;
            clean();
            return;
        }
        for (Vertex v : visited) {
            if (v.getState() == Vertex.State.ACTIVE) {
                v.setState(Vertex.State.IN_MST);
            }
        }
        Edge edge = pq.poll();
        while (edge != null && visited.contains(edge.getV1()) && visited.contains(edge.getV2())) {
            if (edge.getState() != Edge.State.IN_MST) {
                edge.setState(Edge.State.DEFAULT);
            }
            edge = pq.poll();
        }
        if (edge == null) {
            done = true;
            clean();
            return;
        }
        Vertex v1 = edge.getV1();
        Vertex v2 = edge.getV2();
        Vertex next = visited.contains(v1) ? v2 : v1;
        edge.setState(Edge.State.IN_MST);
        mst.add(edge);
        weight += edge.getWeight();
        next.setState(Vertex.State.ACTIVE);
        visited.add(next);
        for (Edge e : graph.getAdjacent(next)) {
            if (!visited.contains(e.getOpposite(next))) {
                pq.add(e);
                e.setState(Edge.State.CONSIDERED);
            }
        }
        if (visited.size() == graph.getVertices().size()) {
            done = true;
            clean();
        }
    }
    public void run() {
        while (!done) {
            step();
        }
    }
    public boolean isDone() { return done; }
    public List<Edge> getMst() { return mst; }
    public int getWeight() { return weight; }
    private void clean() {
        for (Vertex v : visited) {
            v.setState(Vertex.State.IN_MST);
        }
        for (Edge e : graph.getEdges()) {
            if (e.getState() == Edge.State.CONSIDERED) {
                e.setState(Edge.State.DEFAULT);
            }
        }
    }
}
