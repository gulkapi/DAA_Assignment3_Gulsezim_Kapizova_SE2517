package algorithms;

import graph.Graph;
import java.util.List;

public class MSTResult {
    private final long totalWeight;
    private final int components;
    private final List<Graph.Edge> edges;
    public MSTResult(long totalWeight, int components, List<Graph.Edge> edges) {
        this.totalWeight = totalWeight; this.components = components; this.edges = List.copyOf(edges);
    }
    public long getTotalWeight() { return totalWeight; }
    public int getComponents() { return components; }
    public List<Graph.Edge> getEdges() { return edges; }
}
