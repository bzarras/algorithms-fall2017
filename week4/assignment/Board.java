/**
 * Ben Zarras
 * 10/26/2017
 *
 * Board.java
 */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Queue;
import java.util.Iterator;

public final class Board {
    private final int[][] blocks;
    private final int n;
    private Board twin;
    private BlockIndex zeroBlock;

    public Board(int[][] blocks) {
        this.n = blocks.length;
        this.blocks = new int[n][n];
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks[i].length; j++) {
                this.blocks[i][j] = blocks[i][j];
                if (this.blocks[i][j] == 0)
                    this.zeroBlock = new BlockIndex(i, j);
            }
        }
    }

    public int dimension() {
        return n;
    }

    public int hamming() {
        int hamming = 0;
        for (int i = 0; i < this.blocks.length; i++) {
            for (int j = 0; j < this.blocks[i].length; j++) {
                if (this.blocks[i][j] != 0 && !this.blockIsCorrect(i, j))
                    hamming++;
            }
        }
        return hamming;
    }

    public int manhattan() {
        int manhattan = 0;
        for (int i = 0; i < this.blocks.length; i++) {
            for (int j = 0; j < this.blocks[i].length; j++) {
                if (this.blocks[i][j] != 0 && !this.blockIsCorrect(i, j)) {
                    BlockIndex index = this.getCorrectPositionGivenValue(this.blocks[i][j]);
                    int manhattanI = Math.abs(i - index.i);
                    int manhattanJ = Math.abs(j - index.j);
                    int thisManhattan = manhattanI + manhattanJ;
                    manhattan += thisManhattan;
                }
            }
        }
        return manhattan;
    }

    public boolean isGoal() {
        return this.manhattan() == 0;
    }

    public Board twin() {
        if (this.twin == null) {
            this.setTwin();
        }
        return this.twin;
    }

    // MARK: - Iterable

    public Iterable<Board> neighbors() {
        Queue<Board> neighbors = new Queue<Board>();
        BlockIndex[] possibleNeighbors = new BlockIndex[4];
        possibleNeighbors[0] = new BlockIndex(this.zeroBlock.i + 1, this.zeroBlock.j);
        possibleNeighbors[1] = new BlockIndex(this.zeroBlock.i, this.zeroBlock.j + 1);
        possibleNeighbors[2] = new BlockIndex(this.zeroBlock.i - 1, this.zeroBlock.j);
        possibleNeighbors[3] = new BlockIndex(this.zeroBlock.i, this.zeroBlock.j - 1);
        for (BlockIndex possibleNeighbor : possibleNeighbors) {
            if ((possibleNeighbor.i >= 0 && possibleNeighbor.i < this.blocks.length)
                && (possibleNeighbor.j >= 0 && possibleNeighbor.j < this.blocks.length)) {
                    BlockIndex indexToSwap = possibleNeighbor;
                    int[][] newBlocks = Board.deepCopy(this.blocks, this.blocks.length);
                    int value1 = newBlocks[this.zeroBlock.i][this.zeroBlock.j];
                    newBlocks[this.zeroBlock.i][this.zeroBlock.j] = newBlocks[indexToSwap.i][indexToSwap.j];
                    newBlocks[indexToSwap.i][indexToSwap.j] = value1;
                    neighbors.enqueue(new Board(newBlocks));
            }
        }
        return neighbors;
    }

    private class BlockIndex {
        int i;
        int j;
        BlockIndex(int i, int j) {
            this.i = i;
            this.j = j;
        }
    }

    // MARK: - Object

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(this.dimension() + "\n");
        for (int i = 0; i < this.blocks.length; i++) {
            for (int j = 0; j < this.blocks[i].length; j++) {
                s.append(String.format("%2d ", this.blocks[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if (that.dimension() != this.dimension()) return false;
        return this.toString().equals(that.toString());
    }

    // MARK: - Test client

    public static void main(String[] args) {
        int[][] blocks = {{2, 3, 4}, {5, 0, 6}, {7, 8, 1}};
        Board board = new Board(blocks);
        Board board2 = new Board(blocks);
        System.out.println(board);
        System.out.println(board.equals(board2));
        System.out.println("hamming: " + board.hamming());
        System.out.println("manhatt: " + board.manhattan());
        System.out.println("is goal: " + board.isGoal());
        System.out.println(board.twin());
        System.out.println("neighbors:");
        for (Board neighbor : board.neighbors()) {
            System.out.println(neighbor);
        }
    }

    // MARK: - Private

    private void setTwin() {
        int[][] newBlocks = Board.deepCopy(this.blocks, this.blocks.length);
        int i1, j1, i2, j2;
        do {
            i1 = StdRandom.uniform(0, this.n);
            j1 = StdRandom.uniform(0, this.n);
            i2 = StdRandom.uniform(0, this.n);
            j2 = StdRandom.uniform(0, this.n);
        } while (newBlocks[i1][j1] == 0
            || newBlocks[i2][j2] == 0
            || newBlocks[i1][j1] == newBlocks[i2][j2]);
        // swap blocks
        int value1 = newBlocks[i1][j1];
        newBlocks[i1][j1] = newBlocks[i2][j2];
        newBlocks[i2][j2] = value1;
        this.twin = new Board(newBlocks);
    }

    private boolean blockIsCorrect(int i, int j) {
        int correctBlockValue;
        if (i == this.n - 1 && j == this.n - 1) {
            correctBlockValue = 0; // last block should be 0
        } else {
            correctBlockValue = (this.n * i) + (j + 1);
        }
        return this.blocks[i][j] == correctBlockValue;
    }

    private BlockIndex getCorrectPositionGivenValue(int value) {
        int i;
        int j;
        if (value % this.n == 0) {
            i = value/this.n - 1;
            j = this.n - 1;
        } else {
            i = value/this.n;
            j = value - (this.n * i) - 1;
        }
        return new BlockIndex(i, j);
    }

    private static int[][] deepCopy(int[][] oldArray, int size) {
        int[][] newArray = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                newArray[i][j] = oldArray[i][j];
            }
        }
        return newArray;
    }
}
