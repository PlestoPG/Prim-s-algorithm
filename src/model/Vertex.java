package model;
import java.util.Objects;
public class Vertex {
    public enum State { DEFAULT, IN_MST, ACTIVE }
    private final String name;
    private int x;
    private int y;
    private State state;
    public Vertex(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.state = State.DEFAULT;
    }
    public String getName() { return name; }
    public int getX() { return x; }
    public int getY() { return y; }
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public State getState() { return state; }
    public void setState(State state) { this.state = state; }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertex vertex = (Vertex) o;
        return Objects.equals(name, vertex.name);
    }
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
