/* *****************************************************************************
 *  Name:gyzdmgqy
 *  Date:8/17/2020
 *  Description:Burrows–Wheeler inverse transform. Now, we describe how to invert the Burrows–Wheeler transform and recover the original input string. If the jth original suffix (original string, shifted j characters to the left) is the ith row in the sorted order, we define next[i] to be the row in the sorted order where the (j + 1)st original suffix appears. For example, if first is the row in which the original input string appears, then next[first] is the row in the sorted order where the 1st original suffix (the original string left-shifted by 1) appears; next[next[first]] is the row in the sorted order where the 2nd original suffix appears; next[next[next[first]]] is the row where the 3rd original suffix appears; and so forth.

 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    private static final int R = 256;

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        String input = BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(input);
        int n = csa.length();
        for (int i = 0; i < n; ++i) {
            if (csa.index(i) == 0) {
                BinaryStdOut.write(i);
                break;
            }
        }
        for (int i = 0; i < n; ++i) {
            char output = input.charAt((csa.index(i) + n - 1) % n);
            BinaryStdOut.write(output);
        }
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        String encoded = BinaryStdIn.readString();
        int n = encoded.length();
        int[] next = new int[n], count = new int[R + 1];
        for (int i = 0; i < n; ++i) count[encoded.charAt(i) + 1]++;
        for (int r = 0; r < R; ++r) count[r + 1] += count[r];
        for (int i = 0; i < n; ++i) next[count[encoded.charAt(i)]++] = i;
        int i = next[first];
        for (int j = 0; j < n; ++j) {
            BinaryStdOut.write(encoded.charAt(i));
            i = next[i];
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-")) transform();
        if (args[0].equals("+")) inverseTransform();

    }
}
