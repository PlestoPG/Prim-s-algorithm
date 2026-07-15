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
    private Vertex start;
    private int weight;
    private boolean done;
    public Prim(Graph graph) {
        this.graph = graph;
        this.pq = new PriorityQueue<>(Comparator.comparingInt(Edge::getWeight));
        this.visited = new HashSet<>();
        this.mst = new ArrayList<>();
        this.weight = 0;
        this.done = false;
    }
    public void start(Vertex start) {
        graph.reset();
        pq.clear();
        visited.clear();
        mst.clear();
        weight = 0;
        done = false;
        if (start == null) return;
        visited.add(start);
        start.setState(Vertex.State.ACTIVE);
        for (Edge e : graph.getAdjacent(start)) {
            pq.add(e);
            e.setState(Edge.State.CONSIDERED);
        }
        checkDone();
    }
    public void step() {
        if (done) return;
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
            finish();
            return;
        }
        Vertex next = visited.contains(edge.getV1()) ? edge.getV2() : edge.getV1();
        edge.setState(Edge.State.IN_MST);
        next.setState(Vertex.State.ACTIVE);
        mst.add(edge);
        weight += edge.getWeight();
        visited.add(next);
        for (Edge e : graph.getAdjacent(next)) {
            if (!visited.contains(e.getOpposite(next))) {
                pq.add(e);
                e.setState(Edge.State.CONSIDERED);
            }
        }
        checkDone();
    }
    public void run() {
        while (!done) {
            step();
        }
    }
    private void checkDone() {
        if (visited.size() == graph.getVertices().size()) {
            finish();
        }
    }
    private void finish() {
        done = true;
        for (Vertex v : visited) {
            v.setState(Vertex.State.IN_MST);
        }
        for (Edge e : graph.getEdges()) {
            if (e.getState() == Edge.State.CONSIDERED) {
                e.setState(Edge.State.DEFAULT);
            }
        }
    }
    public boolean isDone() { return done; }
    public List<Edge> getMst() { return mst; }
    public int getWeight() { return weight; }
}
