import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private final double confidence95 = 1.96;
    private final double[] percolationThresholds;
    private final int trials;
    private double mean = -1;
    private double std = -1;


    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) throw new IllegalArgumentException();
        this.trials = trials;
        percolationThresholds = new double[trials];
        double totalDim = n * n;
        for (int t = 0; t < trials; ++t) {
            Percolation perc = new Percolation(n);
            while (!perc.percolates()) {
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);
                perc.open(row, col);
            }
            percolationThresholds[t] = perc.numberOfOpenSites() / totalDim;
        }

    }

    // sample mean of percolation threshold
    public double mean() {
        if (mean < 0) mean = StdStats.mean(percolationThresholds);
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        if (std < 0) std = StdStats.stddev(percolationThresholds);
        return std;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - confidence95 * stddev() / Math.sqrt((double) trials);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + confidence95 * stddev() / Math.sqrt((double) trials);
    }

    private void printResults() {
        System.out.printf("mean = %f; std = %f; 95%% confidence interval [%f,%f].\n", mean(),
                          stddev(), confidenceLo(), confidenceHi());
    }

    // test client (see below)
    public static void main(String[] args) {
        PercolationStats perc1 = new PercolationStats(200, 100);
        perc1.printResults();
        PercolationStats perc2 = new PercolationStats(300, 200);
        perc2.printResults();
        PercolationStats perc3 = new PercolationStats(2, 10000);
        perc3.printResults();
        PercolationStats perc4 = new PercolationStats(2, 100000);
        perc4.printResults();

    }
}
