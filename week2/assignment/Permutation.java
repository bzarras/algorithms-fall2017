/**
 * Ben Zarras
 * 10/15/2017
 *
 * A client for Deque and RandomizedQueue
 *
 * Compile: javac-algs4 Permutation.java
 * Run: java-algs4 Permutation <number_to_print>
 */

import edu.princeton.cs.algs4.StdIn;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> rq = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            String s = StdIn.readString();
            rq.enqueue(s);
        }
        for (int i = 0; i < k; i++) {
            System.out.println(rq.dequeue());
        }
    }
}
