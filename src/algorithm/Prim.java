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
    private final List<Edge> mst;
    private int weight;
    public Prim(Graph graph) {
        this.graph = graph;
        this.mst = new ArrayList<>();
        this.weight = 0;
    }
    public void run(Vertex start) {
        graph.reset();
        mst.clear();
        weight = 0;
        if (start == null) return;
        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingInt(Edge::getWeight));
        Set<Vertex> visited = new HashSet<>();
        visited.add(start);
        start.setState(Vertex.State.IN_MST);
        for (Edge e : graph.getAdjacent(start)) {
            pq.add(e);
        }
        while (!pq.isEmpty() && visited.size() < graph.getVertices().size()) {
            Edge edge = pq.poll();
            if (visited.contains(edge.getV1()) && visited.contains(edge.getV2())) continue;
            Vertex next = visited.contains(edge.getV1()) ? edge.getV2() : edge.getV1();
            edge.setState(Edge.State.IN_MST);
            next.setState(Vertex.State.IN_MST);
            mst.add(edge);
            weight += edge.getWeight();
            visited.add(next);
            for (Edge e : graph.getAdjacent(next)) {
                if (!visited.contains(e.getOpposite(next))) {
                    pq.add(e);
                }
            }
        }
    }
    public List<Edge> getMst() { return mst; }
    public int getWeight() { return weight; }
}
