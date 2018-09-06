/**
 * Ben Zarras
 * 10/15/2017
 *
 * Class Deque implements the abstract data type Deque,
 * which can be used as both a stack and a queue.
 *
 * Compile: javac-algs4 Deque.java
 */

import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
    private class Node {
        private Item item;
        private Node next;
        private Node prev;
    }

    private Node first, last;
    private int size;

    public Deque() {
        this.first = null;
        this.last = null;
        this.size = 0;
    }

    public boolean isEmpty() {
        return this.first == null || this.last == null;
    }

    public int size() {
        return this.size;
    }

    public void addFirst(Item item) {
        this.validateItem(item);
        Node newFirst = new Node();
        newFirst.item = item;
        if (this.isEmpty()) {
            this.first = newFirst;
            this.last = this.first;
        } else {
            Node oldFirst = this.first;
            newFirst.next = oldFirst;
            oldFirst.prev = newFirst;
            this.first = newFirst;
        }
        this.size++;
    }

    public void addLast(Item item) {
        this.validateItem(item);
        Node newLast = new Node();
        newLast.item = item;
        if (this.isEmpty()) {
            this.last = newLast;
            this.first = this.last;
        } else {
            this.last.next = newLast;
            newLast.prev = this.last;
            this.last = newLast;
        }
        this.size++;
    }

    public Item removeFirst() {
        this.validateItemToReturn();
        Node f = this.first;
        Item item = f.item;
        this.first = f.next;
        this.size--;
        return item;
    }

    public Item removeLast() {
        this.validateItemToReturn();
        Node l = this.last;
        Item item = l.item;
        this.last = l.prev;
        this.size--;
        return item;
    }

    // Iterable

    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node currentNode = first;
        public boolean hasNext() {
            return this.currentNode != null;
        }

        public Item next() {
            if (this.currentNode == null) {
                throw new java.util.NoSuchElementException();
            }
            Item item = this.currentNode.item;
            currentNode = this.currentNode.next;
            return item;
        }

        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }
    }

    // Test clients

    public static void main(String[] args) {
        Deque<Integer> deck = new Deque<Integer>();
        for (int i = 0; i < 100; i++) {
            deck.addFirst(i);
        }
        for (int i : deck) {
            System.out.println(i);
        }
    }

    // Private

    private void validateItem(Item item) {
        if (item == null) {
            throw new java.lang.IllegalArgumentException("cannot insert null");
        }
    }

    private void validateItemToReturn() {
        if (this.isEmpty()) {
            throw new java.util.NoSuchElementException("deque is empty");
        }
    }
}
