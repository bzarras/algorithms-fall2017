/**
 * Ben Zarras
 * 10/15/2017
 *
 * Class RandomizedQueue implements the abstract data type Randomized Queue,
 * which has the same API as a queue but removes items randomly rather
 * than in LIFO order.
 *
 * Compile: javac-algs4 RandomizedQueue.java
 */

import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] array = (Item[]) new Object[1];
    private int last = -1;
    private int size = 0;

    public boolean isEmpty() {
        return this.size() == 0;
    }

    public int size() {
        return this.size;
    }

    public void enqueue(Item item) {
        if (item == null) {
            throw new java.lang.IllegalArgumentException("item cant be null");
        }
        if (this.size() == this.array.length) {
            this.resize(this.array.length * 2);
        }
        this.array[++this.last] = item;
        this.size++;
    }

    public Item dequeue() {
        this.validateItemCanBeRemoved();
        if (this.size() <= this.array.length / 4) {
            this.resize(this.array.length / 2);
        }
        this.prepareLastItemForUse();
        Item item = this.array[this.last];
        this.array[this.last--] = null; // prevent loitering
        this.size--;
        return item;
    }

    public Item sample() {
        this.validateItemCanBeRemoved();
        this.prepareLastItemForUse();
        return this.array[this.last];
    }

    // Iterable

    public Iterator<Item> iterator() {
        return new RQIterator(this.array, this.size());
    }

    private class RQIterator implements Iterator<Item> {
        private Item[] array;
        private int current;

        RQIterator(Item[] items, int size) {
            this.array = (Item[]) new Object[size];
            for (int i = 0; i < size; i++) {
                this.array[i] = items[i];
            }
            StdRandom.shuffle(this.array);
        }

        public boolean hasNext() {
            return this.current < this.array.length;
        }

        public Item next() {
            if (this.current >= this.array.length) {
                throw new java.util.NoSuchElementException();
            }
            return this.array[this.current++];
        }

        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }
    }

    // Private

    private void validateItemCanBeRemoved() {
        if (this.isEmpty()) {
            throw new java.util.NoSuchElementException("queue is empty");
        }
    }

    private void resize(int newSize) {
        Item[] newArray = (Item[]) new Object[newSize];
        for (int i = 0; i <= this.last; i++) {
            newArray[i] = this.array[i];
        }
        this.array = newArray;
    }

    private void swapItemsAt(int index1, int index2) {
        Item a = this.array[index1];
        this.array[index1] = this.array[index2];
        this.array[index2] = a;
    }

    private void prepareLastItemForUse() {
        int randIndex = StdRandom.uniform(0, this.size());
        this.swapItemsAt(randIndex, this.last);
    }

    // Test client

    public static void main(String[] args) {
        RandomizedQueue<Integer> rq = new RandomizedQueue<Integer>();
        for (int i = 0; i < 100; i++) {
            rq.enqueue(i);
        }

        for (int i = 0; i < 100; i++) {
            rq.dequeue();
        }

        for (int i = 0; i < 4; i++) {
            rq.enqueue(i);
        }

        for (int i : rq) {
            System.out.println(i);
        }
    }
 }
