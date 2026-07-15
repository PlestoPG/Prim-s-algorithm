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
    public enum Stage { FIND_MIN, ADD_TO_TREE, UPDATE_EDGES, FINISHED }
    private final Graph graph;
    private final PriorityQueue<Edge> pq;
    private final Set<Vertex> visited;
    private final List<Edge> mst;
    private int weight;
    private boolean done;
    private Stage stage;
    private String msg;
    private Edge currEdge;
    private Vertex currVert;
    public Prim(Graph graph) {
        this.graph = graph;
        this.pq = new PriorityQueue<>(Comparator.comparingInt(Edge::getWeight));
        this.visited = new HashSet<>();
        this.mst = new ArrayList<>();
        this.weight = 0;
        this.done = false;
        this.stage = Stage.FINISHED;
        this.msg = "";
    }
    public void start(Vertex start) {
        graph.reset();
        pq.clear();
        visited.clear();
        mst.clear();
        weight = 0;
        done = false;
        if (start == null) return;
        currVert = start;
        visited.add(start);
        start.setState(Vertex.State.ACTIVE);
        for (Edge e : graph.getAdjacent(start)) {
            pq.add(e);
            e.setState(Edge.State.CONSIDERED);
        }
        msg = "Начало работы. Стартовая вершина: " + start.getName();
        stage = visited.size() == graph.getVertices().size() ? Stage.FINISHED : Stage.FIND_MIN;
    }
    public void step() {
        if (done) return;
        switch (stage) {
            case FIND_MIN:
                findMin();
                break;
            case ADD_TO_TREE:
                addTree();
                break;
            case UPDATE_EDGES:
                updateEdges();
                break;
            case FINISHED:
                finish();
                break;
        }
    }
    private void findMin() {
        Edge edge = pq.poll();
        while (edge != null && visited.contains(edge.getV1()) && visited.contains(edge.getV2())) {
            if (edge.getState() != Edge.State.IN_MST) {
                edge.setState(Edge.State.DEFAULT);
            }
            edge = pq.poll();
        }
        if (edge == null) {
            done = true;
            msg = "Граф несвязный. Построение остановлено. Суммарный вес: " + weight;
            cleanUp();
            return;
        }
        currEdge = edge;
        msg = "Рассматриваем ребро " + edgeStr(currEdge) + " (вес " + currEdge.getWeight() + ") — минимальное";
        stage = Stage.ADD_TO_TREE;
    }
    private void addTree() {
        for (Vertex v : visited) {
            if (v.getState() == Vertex.State.ACTIVE) {
                v.setState(Vertex.State.IN_MST);
            }
        }
        currVert = visited.contains(currEdge.getV1()) ? currEdge.getV2() : currEdge.getV1();
        currEdge.setState(Edge.State.IN_MST);
        currVert.setState(Vertex.State.ACTIVE);
        mst.add(currEdge);
        weight += currEdge.getWeight();
        visited.add(currVert);
        msg = "Добавляем вершину " + currVert.getName() + " и ребро " + edgeStr(currEdge) + " в дерево";
        stage = visited.size() == graph.getVertices().size() ? Stage.FINISHED : Stage.UPDATE_EDGES;
    }
    private void updateEdges() {
        for (Edge e : graph.getAdjacent(currVert)) {
            if (!visited.contains(e.getOpposite(currVert))) {
                pq.add(e);
                e.setState(Edge.State.CONSIDERED);
            }
        }
        List<Edge> temp = new ArrayList<>(pq);
        temp.sort(Comparator.comparingInt(Edge::getWeight));
        List<String> list = new ArrayList<>();
        for (Edge e : temp) {
            if (!visited.contains(e.getV1()) || !visited.contains(e.getV2())) {
                list.add(edgeStr(e) + " (" + e.getWeight() + ")");
            }
        }
        msg = "Теперь доступны рёбра: " + String.join(", ", list);
        stage = Stage.FIND_MIN;
    }
    public void run() {
        while (!done) {
            step();
        }
    }
    private void finish() {
        done = true;
        msg = "Минимальное остовное дерево построено! Суммарный вес: " + weight;
        cleanUp();
    }
    private void cleanUp() {
        for (Vertex v : visited) {
            v.setState(Vertex.State.IN_MST);
        }
        for (Edge e : graph.getEdges()) {
            if (e.getState() == Edge.State.CONSIDERED) {
                e.setState(Edge.State.DEFAULT);
            }
        }
    }
    private String edgeStr(Edge e) {
        String n1 = e.getV1().getName();
        String n2 = e.getV2().getName();
        return n1.compareTo(n2) < 0 ? n1 + "-" + n2 : n2 + "-" + n1;
    }
    public boolean isDone() { return done; }
    public List<Edge> getMst() { return mst; }
    public int getWeight() { return weight; }
    public String getMsg() { return msg; }
}
