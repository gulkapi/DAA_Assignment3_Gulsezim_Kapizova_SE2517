package algorithms;

import graph.Graph;
import metrics.Metrics;
import java.util.*;

public final class Prim {
    private Prim() {}
    private static final class Candidate {
        int from, to, weight;
        Candidate(int from, int to, int weight) { this.from = from; this.to = to; this.weight = weight; }
    }
    public static MSTResult run(Graph graph, Metrics metrics) {
        int n = graph.size();
        boolean[] used = new boolean[n];
        PriorityQueue<Candidate> pq = new PriorityQueue<>((a,b) -> {
            metrics.incrementComparisons(); return Integer.compare(a.weight, b.weight);
        });
        List<Graph.Edge> chosen = new ArrayList<>();
        long total = 0; int components = 0;
        for (int start = 0; start < n; start++) {
            if (used[start]) continue;
            components++;
            used[start] = true;
            for (Graph.Arc arc : graph.neighbors(start)) { metrics.incrementEdgeScans(); pq.add(new Candidate(start, arc.to, arc.weight)); }
            while (!pq.isEmpty()) {
                Candidate c = pq.poll();
                if (used[c.to]) continue;
                used[c.to] = true;
                chosen.add(new Graph.Edge(c.from, c.to, c.weight)); total += c.weight;
                for (Graph.Arc arc : graph.neighbors(c.to)) {
                    metrics.incrementEdgeScans();
                    if (!used[arc.to]) pq.add(new Candidate(c.to, arc.to, arc.weight));
                }
            }
        }
        return new MSTResult(total, components, chosen);
    }
}
