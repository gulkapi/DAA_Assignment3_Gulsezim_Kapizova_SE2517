package algorithms;

import graph.Graph;
import metrics.Metrics;
import java.util.*;

public final class DagShortestPath {
    private DagShortestPath() {}
    public static final class Result {
        private final long[] distances;
        private final int[] parent;
        Result(long[] distances, int[] parent) { this.distances = distances; this.parent = parent; }
        public long[] getDistances() { return distances.clone(); }
        public int[] getParent() { return parent.clone(); }
        public List<Integer> pathTo(int target) {
            List<Integer> path = new ArrayList<>();
            if (distances[target] == Long.MAX_VALUE) return path;
            for (int v = target; v != -1; v = parent[v]) path.add(v);
            Collections.reverse(path); return path;
        }
        public long sumDistances() {
            long sum = 0;
            for (long d : distances) if (d != Long.MAX_VALUE) sum += d;
            return sum;
        }
    }
    public static Result run(Graph graph, Metrics metrics) {
        List<Integer> order = Kahn.sort(graph, new Metrics());
        int n = graph.size();
        long[] dist = new long[n]; int[] parent = new int[n];
        Arrays.fill(dist, Long.MAX_VALUE); Arrays.fill(parent, -1);
        boolean[] hasIncoming = new boolean[n];
        for (Graph.Edge e : graph.getEdges()) hasIncoming[e.getV()] = true;
        if (!graph.isDirected()) for (Graph.Edge e : graph.getEdges()) hasIncoming[e.getU()] = true;
        for (int i = 0; i < n; i++) if (!hasIncoming[i]) dist[i] = 0;
        for (int u : order) {
            if (dist[u] == Long.MAX_VALUE) continue;
            for (Graph.Arc arc : graph.neighbors(u)) {
                metrics.incrementEdgeScans();
                long candidate = dist[u] + arc.weight;
                if (candidate < dist[arc.to]) { dist[arc.to] = candidate; parent[arc.to] = u; }
            }
        }
        return new Result(dist, parent);
    }
}
