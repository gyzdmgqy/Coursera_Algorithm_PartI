import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
    private class Node {
        private final Item item;
        private Node next = null;
        private Node previous = null;

        public Node(Item item) {
            this.item = item;
        }
    }

    private Node first;
    private Node last;
    private int size;

    // construct an empty deque
    public Deque() {
        first = null;
        last = null;
        size = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException();
        Node current = new Node(item);
        if (isEmpty()) {
            first = current;
            last = current;
        }
        else {
            current.next = first;
            first.previous = current;
            first = current;
        }
        size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException();
        Node current = new Node(item);
        if (isEmpty()) {
            first = current;
            last = current;
        }
        else {
            last.next = current;
            current.previous = last;
            last = current;
        }
        size++;

    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) throw new java.util.NoSuchElementException();
        size--;
        Node firstNode = first;
        first = first.next;
        if (isEmpty()) {
            last = null;
        }
        else {
            first.previous = null;
        }
        return firstNode.item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) throw new java.util.NoSuchElementException();
        size--;
        Node lastNode = last;
        last = last.previous;
        if (isEmpty()) {
            first = null;
        }
        else {
            last.next = null;
        }
        return lastNode.item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new java.util.NoSuchElementException();
            Item currentItem = current.item;
            current = current.next;
            return currentItem;
        }
    }


    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();
        deque.addFirst(1);
        deque.addLast(2);
        System.out.println(deque.removeFirst());
        System.out.println(deque.size());
        System.out.println(deque.removeLast());
        System.out.println(deque.isEmpty());
        for (int i = 0; i < 100; i++) {
            deque.addLast(i);
        }
        for (Integer i : deque) {
            System.out.println("Iterator:" + i);
        }

    }

}
