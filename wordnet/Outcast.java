/* *****************************************************************************
 *  Name:gyzdmgqy
 *  Date:05/03/2020
 *  Description:Outcast detection. Given a list of WordNet nouns x1, x2, ..., xn, which noun is the least related to the others? To identify an outcast, compute the sum of the distances between each noun and every other one:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private final String outcast;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        if (wordnet == null) throw new IllegalArgumentException();
        int maxDistanceSum = 0;
        String outcastMax = "";
        for (String source : wordnet.nouns()) {
            int distanceSum = 0;
            for (String sink : wordnet.nouns()) {
                int distance = wordnet.distance(source, sink);
                if (distance > 0) {
                    distanceSum += distance;
                }
            }
            if (distanceSum > maxDistanceSum) {
                maxDistanceSum = distanceSum;
                outcastMax = source;
            }
        }
        outcast = outcastMax;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        return outcast;

    }

    // see test client below
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
