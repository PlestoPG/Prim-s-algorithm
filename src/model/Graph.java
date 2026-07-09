package model;
import java.util.*;
public class Graph {
    private final Map<String, Vertex> vertexMap = new LinkedHashMap<>();
    private final Set<Edge> edgeSet = new LinkedHashSet<>();
    private final Map<Vertex, Set<Edge>> adjMap = new HashMap<>();
    public void addVertex(Vertex v) {
        if (vertexMap.containsKey(v.getName())) {
            throw new IllegalArgumentException("Vertex with this name already exists");
        }
        vertexMap.put(v.getName(), v);
        adjMap.put(v, new LinkedHashSet<>());
    }
    public void removeVertex(String name) {
        Vertex v = vertexMap.remove(name);
        if (v != null) {
            Set<Edge> incident = adjMap.remove(v);
            if (incident != null) {
                for (Edge e : incident) {
                    edgeSet.remove(e);
                    Vertex opp = e.getOpposite(v);
                    Set<Edge> oppEdges = adjMap.get(opp);
                    if (oppEdges != null) {
                        oppEdges.remove(e);
                    }
                }
            }
        }
    }
    public void addEdge(String n1, String n2, int w) {
        Vertex v1 = vertexMap.get(n1);
        Vertex v2 = vertexMap.get(n2);
        if (v1 != null && v2 != null) {
            Edge e = new Edge(v1, v2, w);
            edgeSet.add(e);
            adjMap.get(v1).add(e);
            adjMap.get(v2).add(e);
        }
    }
    public Set<Vertex> getVertices() {
        return new LinkedHashSet<>(vertexMap.values());
    }
    public Set<Edge> getEdges() {
        return new LinkedHashSet<>(edgeSet);
    }
    public Set<Edge> getAdjacent(Vertex v) {
        return adjMap.getOrDefault(v, Collections.emptySet());
    }
    public void reset() {
        vertexMap.values().forEach(v -> v.setState(Vertex.State.DEFAULT));
        edgeSet.forEach(e -> e.setState(Edge.State.DEFAULT));
    }
}