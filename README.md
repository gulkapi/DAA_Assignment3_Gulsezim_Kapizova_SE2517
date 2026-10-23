# DAA Assignment 3 — Graph Algorithms

## Requirements
- Java 17+
- Maven 3.9+

## Build and test
```bash
mvn clean test
```

## Run the benchmark
```bash
mvn -q -DskipTests package
java -cp target/classes benchmark.Benchmark
```
The benchmark writes `results/results.csv`. Run it on a machine with enough free memory; the largest dense graph has 100,000 vertices and 2,000,000 edges.

## Input graph format
The first line is `n m directed|undirected`; each following line contains `u v weight`. Vertices are numbered from `0` to `n-1`.

## Algorithms
- DSU uses path compression and union by rank.
- Kruskal sorts edges and uses DSU to avoid cycles.
- Prim grows a minimum spanning forest with a priority queue.
- Kosaraju uses iterative DFS passes.
- Kahn's algorithm returns a topological order and throws an exception if a cycle exists.
- DAG shortest paths relax edges in topological order and preserve parent pointers for path reconstruction.

## Metrics
Counters must be incremented where the operation occurs in the algorithm. `dsuOps` counts DSU operations, `edgeScans` counts edge visits, `comparisons` counts weight comparisons, and `maxDepth` records the maximum explicit DFS stack size. `time_ms` is machine-dependent and should be interpreted as supporting information rather than the main complexity evidence.

## AI-use acknowledgement
AI assistance was used to help draft and review parts of the project. The submitted implementation and benchmark results should be run, checked, and understood by the student before submission.

## Generate plots
Install Python 3 and matplotlib, then run:
```bash
python3 -m pip install matplotlib
python3 scripts/plot_results.py
```
The plots are written to `results/plots/`.
