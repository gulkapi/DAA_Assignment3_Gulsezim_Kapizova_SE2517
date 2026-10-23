package algorithms;

import graph.Graph;
import metrics.Metrics;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class GraphAlgorithmsTest {
    @Test void kruskalAndPrimAgree() {
        Graph g = new Graph(4, false);
        g.addEdge(0,1,4); g.addEdge(0,2,1); g.addEdge(2,1,2); g.addEdge(1,3,1); g.addEdge(2,3,5);
        assertEquals(Kruskal.run(g, new Metrics()).getTotalWeight(), Prim.run(g, new Metrics()).getTotalWeight());
        assertEquals(3, Kruskal.run(g, new Metrics()).getEdges().size());
    }
    @Test void disconnectedMstReturnsForest() {
        Graph g = new Graph(4, false); g.addEdge(0,1,3); g.addEdge(2,3,2);
        assertEquals(2, Kruskal.run(g, new Metrics()).getComponents());
        assertEquals(2, Prim.run(g, new Metrics()).getComponents());
    }
    @Test void dsuCountsComponents() {
        DSU dsu = new DSU(5, new Metrics()); dsu.union(0,1); dsu.union(1,2);
        assertEquals(3, dsu.components()); assertEquals(dsu.find(0), dsu.find(2));
    }
    @Test void kahnDetectsCycle() {
        Graph g = new Graph(3, true); g.addEdge(0,1,1); g.addEdge(1,2,1); g.addEdge(2,0,1);
        assertThrows(IllegalArgumentException.class, () -> Kahn.sort(g, new Metrics()));
    }
    @Test void dagPathsAllowNegativeWeights() {
        Graph g = new Graph(4, true); g.addEdge(0,1,2); g.addEdge(0,2,5); g.addEdge(1,2,-4); g.addEdge(2,3,3);
        DagShortestPath.Result r = DagShortestPath.run(g, new Metrics());
        assertArrayEquals(new long[]{0,2,-2,1}, r.getDistances());
        assertEquals(List.of(0,1,2,3), r.pathTo(3));
    }
    @Test void kosarajuFindsStrongComponents() {
        Graph g = new Graph(4, true); g.addEdge(0,1,1); g.addEdge(1,0,1); g.addEdge(1,2,1); g.addEdge(2,3,1); g.addEdge(3,2,1);
        assertEquals(2, Kosaraju.run(g, new Metrics()).size());
    }
}
