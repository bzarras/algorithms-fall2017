/**
 * 2017-10-08
 * Ben Zarras
 *
 * Class PercolationStats represents a data type that
 * computes various values necessary for finding the percolation
 * threshold of a material.
 */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private int trials;
    private double[] thresholds;

    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new java.lang.IllegalArgumentException("cant be < 0");
        }
        this.trials = trials;
        this.thresholds = new double[trials];
        for (int t = 0; t < trials; t++) {
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates()) {
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);
                percolation.open(row, col);
            }
            double threshold = ((double) percolation.numberOfOpenSites()) / (n * n);
            thresholds[t] = threshold;
        }
    }

    public double mean() {
        return StdStats.mean(this.thresholds);
    }

    public double stddev() {
        if (this.trials == 1) {
            return Double.NaN;
        }
        return StdStats.stddev(this.thresholds);
    }

    public double confidenceLo() {
        return this.mean() - (1.96 * this.stddev()) / Math.sqrt(this.trials);
    }

    public double confidenceHi() {
        return this.mean() + (1.96 * this.stddev()) / Math.sqrt(this.trials);
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(n, t);
        System.out.printf("mean                     = %f%n", stats.mean());
        System.out.printf("stddev                   = %f%n", stats.stddev());
        String s       = "95%% confidence interval  = [%f, %f]%n";
        System.out.printf(s, stats.confidenceLo(), stats.confidenceHi());
    }
}
