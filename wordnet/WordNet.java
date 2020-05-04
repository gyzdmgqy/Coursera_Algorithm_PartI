/* *****************************************************************************
 *  Name:gyzdmgqy
 *  Date:5/2/2020
 *  Description:WordNet data type. Implement an immutable data type WordNet with the following API:
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

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
        Digraph digraph = new Digraph(word2Ids.size());
        In hyperFile = new In(hypernyms);
        while (hyperFile.hasNextLine()) {
            String[] idlines = hyperFile.readLine().split(",");
            int synsetid = Integer.parseInt(idlines[0]);
            for (int i = 1; i < idlines.length; ++i) {
                int hyperid = Integer.parseInt(idlines[i]);
                digraph.addEdge(synsetid, hyperid);
            }
        }
        hyperFile.close();
        sap = new SAP(digraph);

    }


    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return word2Ids.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return word2Ids.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        Set<Integer> v = word2Ids.get(nounA);
        Set<Integer> w = word2Ids.get(nounB);
        return sap.length(v, w);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        Set<Integer> v = word2Ids.get(nounA);
        Set<Integer> w = word2Ids.get(nounB);
        int ancesterId = sap.ancestor(v, w);
        return iD2Synset.get(ancesterId);
    }

    // do unit testing of this class
    public static void main(String[] args) {

    }
}
