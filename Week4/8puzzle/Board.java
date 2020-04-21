/* *****************************************************************************
 *  Name:Gary Gu
 *  Date:4/18/2020
 *  Description:Week4 Homework
 **************************************************************************** */

import edu.princeton.cs.algs4.In;

import java.util.ArrayList;

public class Board {
    private final int n;
    private final int[] tilesVec;
    private final int[] positionMat;
    private int hammingScore = -1;
    private int manhattanScore = -1;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        n = tiles.length;
        positionMat = new int[n * n];
        tilesVec = new int[n * n];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                int index = getIndex(i, j);
                tilesVec[index] = tiles[i][j];
                positionMat[tilesVec[index]] = index;
            }
        }
    }

    private int getIndex(int row, int col) {
        return row * dimension() + col;
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n);
        s.append("\n");
        for (int i = 0; i < dimension(); ++i) {
            for (int j = 0; j < dimension(); ++j) {
                s.append(String.format("%2d ", tilesVec[getIndex(i, j)]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        if (hammingScore >= 0) return hammingScore;
        hammingScore = 0;
        for (int i = 0; i < n * n - 1; ++i) {
            int solutionValue = i + 1;
            if (tilesVec[i] != solutionValue) hammingScore++;
        }
        return hammingScore;

    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        if (manhattanScore >= 0) return manhattanScore;
        manhattanScore = 0;
        for (int i = 0; i < n * n - 1; ++i) {
            int solutionValue = i + 1;
            int currentPosition = positionMat[solutionValue];
            int diffCol = Math.abs(currentPosition % n - i % n);
            int diffRow = Math.abs(currentPosition / n - i / n);
            manhattanScore += diffCol + diffRow;
        }
        return manhattanScore;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null) return false;
        if (getClass() != y.getClass()) return false;
        Board yBoard = (Board) y;
        if (dimension() != yBoard.dimension()) return false;
        for (int i = 0; i < n * n; ++i) if (tilesVec[i] != yBoard.tilesVec[i]) return false;
        return true;
    }

    private int dManhatanScoreNeibour(int dRow, int dCol) {
        // int targetPosition = positionMat[targetVal];
        int zeroPosition = positionMat[0];
        int zeroRow = zeroPosition / n;
        int zeroColumn = zeroPosition % n;
        int tileIdx = getIndex(zeroRow + dRow, zeroColumn + dCol);
        int targetVal = tilesVec[tileIdx];
        int optimalPosition = targetVal - 1;
        int opRow = optimalPosition / n;
        int opCol = optimalPosition % n;
        int dManhattanScore = Math.abs(zeroRow - opRow) + Math.abs(zeroColumn - opCol) - Math
                .abs(zeroRow + dRow - opRow) - Math.abs(zeroColumn + dCol - opCol);
        return dManhattanScore;
    }

    private int dHammingScoreNeibour(int tileIdx) {
        int targetVal = tilesVec[tileIdx];
        int targetPosition = positionMat[targetVal];
        int zeroPosition = positionMat[0];
        int solutionValatZeroPosition = zeroPosition + 1;
        int solutionValatTargetPosition = (targetPosition + 1) % (n * n);
        if (solutionValatZeroPosition == targetVal) return -1;
        if (solutionValatTargetPosition == targetVal) return 1;
        return 0;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        ArrayList<Board> boards;
        boards = new ArrayList<Board>();
        int positionZero = positionMat[0];
        int zeroRow = positionZero / dimension();

        int zeroColumn = positionZero % dimension();
        if (zeroRow != 0) {
            int[][] newTiles = new int[dimension()][dimension()];
            for (int i = 0; i < dimension(); ++i)
                for (int j = 0; j < dimension(); ++j) newTiles[i][j] = tilesVec[getIndex(i, j)];
            newTiles[zeroRow][zeroColumn] = newTiles[zeroRow - 1][zeroColumn];
            newTiles[zeroRow - 1][zeroColumn] = 0;
            Board newBoard = new Board(newTiles);
            int hscore = hamming();
            newBoard.manhattanScore = manhattan() + dManhatanScoreNeibour(-1, 0);
            newBoard.hammingScore = hamming() + dHammingScoreNeibour(
                    getIndex(zeroRow - 1, zeroColumn));
            boards.add(newBoard);
        }
        if (zeroRow != n - 1) {
            int[][] newTiles = new int[dimension()][dimension()];
            for (int i = 0; i < dimension(); ++i)
                for (int j = 0; j < dimension(); ++j) newTiles[i][j] = tilesVec[getIndex(i, j)];
            newTiles[zeroRow][zeroColumn] = newTiles[zeroRow + 1][zeroColumn];
            newTiles[zeroRow + 1][zeroColumn] = 0;
            Board newBoard = new Board(newTiles);
            int hscore = hamming();
            newBoard.manhattanScore = manhattan() + dManhatanScoreNeibour(1, 0);
            newBoard.hammingScore = hamming() + dHammingScoreNeibour(
                    getIndex(zeroRow + 1, zeroColumn));
            boards.add(newBoard);
        }
        if (zeroColumn != 0) {
            int[][] newTiles = new int[dimension()][dimension()];
            for (int i = 0; i < dimension(); ++i)
                for (int j = 0; j < dimension(); ++j) newTiles[i][j] = tilesVec[getIndex(i, j)];
            newTiles[zeroRow][zeroColumn] = newTiles[zeroRow][zeroColumn - 1];
            newTiles[zeroRow][zeroColumn - 1] = 0;
            Board newBoard = new Board(newTiles);
            int hscore = hamming();
            newBoard.manhattanScore = manhattan() + dManhatanScoreNeibour(0, -1);
            newBoard.hammingScore = hamming() + dHammingScoreNeibour(
                    getIndex(zeroRow, zeroColumn - 1));
            boards.add(newBoard);
        }
        if (zeroColumn != n - 1) {
            int[][] newTiles = new int[dimension()][dimension()];
            for (int i = 0; i < dimension(); ++i)
                for (int j = 0; j < dimension(); ++j) newTiles[i][j] = tilesVec[getIndex(i, j)];
            newTiles[zeroRow][zeroColumn] = newTiles[zeroRow][zeroColumn + 1];
            newTiles[zeroRow][zeroColumn + 1] = 0;
            Board newBoard = new Board(newTiles);
            newBoard.manhattanScore = manhattan() + dManhatanScoreNeibour(0, 1);
            int hscore = hamming();
            newBoard.hammingScore = hamming() + dHammingScoreNeibour(
                    getIndex(zeroRow, zeroColumn + 1));
            boards.add(newBoard);
        }
        return boards;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int x1 = 0, x2 = 0, y1 = 0, y2 = 1;
        int[][] newTiles = new int[dimension()][dimension()];
        for (int i = 0; i < dimension(); ++i)
            for (int j = 0; j < dimension(); ++j) newTiles[i][j] = tilesVec[getIndex(i, j)];
        if (tilesVec[getIndex(y1, x1)] == 0) x1++;
        if (tilesVec[getIndex(y2, x2)] == 0) x2++;
        newTiles[y1][x1] = tilesVec[getIndex(y2, x2)];
        newTiles[y2][x2] = tilesVec[getIndex(y1, x1)];
        return new Board(newTiles);
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);
        System.out.println(initial.toString());
        System.out.println("hamming:" + initial.hamming());
        System.out.println("manhattan:" + initial.manhattan());
        Iterable<Board> it = initial.neighbors();
        for (Board p : it) {
            System.out.println(p.toString());
            System.out.println("hamming:" + p.hamming());
            System.out.println("manhattan:" + p.manhattan());
            System.out.println(p.dimension());
            System.out.println(p.isGoal());
            System.out.println(p.equals(initial));
            System.out.println(p.twin().toString());
        }


    }

}
