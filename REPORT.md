# Assignment 3 Report: Graph Algorithms

## 1. Complexity table

| Algorithm | Time complexity | Extra space | Justification |
|---|---|---|---|
| DSU `find` / `union` | Amortized `O(α(V))` per operation | `O(V)` total | Union by rank and path compression keep the trees shallow. |
| Kruskal | `O(E log E + E α(V))` | `O(V + E)` | Sorting edges dominates; DSU checks whether an edge creates a cycle. |
| Prim with a priority queue | `O((V+E) log V)` in the usual adjacency-list analysis | `O(V+E)` | Each edge can add a queue candidate and priority-queue operations are logarithmic. |
| Kosaraju | `O(V+E)` | `O(V+E)` | Two DFS passes inspect each vertex and edge a constant number of times. |
| Kahn | `O(V+E)` | `O(V)` | In-degrees are computed and each vertex/edge is processed a constant number of times. |
| DAG shortest paths | `O(V+E)` | `O(V)` beyond the graph | Topological sorting and one relaxation pass are linear. |

## 2. Correctness arguments

### Cut property and MST
For any cut that separates a graph's vertices into two sets, a minimum-weight edge crossing that cut is safe for a minimum spanning tree. If an optimal spanning tree does not contain that edge, adding it creates a cycle. The cycle must contain another edge crossing the same cut. Replacing that edge with the lighter edge does not increase the tree's total weight. Kruskal repeatedly chooses the lightest edge that joins different components, so it avoids cycles while building a minimum spanning forest. Prim chooses a lightest edge from the current tree to an unvisited vertex, which is also a minimum edge crossing the corresponding cut. Therefore both algorithms return the same minimum total weight for the same graph, although the selected edges may differ when weights tie.

### DAG shortest paths by topological induction
A topological order places every vertex before all of its outgoing neighbors. Every predecessor of a vertex is therefore processed before that vertex. Assume distances are correct for all vertices processed before the current vertex. Relaxing each outgoing edge propagates the best known distance to its destination. By the time a vertex is processed, all possible incoming paths from earlier vertices have already been considered, so its shortest distance is final. Each edge is relaxed once, after topological ordering, giving `O(V+E)` time. Negative edge weights are safe because a DAG has no directed cycles, so a path cannot loop indefinitely to reduce its cost. In a general graph, negative cycles can make shortest paths undefined.

## 3. Which algorithm when?

| Situation | Recommended choice | Why |
|---|---|---|
| Sparse undirected weighted graph, edge list | Kruskal | Sorting edges and DSU are a natural fit. |
| Adjacency-list graph where the MST is grown from a start vertex | Prim | Priority queue selects the next light edge. |
| Need strongly connected components in a directed graph | Kosaraju | Two linear graph traversals find all SCCs. |
| Need a topological order and cycle detection | Kahn | A result shorter than `V` signals a cycle. |
| DAG with possibly negative edge weights | DAG shortest paths | Topological relaxation is linear and negative weights are safe. |
| Disconnected undirected graph | Kruskal or Prim forest | Both should return a minimum spanning forest and component count. |

## 4. Counters vs. formulas

Use `results/results.csv` to calculate ratios for each graph size. For linear traversals, compare `edge_scans / (V + E)` or `edge_scans / E`, using the precise counter definition documented in the implementation. For Kruskal, compare DSU work to `E` and weight comparisons to the expected sorting scale `E log E`. Ratios that remain approximately stable support the expected complexity class. Exact ratios depend on what the implementation counts, so counters must be incremented at the operation site and the same definition must be used for every run.

## 5. Runtime observations

`time_ms` is affected by the CPU, JVM version, garbage collection, background processes and warm-up. Use the median of five runs for the final timing column. The operation counters are more reproducible than wall-clock time and should be the main evidence for the complexity discussion.

## 6. Plots and results

Generate the three required plots from the benchmark CSV: operations versus `n` on log-log axes, scans per edge versus `n`, and Kruskal edge-scan share for sparse and dense graphs. Discuss the fitted slope of the traversal edge-scan curves and compare it with the expected linear slope of approximately `1`. Attach the actual plots and measured ratios after running and validating the benchmark.
