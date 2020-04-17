import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] queue;
    private int size = 0;
    private int capacity = 2;
    private int lastIdx = -1;


    // construct an empty randomized queue
    public RandomizedQueue() {
        queue = (Item[]) new Object[capacity];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    private void resize(int newSize) {
        Item[] newQueue = (Item[]) new Object[newSize];
        for (int i = 0; i < size; ++i) newQueue[i] = queue[i];
        capacity = newSize;
        queue = newQueue;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException();
        if (size + 1 > capacity) {
            resize(2 * size);
        }
        queue[size] = item;
        lastIdx++;
        size++;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new java.util.NoSuchElementException();
        if (size <= capacity / 4) {
            resize(capacity / 2);
        }
        int randomIdx = StdRandom.uniform(size);
        Item returnValue = queue[randomIdx];
        queue[randomIdx] = queue[lastIdx];
        size--;
        lastIdx--;
        return returnValue;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new java.util.NoSuchElementException();
        int randomIdx = StdRandom.uniform(size);
        return queue[randomIdx];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomIterator();
    }

    private class RandomIterator implements Iterator<Item> {
        private int currentIdx = 0;
        private final int[] randomIdx;

        public RandomIterator() {
            randomIdx = new int[size];
            for (int i = 0; i < size; ++i) randomIdx[i] = i;
            StdRandom.shuffle(randomIdx);
        }

        public boolean hasNext() {
            return currentIdx < size;
        }

        public Item next() {
            if (!hasNext()) throw new java.util.NoSuchElementException();
            currentIdx++;
            return queue[randomIdx[currentIdx - 1]];
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> queue = new RandomizedQueue<>();
        for (int i = 0; i < 100; ++i) queue.enqueue(i);
        System.out.println(queue.dequeue());
        System.out.println(queue.size());
        System.out.println(queue.isEmpty());
        System.out.println(queue.sample());
        for (Integer j : queue) System.out.println(j);
    }

}
