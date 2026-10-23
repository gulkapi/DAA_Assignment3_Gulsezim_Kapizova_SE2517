package algorithms;

import graph.Graph;
import metrics.Metrics;
import java.util.*;

public final class Kosaraju {
    private Kosaraju() {}
    public static List<List<Integer>> run(Graph graph, Metrics metrics) {
        int n = graph.size();
        boolean[] seen = new boolean[n];
        List<Integer> order = new ArrayList<>(n);
        for (int start = 0; start < n; start++) {
            if (seen[start]) continue;
            Deque<int[]> stack = new ArrayDeque<>();
            seen[start] = true; stack.push(new int[]{start, 0}); metrics.updateMaxDepth(stack.size());
            while (!stack.isEmpty()) {
                int[] frame = stack.peek();
                List<Graph.Arc> adj = graph.neighbors(frame[0]);
                if (frame[1] < adj.size()) {
                    Graph.Arc arc = adj.get(frame[1]++); metrics.incrementEdgeScans();
                    if (!seen[arc.to]) { seen[arc.to] = true; stack.push(new int[]{arc.to, 0}); metrics.updateMaxDepth(stack.size()); }
                } else { order.add(frame[0]); stack.pop(); }
            }
        }
        Graph reverse = graph.reversed();
        Arrays.fill(seen, false);
        List<List<Integer>> components = new ArrayList<>();
        for (int i = order.size() - 1; i >= 0; i--) {
            int start = order.get(i);
            if (seen[start]) continue;
            List<Integer> component = new ArrayList<>();
            Deque<Integer> stack = new ArrayDeque<>(); stack.push(start); seen[start] = true; metrics.updateMaxDepth(stack.size());
            while (!stack.isEmpty()) {
                int v = stack.pop(); component.add(v);
                for (Graph.Arc arc : reverse.neighbors(v)) {
                    metrics.incrementEdgeScans();
                    if (!seen[arc.to]) { seen[arc.to] = true; stack.push(arc.to); metrics.updateMaxDepth(stack.size()); }
                }
            }
            components.add(component);
        }
        return components;
    }
}
