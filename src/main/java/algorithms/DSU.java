package algorithms;

import metrics.Metrics;
import java.util.Arrays;

public class DSU {
    private final int[] parent, rank;
    private int components;
    private final Metrics metrics;
    public DSU(int n, Metrics metrics) {
        if (n < 0) throw new IllegalArgumentException("n must be non-negative");
        this.metrics = metrics; parent = new int[n]; rank = new int[n]; components = n;
        for (int i = 0; i < n; i++) parent[i] = i;
    }
    public int find(int x) {
        metrics.incrementDsuOps();
        if (parent[x] != x) parent[x] = find(parent[x]);
        return parent[x];
    }
    public boolean union(int a, int b) {
        int ra = find(a), rb = find(b);
        if (ra == rb) return false;
        metrics.incrementDsuOps();
        if (rank[ra] < rank[rb]) parent[ra] = rb;
        else if (rank[ra] > rank[rb]) parent[rb] = ra;
        else { parent[rb] = ra; rank[ra]++; }
        components--;
        return true;
    }
    public int components() { return components; }
}
