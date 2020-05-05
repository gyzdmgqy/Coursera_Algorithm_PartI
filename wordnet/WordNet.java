/* *****************************************************************************
 *  Name:gyzdmgqy
 *  Date:5/2/2020
 *  Description:WordNet data type. Implement an immutable data type WordNet with the following API:
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class WordNet {

    private final HashMap<Integer, String> iD2Synset;
    private final HashMap<String, Set<Integer>> word2Ids;
    private final SAP sap;


    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        iD2Synset = new HashMap<>();
        word2Ids = new HashMap<>();
        In synsetFile = new In(synsets);
        while (synsetFile.hasNextLine()) {
            String[] lines = synsetFile.readLine().split(",");
            int id = Integer.parseInt(lines[0]);
            String synset = lines[1];
            if (iD2Synset.containsKey(id)) throw new IllegalArgumentException();
            iD2Synset.put(id, synset);
            String[] words = synset.split(" ");
            for (String word : words) {
                if (!word2Ids.containsKey(word)) {
                    Set<Integer> ids = new HashSet<>();
                    ids.add(id);
                    word2Ids.put(word, ids);
                }
                else {
                    word2Ids.get(word).add(id);
                }

            }
        }
        synsetFile.close();
        Digraph digraph = new Digraph(iD2Synset.size());
        In hyperFile = new In(hypernyms);
        while (hyperFile.hasNextLine()) {
            String[] idlines = hyperFile.readLine().split(",");
            int synsetid = Integer.parseInt(idlines[0]);
            for (int i = 1; i < idlines.length; ++i) {
                int hyperid = Integer.parseInt(idlines[i]);
                digraph.addEdge(synsetid, hyperid);
            }
        }
        if (!isDAG(digraph)) throw new IllegalArgumentException();
        hyperFile.close();
        sap = new SAP(digraph);

    }

    private static boolean isDAG(Digraph digraph) {
        DirectedCycle diCycle = new DirectedCycle(digraph);
        if (diCycle.hasCycle()) return false;
        int rootCount = 0;
        for (int v = 0; v < digraph.V(); ++v) {
            if (digraph.outdegree(v) == 0)
                rootCount++;
            if (rootCount > 1)
                return false;
        }
        return true;
    }


    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return word2Ids.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException();
        return word2Ids.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
        Set<Integer> v = word2Ids.get(nounA);
        Set<Integer> w = word2Ids.get(nounB);
        return sap.length(v, w);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
        Set<Integer> v = word2Ids.get(nounA);
        Set<Integer> w = word2Ids.get(nounB);
        int ancesterId = sap.ancestor(v, w);
        return iD2Synset.get(ancesterId);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        StdOut.println(wordnet.sap("macromolecule", "supermolecule"));

    }
}
