import java.util.*;

/** Mission 5: vertex-cover dynamic programming on a tree rooted at vertex 0. */
public class Outbreak {
    private static final long INF = Long.MAX_VALUE / 4;

    /** Minimum-cost vertex cover; returns which vertices are selected. */
    public static boolean[] cheapestTests(int n, int[][] nb, int[] cost) {
        boolean[] selected = new boolean[n];
        if (n == 0) return selected;
        int[] parent = new int[n]; Arrays.fill(parent, -1);
        int[] order = new int[n]; int size = 0;
        int[] stack = new int[n]; int top = 0; stack[top++] = 0; parent[0] = 0;
        while (top > 0) {
            int v = stack[--top]; order[size++] = v;
            for (int u : nb[v]) if (parent[u] == -1) { parent[u] = v; stack[top++] = u; }
        }
        long[][] dp = new long[n][2]; // 0: v not tested; 1: v tested
        for (int oi = size - 1; oi >= 0; oi--) {
            int v = order[oi]; dp[v][1] = cost[v]; dp[v][0] = 0;
            for (int u : nb[v]) if (parent[u] == v) {
                dp[v][1] += Math.min(dp[u][0], dp[u][1]);
                dp[v][0] += dp[u][1];
            }
        }
        boolean rootTested = dp[0][1] < dp[0][0];
        ArrayDeque<int[]> todo = new ArrayDeque<>(); todo.push(new int[]{0, rootTested ? 1 : 0});
        while (!todo.isEmpty()) {
            int[] state = todo.pop(); int v = state[0]; boolean take = state[1] == 1;
            selected[v] = take;
            for (int u : nb[v]) if (parent[u] == v) {
                boolean childTake = !take || dp[u][1] < dp[u][0];
                todo.push(new int[]{u, childTake ? 1 : 0});
            }
        }
        return selected;
    }

    /** Diameter of the tree, measured in number of edges. */
    public static int longestChain(int n, int[][] nb) {
        if (n <= 1) return 0;
        int[] parent = new int[n], order = new int[n], stack = new int[n];
        Arrays.fill(parent, -1);
        int top = 0, size = 0; stack[top++] = 0; parent[0] = 0;
        while (top > 0) {
            int v = stack[--top]; order[size++] = v;
            for (int u : nb[v]) if (parent[u] == -1) { parent[u] = v; stack[top++] = u; }
        }
        int[] down = new int[n]; int diameter = 0;
        for (int oi = size - 1; oi >= 0; oi--) {
            int v = order[oi], first = 0, second = 0;
            for (int u : nb[v]) if (parent[u] == v) {
                int d = down[u] + 1;
                if (d > first) { second = first; first = d; }
                else if (d > second) second = d;
            }
            down[v] = first;
            diameter = Math.max(diameter, first + second);
        }
        return diameter;
    }
}
