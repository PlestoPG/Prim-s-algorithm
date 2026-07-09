package model;
import java.util.Objects;
public class Edge {
    public enum State { DEFAULT, IN_MST, CONSIDERED }
    private final Vertex v1;
    private final Vertex v2;
    private final int weight;
    private State state;
    public Edge(Vertex v1, Vertex v2, int weight) {
        this.v1 = v1;
        this.v2 = v2;
        this.weight = weight;
        this.state = State.DEFAULT;
    }
    public Vertex getV1() { return v1; }
    public Vertex getV2() { return v2; }
    public int getWeight() { return weight; }
    public State getState() { return state; }
    public void setState(State state) { this.state = state; }
    public Vertex getOpposite(Vertex v) {
        if (v.equals(v1)) return v2;
        if (v.equals(v2)) return v1;
        throw new IllegalArgumentException();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        if (weight != edge.weight) return false;
        return (Objects.equals(v1, edge.v1) && Objects.equals(v2, edge.v2)) || 
               (Objects.equals(v1, edge.v2) && Objects.equals(v2, edge.v1));
    }
    @Override
    public int hashCode() {
        int h1 = Objects.hashCode(v1);
        int h2 = Objects.hashCode(v2);
        return Objects.hash(weight, Math.min(h1, h2), Math.max(h1, h2));
    }
}