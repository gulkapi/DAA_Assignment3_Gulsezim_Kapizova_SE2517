import csv
import os
import math
import matplotlib.pyplot as plt

CSV_PATH = "results/results.csv"
OUT_DIR = "results/plots"
os.makedirs(OUT_DIR, exist_ok=True)

with open(CSV_PATH, newline="", encoding="utf-8") as f:
    rows = list(csv.DictReader(f))

for row in rows:
    for key in ("n", "m", "edge_scans", "dsu_ops", "comparisons"):
        row[key] = int(row[key])

# Plot 1: operation counters versus n, grouped by algorithm and density.
plt.figure()
for algorithm in sorted({r["algorithm"] for r in rows}):
    for density in ("sparse", "dense"):
        selected = [r for r in rows if r["algorithm"] == algorithm and
                    (r["m"] == 4 * r["n"] if density == "sparse" else r["m"] == 20 * r["n"])]
        if selected:
            selected.sort(key=lambda r: r["n"])
            plt.plot([r["n"] for r in selected],
                     [max(1, r["dsu_ops"] + r["edge_scans"] + r["comparisons"]) for r in selected],
                     marker="o", label=f"{algorithm} {density}")
plt.xscale("log"); plt.yscale("log")
plt.xlabel("Vertices (n)"); plt.ylabel("Counted operations")
plt.title("Operations vs n"); plt.legend(fontsize="small"); plt.tight_layout()
plt.savefig(os.path.join(OUT_DIR, "operations_vs_n.png"), dpi=160); plt.close()

# Plot 2: edge scans per edge.
plt.figure()
for algorithm in sorted({r["algorithm"] for r in rows}):
    selected = [r for r in rows if r["algorithm"] == algorithm]
    selected.sort(key=lambda r: r["n"])
    if selected:
        plt.plot([r["n"] for r in selected],
                 [r["edge_scans"] / max(1, r["m"]) for r in selected],
                 marker="o", label=algorithm)
plt.xscale("log"); plt.xlabel("Vertices (n)"); plt.ylabel("edge_scans / m")
plt.title("Scans per edge"); plt.legend(fontsize="small"); plt.tight_layout()
plt.savefig(os.path.join(OUT_DIR, "scans_per_edge.png"), dpi=160); plt.close()

# Plot 3: Kruskal edge-scan share for sparse and dense inputs.
plt.figure()
for density in ("sparse", "dense"):
    selected = [r for r in rows if r["algorithm"] == "Kruskal" and
                (r["m"] == 4 * r["n"] if density == "sparse" else r["m"] == 20 * r["n"])]
    selected.sort(key=lambda r: r["n"])
    if selected:
        plt.plot([r["n"] for r in selected],
                 [r["edge_scans"] / max(1, r["m"]) for r in selected],
                 marker="o", label=density)
plt.xscale("log"); plt.xlabel("Vertices (n)"); plt.ylabel("Kruskal edge_scans / m")
plt.title("Kruskal edge-scan share"); plt.legend(); plt.tight_layout()
plt.savefig(os.path.join(OUT_DIR, "kruskal_edge_share.png"), dpi=160); plt.close()
