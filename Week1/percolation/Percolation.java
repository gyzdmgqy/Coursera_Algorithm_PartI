import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[] sites;
    private int numOpenSite;
    private final WeightedQuickUnionUF unionFind;
    private final WeightedQuickUnionUF unionFindFull;
    private final int dimension;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException();
        dimension = n;
        sites = new boolean[n * n];
        unionFind = new WeightedQuickUnionUF(n * n + 2);
        unionFindFull = new WeightedQuickUnionUF(n * n + 1);
        numOpenSite = 0;
        for (int i = 0; i < n * n; ++i) sites[i] = false;
    }

    private int rowCol2id(int row, int col) {
        if (!isRowColValid(row, col, true)) return 0;
        return (row - 1) * dimension + col - 1;
    }


    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (!isRowColValid(row, col, true)) return;
        int siteid = rowCol2id(row, col);
        if (!sites[siteid]) {
            sites[siteid] = true;
            // check the neibouring site and establish union if needed.
            if (isRowColValid(row + 1, col, false)) {
                if (isOpen(row + 1, col)) {
                    unionFind.union(rowCol2id(row + 1, col), rowCol2id(row, col));
                    unionFindFull.union(rowCol2id(row + 1, col), rowCol2id(row, col));
                }
            }
            if (isRowColValid(row - 1, col, false)) {
                if (isOpen(row - 1, col)) {
                    unionFind.union(rowCol2id(row - 1, col), rowCol2id(row, col));
                    unionFindFull.union(rowCol2id(row - 1, col), rowCol2id(row, col));
                }
            }
            if (isRowColValid(row, col + 1, false)) {
                if (isOpen(row, col + 1)) {
                    unionFind.union(rowCol2id(row, col + 1), rowCol2id(row, col));
                    unionFindFull.union(rowCol2id(row, col + 1), rowCol2id(row, col));
                }
            }
            if (isRowColValid(row, col - 1, false)) {
                if (isOpen(row, col - 1)) {
                    unionFind.union(rowCol2id(row, col - 1), rowCol2id(row, col));
                    unionFindFull.union(rowCol2id(row, col - 1), rowCol2id(row, col));
                }
            }
            if (row == 1) {
                unionFind.union(rowCol2id(row, col), dimension * dimension);
                unionFindFull.union(rowCol2id(row, col), dimension * dimension);
            }
            if (row == dimension) unionFind.union(rowCol2id(row, col), dimension * dimension + 1);
            numOpenSite++;
        }
    }

    private boolean isRowColValid(int row, int col, boolean raiseException) {
        boolean valid = true;
        if (row < 1 || col < 1 || row > dimension || col > dimension) {
            valid = false;
            if (raiseException) throw new IllegalArgumentException();
        }
        return valid;
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (!isRowColValid(row, col, true)) return false;
        return sites[rowCol2id(row, col)];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (!isRowColValid(row, col, true)) return false;
        if (!isOpen(row, col)) return false;
        return unionFindFull.connected(dimension * dimension, rowCol2id(row, col));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numOpenSite;
    }

    // does the system percolate?
    public boolean percolates() {
        return unionFind.connected(dimension * dimension, dimension * dimension + 1);
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation perc = new Percolation(3);
        perc.open(1, 1);
        //  perc.open(0, 1);
        perc.open(2, 1);
        StdOut.print(perc.isFull(2, 1));
        StdOut.print(perc.percolates());
        perc.open(3, 1);
        perc.open(3, 3);
        StdOut.print(perc.percolates());
        StdOut.print(perc.numberOfOpenSites());
        System.out.printf("%b", perc.isFull(3, 3));
    }
}
