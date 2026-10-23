package metrics;

public class Metrics {
    private long dsuOps;
    private long edgeScans;
    private long comparisons;
    private int maxDepth;

    public void incrementDsuOps() { dsuOps++; }
    public void incrementEdgeScans() { edgeScans++; }
    public void incrementComparisons() { comparisons++; }
    public void updateMaxDepth(int depth) { maxDepth = Math.max(maxDepth, depth); }
    public long getDsuOps() { return dsuOps; }
    public long getEdgeScans() { return edgeScans; }
    public long getComparisons() { return comparisons; }
    public int getMaxDepth() { return maxDepth; }
    public void reset() { dsuOps = 0; edgeScans = 0; comparisons = 0; maxDepth = 0; }
}
