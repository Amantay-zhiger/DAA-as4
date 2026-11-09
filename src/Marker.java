import java.util.*;

/** Missions 3 and 4: prefix function, KMP and Rabin-Karp. */
public class Marker {
    /** pi[i] is the longest proper prefix of p[0..i] that is also its suffix. */
    public static int[] prefixFunction(String p) {
        int[] pi = new int[p.length()];
        for (int i = 1; i < p.length(); i++) {
            int j = pi[i - 1];
            while (j > 0 && p.charAt(i) != p.charAt(j)) j = pi[j - 1];
            if (p.charAt(i) == p.charAt(j)) j++;
            pi[i] = j;
        }
        return pi;
    }

    /** Returns all (including overlapping) occurrences of p in text using KMP. */
    public static int[] findAll(String text, String p) {
        if (p.isEmpty()) return new int[0];
        int[] pi = prefixFunction(p);
        ArrayList<Integer> hits = new ArrayList<>();
        int j = 0;
        for (int i = 0; i < text.length(); i++) {
            while (j > 0 && text.charAt(i) != p.charAt(j)) j = pi[j - 1];
            if (text.charAt(i) == p.charAt(j)) j++;
            if (j == p.length()) {
                hits.add(i - p.length() + 1);
                j = pi[j - 1];
            }
        }
        int[] result = new int[hits.size()];
        for (int i = 0; i < result.length; i++) result[i] = hits.get(i);
        return result;
    }

    /** Counts A-windows of length k that occur in B, verifying equal hashes character by character. */
    public static int sharedWords(String a, String b, int k) {
        if (k <= 0 || a.length() < k || b.length() < k) return 0;
        final long base = 5L;
        long power = 1;
        for (int i = 1; i < k; i++) power *= base;
        Map<Long, ArrayList<Integer>> positions = new HashMap<>();
        long hash = 0;
        for (int i = 0; i < k; i++) hash = hash * base + value(b.charAt(i));
        positions.computeIfAbsent(hash, x -> new ArrayList<>()).add(0);
        for (int i = k; i < b.length(); i++) {
            hash = (hash - value(b.charAt(i - k)) * power) * base + value(b.charAt(i));
            positions.computeIfAbsent(hash, x -> new ArrayList<>()).add(i - k + 1);
        }
        int answer = 0;
        hash = 0;
        for (int i = 0; i < k; i++) hash = hash * base + value(a.charAt(i));
        for (int start = 0; ; start++) {
            ArrayList<Integer> candidates = positions.get(hash);
            boolean found = false;
            if (candidates != null) {
                for (int pos : candidates) {
                    int j = 0;
                    while (j < k && a.charAt(start + j) == b.charAt(pos + j)) j++;
                    if (j == k) { found = true; break; }
                }
            }
            if (found) answer++;
            if (start + k >= a.length()) break;
            hash = (hash - value(a.charAt(start)) * power) * base + value(a.charAt(start + k));
        }
        return answer;
    }

    private static int value(char c) { return c - 'A' + 1; }
}
