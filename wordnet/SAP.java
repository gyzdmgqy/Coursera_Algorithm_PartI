/* *****************************************************************************
 *  Name:gyzdmgqy
 *  Date:5/3/2020
 *  Description:SAP data type. Implement an immutable data type SAP with the following API:
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashMap;

public class SAP {
    private final Digraph digraph;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) throw new IllegalArgumentException();
        digraph = G;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        ArrayList<Integer> vList = new ArrayList<>();
        ArrayList<Integer> wList = new ArrayList<>();
        vList.add(v);
        wList.add(w);
        int[] result = findSAP(vList, wList);
        return result[1];
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        ArrayList<Integer> vList = new ArrayList<>();
        ArrayList<Integer> wList = new ArrayList<>();
        vList.add(v);
        wList.add(w);
        int[] result = findSAP(vList, wList);
        return result[0];
    }

    private int[] findSAP(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException();
        if (containNull(v) || containNull(w)) throw new IllegalArgumentException();
        final int VMARK = 1;
        final int WMARK = 2;
        final int BOTHMARK = 3;
        int[] result = new int[2];
        for (int i = 0; i < result.length; ++i) result[i] = -1;
        Queue<Integer> sapQueue = new Queue<>();
        Queue<Boolean> sapQueueVorW = new Queue<>();
        HashMap<Integer, Integer> vLength = new HashMap<>();
        HashMap<Integer, Integer> wLength = new HashMap<>();
        HashMap<Integer, Integer> markedDic = new HashMap<>();
        for (Integer vertex : v) {
            sapQueue.enqueue(vertex);
            sapQueueVorW.enqueue(true);
            vLength.put(vertex, 0);
        }
        for (Integer vertex : w) {
            sapQueue.enqueue(vertex);
            sapQueueVorW.enqueue(false);
            wLength.put(vertex, 0);
        }
        Integer curVlen = -1;
        Integer curWlen = -1;
        int totalDistance = digraph.V() * 2;
        int ancestry = -1;


        while (!sapQueue.isEmpty()) {
            boolean isV = sapQueueVorW.dequeue();
            int currentVertex = sapQueue.dequeue();
            int markValue = markedDic.containsKey(currentVertex) ?
                            markedDic.get(currentVertex) : -1;
            if (isV) {
                curVlen = vLength.get(currentVertex);
                if (curVlen > totalDistance) continue;
                if (markValue == VMARK || markValue == BOTHMARK) continue;
                if (markValue == WMARK) {
                    int distance = curVlen + wLength.get(currentVertex);
                    if (distance < totalDistance) {
                        ancestry = currentVertex;
                        totalDistance = distance;
                        markedDic.put(currentVertex, BOTHMARK);
                    }
                }
                markedDic.put(currentVertex, VMARK);
                for (Integer adjV : digraph.adj(currentVertex)) {
                    sapQueue.enqueue(adjV);
                    sapQueueVorW.enqueue(true);
                    if (!vLength.containsKey(adjV)) vLength.put(adjV, curVlen + 1);
                }

            }
            else {
                curWlen = wLength.get(currentVertex);
                if (curWlen > totalDistance) continue;
                // if current vertex has already been visited by W.
                if (markValue == WMARK || markValue == BOTHMARK) continue;

                if (markValue == VMARK) { // this has also been visited by V
                    int distance = curWlen + vLength.get(currentVertex);
                    if (distance < totalDistance) {
                        ancestry = currentVertex;
                        totalDistance = distance;
                        markedDic.put(currentVertex, BOTHMARK);
                    }
                }
                markedDic.put(currentVertex, WMARK);
                for (Integer adjW : digraph.adj(currentVertex)) {
                    sapQueue.enqueue(adjW);
                    sapQueueVorW.enqueue(false);
                    if (!wLength.containsKey(adjW)) wLength.put(adjW, curWlen + 1);
                }
            }
        }
        if (ancestry != -1) {
            result[0] = ancestry;
            result[1] = totalDistance;
        }
        return result;

    }

    private static boolean containNull(Iterable<Integer> itr) {
        for (Integer value : itr) if (value == null) return true;
        return false;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException();
        if (containNull(v) || containNull(w)) throw new IllegalArgumentException();
        int[] result = findSAP(v, w);
        return result[1];
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException();
        if (containNull(v) || containNull(w)) throw new IllegalArgumentException();
        int[] result = findSAP(v, w);
        return result[0];
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
