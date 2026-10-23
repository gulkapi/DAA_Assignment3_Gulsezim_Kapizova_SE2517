package benchmark;

import algorithms.*;
import graph.Graph;
import metrics.Metrics;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Benchmark {
    public static void main(String[] args) throws Exception {
        Path out = Path.of("results", "results.csv");
        Files.createDirectories(out.getParent());
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(out))) {
            writer.println("algorithm,graph,n,m,time_ms,dsu_ops,edge_scans,comparisons,max_depth,result");
            int[] sizes = {1000, 3162, 10000, 31623, 100000};
            for (int n : sizes) for (int factor : new int[]{4, 20}) {
                int m = n * factor;
                Graph undirected = generate(n, m, false, true, 42);
                Graph directed = generate(n, m, true, false, 42);
                Graph dag = generateDag(n, m, 42);
                record(writer, "Kruskal", "undirected", undirected, () -> Kruskal.run(undirected, new Metrics()).getTotalWeight(), "mst");
                record(writer, "Prim", "undirected", undirected, () -> Prim.run(undirected, new Metrics()).getTotalWeight(), "mst");
                record(writer, "Kosaraju", "directed", directed, () -> Kosaraju.run(directed, new Metrics()).size(), "scc");
                record(writer, "Kahn", "dag", dag, () -> Kahn.sort(dag, new Metrics()).size(), "order");
                record(writer, "DagShortestPath", "dag", dag, () -> DagShortestPath.run(dag, new Metrics()).sumDistances(), "distance");
            }
        }
        System.out.println("Benchmark written to " + out.toAbsolutePath());
    }
    private interface Task { long run(); }

    private static void record(PrintWriter w, String name, String kind,
                               Graph g, Task task, String type) {
        long[] times = new long[5];
        long result = 0;
        Metrics metrics = null;

        for (int i = 0; i < 5; i++) {
            Metrics current = new Metrics();

            long start = System.nanoTime();

            long currentResult;

            if (name.equals("Kruskal")) {
                currentResult = Kruskal.run(g, current).getTotalWeight();
            } else if (name.equals("Prim")) {
                currentResult = Prim.run(g, current).getTotalWeight();
            } else if (name.equals("Kosaraju")) {
                currentResult = Kosaraju.run(g, current).size();
            } else if (name.equals("Kahn")) {
                currentResult = Kahn.sort(g, current).size();
            } else {
                currentResult = DagShortestPath.run(g, current).sumDistances();
            }

            times[i] = System.nanoTime() - start;

            if (i == 0) {
                result = currentResult;
                metrics = current;
            } else if (currentResult != result) {
                throw new IllegalStateException(
                        name + " produced inconsistent results"
                );
            }
        }

        Arrays.sort(times);
        double medianMs = times[2] / 1_000_000.0;

        w.printf(Locale.ROOT,
                "%s,%s,%d,%d,%.3f,%d,%d,%d,%d,%d%n",
                name, kind, g.size(), g.getEdges().size(),
                medianMs,
                metrics.getDsuOps(),
                metrics.getEdgeScans(),
                metrics.getComparisons(),
                metrics.getMaxDepth(),
                result
        );
    }

    private static Graph generate(int n, int m, boolean directed, boolean spanningTree, long seed) {
        Random rnd = new Random(seed);
        Graph g = new Graph(n, directed);
        if (spanningTree && n > 1) {
            int[] perm = new int[n]; for (int i=0;i<n;i++) perm[i]=i;
            for (int i=n-1;i>0;i--) { int j=rnd.nextInt(i+1), t=perm[i]; perm[i]=perm[j]; perm[j]=t; }
            for (int i=1;i<n;i++) g.addEdge(perm[rnd.nextInt(i)], perm[i], 1+rnd.nextInt(1000));
        }
        while (g.getEdges().size() < m) {
            int a = rnd.nextInt(n), b = rnd.nextInt(n);
            if (a == b) continue;
            g.addEdge(a, b, 1+rnd.nextInt(1000));
        }
        return g;
    }

    private static Graph generateDag(int n, int m, long seed) {
        Random rnd = new Random(seed);

        int[] rank = new int[n];
        for (int i = 0; i < n; i++) {
            rank[i] = i;
        }

        for (int i = n - 1; i > 0; i--) {
            int j = rnd.nextInt(i + 1);
            int temp = rank[i];
            rank[i] = rank[j];
            rank[j] = temp;
        }

        Graph dag = new Graph(n, true);

        while (dag.getEdges().size() < m) {
            int a = rnd.nextInt(n);
            int b = rnd.nextInt(n);

            if (a == b) {
                continue;
            }

            if (rank[a] > rank[b]) {
                int temp = a;
                a = b;
                b = temp;
            }

            int weight = 1 + rnd.nextInt(1000);
            dag.addEdge(a, b, weight);
        }

        return dag;
    }

}
