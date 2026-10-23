package graph;

import metrics.Metrics;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Graph {
    public static final class Edge {
        private final int u, v, weight;
        public Edge(int u, int v, int weight) { this.u = u; this.v = v; this.weight = weight; }
        public int getU() { return u; }
        public int getV() { return v; }
        public int getWeight() { return weight; }
    }
    public static final class Arc {
        public final int to, weight;
        public Arc(int to, int weight) { this.to = to; this.weight = weight; }
    }
    private final int n;
    private final boolean directed;
    private final List<List<Arc>> adjacency;
    private final List<Edge> edges = new ArrayList<>();

    public Graph(int n, boolean directed) {
        if (n < 0) throw new IllegalArgumentException("Number of vertices cannot be negative");
        this.n = n; this.directed = directed;
        adjacency = new ArrayList<>();
        for (int i = 0; i < n; i++) adjacency.add(new ArrayList<>());
    }
    public int size() { return n; }
    public boolean isDirected() { return directed; }
    public List<Edge> getEdges() { return Collections.unmodifiableList(edges); }
    public List<Arc> neighbors(int u) { return Collections.unmodifiableList(adjacency.get(u)); }
    public void addEdge(int u, int v, int w) {
        checkVertex(u); checkVertex(v);
        edges.add(new Edge(u, v, w));
        adjacency.get(u).add(new Arc(v, w));
        if (!directed) adjacency.get(v).add(new Arc(u, w));
    }
    public int inDegree(int v) {
        int degree = 0;
        for (Edge e : edges) if (e.v == v) degree++;
        if (!directed) for (Edge e : edges) if (e.u == v && e.u != e.v) degree++;
        return degree;
    }
    public Graph reversed() {
        Graph reverse = new Graph(n, true);
        for (Edge e : edges) reverse.addEdge(e.v, e.u, e.weight);
        return reverse;
    }
    public static Graph read(Path path) throws IOException {
        List<String> lines = Files.readAllLines(path);
        String[] header = lines.get(0).trim().split("\\s+");
        int n = Integer.parseInt(header[0]);
        boolean directed = header[2].equalsIgnoreCase("directed");
        Graph graph = new Graph(n, directed);
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty() || line.startsWith("#")) continue;
            String[] p = line.split("\\s+");
            graph.addEdge(Integer.parseInt(p[0]), Integer.parseInt(p[1]), Integer.parseInt(p[2]));
        }
        return graph;
    }
    private void checkVertex(int v) {
        if (v < 0 || v >= n) throw new IndexOutOfBoundsException("Invalid vertex: " + v);
    }
}
