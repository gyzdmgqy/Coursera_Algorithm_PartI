/* *****************************************************************************
 *  Name:gyzdmgqy
 *  Date:4/19/2020
 *  Description:Solver data type. In this part, you will implement A* search to solve n-by-n slider puzzles. Create an immutable data type Solver with the following API:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private int moveSteps = -1;
    private SNode solutionNode;

    private class SNode implements Comparable<SNode> {
        public final int moves;
        public final SNode prevNode;
        public final Board board;
        private final int score;

        public SNode(Board board, SNode prevNode, int moves) {
            this.board = board;
            this.prevNode = prevNode;
            this.moves = moves;
            score = board.manhattan() + moves;
        }

        public int compareTo(SNode other) {
            return Integer.compare(this.score, other.score);
        }
    }


    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        final int MAXITR = 1000000;
        if (initial == null) throw new IllegalArgumentException();
        MinPQ<SNode> pqboards = new MinPQ<>();
        pqboards.insert(new SNode(initial, null, 0));
        int itr = 0;
        boolean debugMode = false;
        while (itr < MAXITR) {
            SNode node = pqboards.delMin();
            if (debugMode) {
                System.out.println(String.format("Itr: %d, score: %d, MScore: %d", itr, node.score,
                                                 node.score - node.moves));
                System.out.println(node.board.toString());
            }
            if (node.board.isGoal()) {
                solutionNode = node;
                moveSteps = node.moves;
                return;
            }
            for (Board nextNode : node.board.neighbors()) {
                if (node.prevNode != null) if (nextNode.equals(node.prevNode.board)) continue;
                int newMove = node.moves + 1;
                pqboards.insert(new SNode(nextNode, node, newMove));
            }
            itr++;
        }
        System.out.println("Maximum iteration has been reached.");

    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return moveSteps >= 0;
    }

    // min number of moves to solve initial board
    public int moves() {
        return moveSteps;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        if (!isSolvable()) return null;
        Stack<Board> solutions = new Stack<Board>();
        SNode node = solutionNode;
        while (node != null) {
            solutions.push(node.board);
            node = node.prevNode;
        }
        return solutions;
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}
