package algorithms;

import graph.Graph;
import metrics.Metrics;
import java.util.*;

public final class Kahn {
    private Kahn() {}
    public static List<Integer> sort(Graph graph, Metrics metrics) {
        int n = graph.size();
        int[] indegree = new int[n];
        for (Graph.Edge edge : graph.getEdges()) indegree[edge.getV()]++;
        if (!graph.isDirected()) {
            for (Graph.Edge edge : graph.getEdges()) if (edge.getU() != edge.getV()) indegree[edge.getU()]++;
        }
        Deque<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < n; i++) if (indegree[i] == 0) queue.add(i);
        List<Integer> order = new ArrayList<>(n);
        while (!queue.isEmpty()) {
            int u = queue.remove(); order.add(u);
            for (Graph.Arc arc : graph.neighbors(u)) {
                metrics.incrementEdgeScans();
                if (--indegree[arc.to] == 0) queue.add(arc.to);
            }
        }
        if (order.size() != n) throw new IllegalArgumentException("Graph contains a cycle");
        return order;
    }
}
