/* *****************************************************************************
 *  Name:gyzdmgqy
 *  Date:8/15/2020
 *  Description:The Boggle game. Boggle is a word game designed by Allan Turoff and distributed by Hasbro. It involves a board made up of 16 cubic dice, where each die has a letter printed on each of its 6 sides. At the beginning of the game, the 16 dice are shaken and randomly distributed into a 4-by-4 tray, with only the top sides of the dice visible. The players compete to accumulate points by building valid words from the dice, according to these rules:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

public class BoggleSolver {
    private final Trie dictTrie;


    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        dictTrie = new Trie();
        for (String word : dictionary) {
            if (word.length() < 3) continue;
            dictTrie.put(word);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        HashSet<String> validWords = new HashSet<>();
        int nRow = board.rows();
        int nCol = board.cols();
        for (int row = 0; row < nRow; ++row) {
            for (int col = 0; col < nCol; ++col) {
                dfsValidWords(board, row, col, validWords);
            }
        }
        return validWords;
    }


    private void dfsValidWords(BoggleBoard board, int row, int col, HashSet<String> validWords) {
        int nRow = board.rows();
        int nCol = board.cols();
        ArrayList<Boolean> visited = new ArrayList<>();
        for (int i = 0; i < nRow * nCol; ++i) visited.add(Boolean.FALSE);
        LinkedList<Integer> stack = new LinkedList<>();
        LinkedList<Trie.Node> stackNodes = new LinkedList<>();
        LinkedList<StringBuilder> stackWords = new LinkedList<>();
        LinkedList<ArrayList<Boolean>> stackVisited = new LinkedList<>();
        Trie.Node current = dictTrie.getRoot();
        char currentChar = board.getLetter(row, col);
        current = current.get(currentChar);
        if (current == null) return;
        StringBuilder prefix = new StringBuilder();
        prefix.append(currentChar);
        if (currentChar == 'Q') {
            current = current.get('U');
            if (current == null) return;
            prefix.append('U');
        }
        int position = col + row * nCol;
        stack.add(position);
        stackNodes.add(current);
        stackWords.add(prefix);
        stackVisited.add(visited);

        while (!stack.isEmpty()) {
            position = stack.removeLast();
            row = position / nCol;
            col = position % nCol;
            visited = stackVisited.removeLast();
            visited.set(position, Boolean.TRUE);
            current = stackNodes.removeLast();
            prefix = stackWords.removeLast();
            for (int i = row - 1; i < row + 2; ++i) {
                for (int j = col - 1; j < col + 2; ++j) {
                    if (!checkValidity(i, j, board)) continue;
                    int newPosition = i * nCol + j;
                    if (newPosition == position) continue;
                    if (visited.get(newPosition)) continue;
                    char nextChar = board.getLetter(i, j);
                    Trie.Node next = current.get(nextChar);
                    if (next != null) {
                        StringBuilder nextWord = new StringBuilder(prefix);
                        nextWord.append(nextChar);
                        if (nextChar == 'Q') {
                            next = next.get('U');
                            if (next == null) continue;
                            nextWord.append('U');
                        }
                        if (next.getScore() > 0) validWords.add(nextWord.toString());
                        stack.add(newPosition);
                        stackNodes.add(next);
                        stackWords.add(nextWord);
                        ArrayList<Boolean> newVisited = new ArrayList<Boolean>(visited);
                        stackVisited.add(newVisited);
                    }
                }
            }

        }

    }

    private boolean checkValidity(int i, int j, BoggleBoard board) {
        int nRow = board.rows();
        int nCol = board.cols();
        if (i < 0 || j < 0) return false;
        if (i >= nRow || j >= nCol) return false;
        return true;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        return dictTrie.searchScore(word);
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);

    }
}
