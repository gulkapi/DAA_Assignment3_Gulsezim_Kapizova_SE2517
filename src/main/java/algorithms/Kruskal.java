package algorithms;

import graph.Graph;
import metrics.Metrics;
import java.util.*;

public final class Kruskal {
    private Kruskal() {}
    public static MSTResult run(Graph graph, Metrics metrics) {
        List<Graph.Edge> sorted = new ArrayList<>(graph.getEdges());
        sorted.sort((a, b) -> { metrics.incrementComparisons(); return Integer.compare(a.getWeight(), b.getWeight()); });
        DSU dsu = new DSU(graph.size(), metrics);
        List<Graph.Edge> chosen = new ArrayList<>();
        long total = 0;
        for (Graph.Edge edge : sorted) {
            metrics.incrementEdgeScans();
            if (dsu.find(edge.getU()) != dsu.find(edge.getV())) {
                dsu.union(edge.getU(), edge.getV());
                chosen.add(edge); total += edge.getWeight();
            }
        }
        return new MSTResult(total, dsu.components(), chosen);
    }
}
