/**
 * Ben Zarras
 * 10/26/2017
 *
 * Solver.java
 */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import java.util.Iterator;

public class Solver {
    private final class SearchNode implements Comparable<SearchNode> {
        final Board board;
        final int moves;
        final SearchNode previous;
        private int hamming = -1;
        private int manhattan = -1;
        SearchNode(Board board, int moves, SearchNode previous) {
            this.board = board;
            this.moves = moves;
            this.previous = previous;
        }
        int hammingPriority() {
            if (this.hamming < 0) {
                this.hamming = this.board.hamming() + this.moves;
            }
            return this.hamming;
        }
        int manhattanPriority() {
            if (this.manhattan < 0) {
                this.manhattan = this.board.manhattan() + this.moves;
            }
            return this.manhattan;
        }
        public int compareTo(SearchNode that) {
            int thisManhattan = this.manhattanPriority();
            int thatManhattan = that.manhattanPriority();
            if (thisManhattan == thatManhattan) {
                // break tie
                // int thisHamming = this.hammingPriority();
                // int thatHamming = that.hammingPriority();
                // if (thisHamming == thatHamming) {
                //     return 0;
                // } else if (thisHamming > thatHamming) {
                //     return 1;
                // } else {
                //     return -1;
                // }
                return 0;
            } else if (thisManhattan > thatManhattan) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    private int moves;
    private Board initialBoard;
    private boolean isSolvable;
    private SearchNode goalNode;

    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException("null board");
        this.initialBoard = initial;
        Board twinBoard = this.initialBoard.twin();
        MinPQ<SearchNode> pq = new MinPQ<SearchNode>();
        MinPQ<SearchNode> twinPq = new MinPQ<SearchNode>();
        SearchNode initialNode = new SearchNode(this.initialBoard, 0, null);
        SearchNode twinInitialNode = new SearchNode(twinBoard, 0, null);
        pq.insert(initialNode);
        twinPq.insert(twinInitialNode);
        SearchNode currentNode = initialNode;
        SearchNode currentTwinNode = twinInitialNode;
        int moves = 0;
        int twinMoves = 0;
        while (!currentNode.board.isGoal() && !currentTwinNode.board.isGoal()) {
            currentNode = pq.delMin();
            currentTwinNode = twinPq.delMin();
            moves = currentNode.moves + 1;
            twinMoves = currentTwinNode.moves + 1;
            this.insertNeighbors(currentNode, pq, moves);
            this.insertNeighbors(currentTwinNode, twinPq, twinMoves);
        }
        this.isSolvable = currentNode.board.isGoal();
        if (this.isSolvable) {
            this.goalNode = currentNode;
            this.moves = moves;
        }
    }

    public boolean isSolvable() {
        return this.isSolvable;
    }

    public int moves() {
        if (!this.isSolvable) {
            return -1;
        }
        return this.goalNode.moves;
    }

    public Iterable<Board> solution() {
        if (!this.isSolvable()) return null;
        Stack<Board> boardStack = new Stack<Board>();
        SearchNode node = this.goalNode;
        while (node != null) {
            boardStack.push(node.board);
            node = node.previous;
        }
        return boardStack;
    }

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

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

    private void insertNeighbors(SearchNode node, MinPQ<SearchNode> pq, int moves) {
        Board predecessor = (node.previous != null) ? node.previous.board : null;
        for (Board neighbor : node.board.neighbors()) {
            if (!neighbor.equals(predecessor)) {
                SearchNode newNode = new SearchNode(neighbor, moves, node);
                pq.insert(newNode);
            }
        }
    }
}
