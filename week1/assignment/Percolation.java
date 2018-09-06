/**
 * 2017-10-07
 * Ben Zarras
 *
 * Class Percolation represents a data type that allows
 * for clean simulation of the percolation problem
 * to help calculate the percolation threshold.
 */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private WeightedQuickUnionUF uf;
    private int n;
    private int top;
    private int bottom;
    private int[] openSites;
    private int openCount;

    public Percolation(int n) {
        if (n <= 0) {
            throw new java.lang.IllegalArgumentException("n > 0");
        }
        this.n = n;
        int size = n * n;
        this.top = size;
        this.bottom = size + 1;
        this.uf = new WeightedQuickUnionUF(size + 2);
        this.openSites = new int[size];
        this.openCount = 0;
    }

    public void open(int row, int col) {
        this.validateIndices(row, col);
        if (this.isOpen(row, col)) {
            return;
        }
        int site = this.xyTo1D(row, col);
        this.openSites[site] = 1;
        this.openCount++;
        int[] openNeighborIndices = {-1, -1, -1, -1};
        // compute neighborIndices
        int p = 0;
        if (row - 1 > 0 && this.isOpen(row - 1, col)) {
            openNeighborIndices[p++] = this.xyTo1D(row - 1, col);
        }
        if (row + 1 <= n && this.isOpen(row + 1, col)) {
            openNeighborIndices[p++] = this.xyTo1D(row + 1, col);
        }
        if (col - 1 > 0 && this.isOpen(row, col - 1)) {
            openNeighborIndices[p++] = this.xyTo1D(row, col - 1);
        }
        if (col + 1 <= n && this.isOpen(row, col + 1)) {
            openNeighborIndices[p++] = this.xyTo1D(row, col + 1);
        }
        // connect with top or bottom virtual sites
        if (row - 1 == 0) {
            openNeighborIndices[p++] = this.top;
        }
        if (row + 1 == n + 1) {
            openNeighborIndices[p++] = this.bottom;
        }
        // reset p back to 0 and go through neighbor
        // indices to union the site and its neighbors
        p = 0;
        int neighbor = openNeighborIndices[p];
        while (neighbor != -1) {
            p++;
            uf.union(site, neighbor);
            neighbor = p < 4 ? openNeighborIndices[p] : -1;
        }
    }

    public boolean isOpen(int row, int col) {
        this.validateIndices(row, col);
        int site = this.xyTo1D(row, col);
        return this.openSites[site] == 1 ? true : false;
    }

    public boolean isFull(int row, int col) {
        this.validateIndices(row, col);
        int site = this.xyTo1D(row, col);
        return uf.connected(this.top, site);
    }

    public int numberOfOpenSites() {
        return this.openCount;
    }

    public boolean percolates() {
        return uf.connected(this.top, this.bottom);
    }

    /**
     * Takes a 1-based row and column index and translates
     * them to a 0-based array index;
     * @param  int row
     * @param  int col
     * @return     integer representing 0-based array index
     */
    private int xyTo1D(int row, int col) {
        return this.n * (row - 1) + col - 1;
    }

    /**
     * Takes a row and column index and throws an error if either
     * is outside its expected range
     * @param int row
     * @param int col
     */
    private void validateIndices(int row, int col) {
        if (row < 1 || row > this.n || col < 1 || col > this.n) {
            throw new java.lang.IllegalArgumentException("1 <= index <= n");
        }
    }

    public static void main(String[] args) {
        Percolation p = new Percolation(5);
        System.out.println(p.percolates());
        p.open(1, 2);
        System.out.println(p.percolates());
        p.open(2, 2);
        System.out.println(p.percolates());
        p.open(2, 3);
        System.out.println(p.percolates());
        p.open(3, 3);
        System.out.println(p.percolates());
        p.open(4, 3);
        System.out.println(p.percolates());
        p.open(5, 3);
        System.out.println(p.percolates());
    }
}
