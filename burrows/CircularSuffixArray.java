/* *****************************************************************************
 *  Name:gyzdmgqy
 *  Date:8/17/2020
 *  Description:Circular suffix array. To efficiently implement the key component in the Burrows–Wheeler transform, you will use a fundamental data structure known as the circular suffix array, which describes the abstraction of a sorted array of the n circular suffixes of a string of length n. As an example, consider the string "ABRACADABRA!" of length 12. The table below shows its 12 circular suffixes and the result of sorting them.
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdOut;

public class CircularSuffixArray {
    // circular suffix array of s
    private final String suffix;
    private int[] index;
    private final int len;

    public CircularSuffixArray(String s) {
        if (s == null) throw new IllegalArgumentException("The input string is null!");
        suffix = s;
        len = suffix.length();
        index = new int[len];
        for (int i = 0; i < len; ++i) index[i] = i;
        sort(0, len - 1, 0);
    }

    private void sort(int low, int high, int position) {
        if (high <= low) return;
        int lessthan = low;
        int greatthan = high;
        int benchmark = charAt(index[low], position);
        int currentRow = low + 1;
        while (currentRow <= greatthan) {
            int target = charAt(index[currentRow], position);
            if (target > benchmark) exchange(currentRow, greatthan--);
            else if (target < benchmark) exchange(currentRow++, lessthan++);
            else currentRow++;
        }
        sort(low, lessthan - 1, position);
        if (benchmark > 0) sort(lessthan, greatthan, position + 1);
        sort(greatthan + 1, high, position);
    }

    private void exchange(int i, int j) {
        int temp = index[j];
        index[j] = index[i];
        index[i] = temp;
    }

    private int charAt(int row, int position) {
        if (position >= len || position < 0) return -1;
        return suffix.charAt((row + position) % len);
    }

    // length of s
    public int length() {
        return len;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i > len - 1) throw new IllegalArgumentException("The index is out of range.");
        return index[i];
    }

    // unit testing (required)
    public static void main(String[] args) {
        String s = "ABRACADABRA!";
        CircularSuffixArray circularSuffixArray = new CircularSuffixArray(s);
        BinaryStdOut.write("length: " + circularSuffixArray.length() + "\n");
        for (int i = 0; i < circularSuffixArray.length(); i++) {
            BinaryStdOut.write(circularSuffixArray.index(i) + " ");
        }
        BinaryStdOut.close();
    }
}
