/*************************************************************************
  * Names: Peter Grabowski and Rafael Grinberg
  * NetIDs: pgrabows@ and rgrinber@
  * Precepts: P02B and P02
  * 
  * Compilation:  javac Deque.java
  * Execution:    java Deque
  * Dependencies: none
  *
  * A generic deque (a queue from which elements can be added
  * to either the front or back), implemented using a linked list.
  * Each deque element is of type Item.
  * 
  * Code skeleton adapted from Queue.java on Booksite.
  *
  *************************************************************************/

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private int N;         // number of elements on deque
    private Node first;    // beginning of deque
    private Node last;     // end of deque
    
    // node class for the linked list
    private class Node {
        private Item item;
        private Node next;
        private Node prev;
    }
    
    // construct an empty deque
    public Deque() {
        first = null;
        last  = null;
        N = 0;
    }
    
    // is the deque empty?
    public boolean isEmpty() {
        return first == null;
    }
    
    // return the number of items on the deque
    public int size() {
        return N;     
    }
    
    // insert the item at the front of the deque
    public void addFirst(Item item) {
        Node front = new Node();
        front.item = item;
        front.prev = null;
        
        if (this.isEmpty()) {
            front.next = null;
            first = front;
            last = front;
        }
        else {
            front.next = first;
            first.prev = front;
            first = front;
        }
        N++;
    }
    
    // inser the item at the end of the deque
    public void addLast(Item item) {
        Node end = new Node();
        end.item = item;
        
        if (this.isEmpty()) {
            end.next = null;
            end.prev = null;
            first = end;
            last = end;
        }
        else {
            last.next = end;
            end.next = null;
            end.prev = last;
            last = end;
        }
        N++;
    }
    
    // delete and return the first item in the deque
    public Item removeFirst() {
        
        if (this.isEmpty())
            throw new RuntimeException("Deque underflow");
        
        Item item = first.item;
        first = first.next;
        if (first != null)
            first.prev = null;
        N--;
        if (N == 0)
            last = null;
        return item;
        
    }
    
    // delete and return the last item in the deque
    public Item removeLast() {
        
        if (this.isEmpty())
            throw new RuntimeException("Deque underflow");
        
        Node newlast = last.prev;
        
        Item item = last.item;
        last.prev = null; // might be helpful for garbage collector
        last = newlast;
        if (last != null)
            last.next = null;
        N--;
        if (N == 0)
            first = null;
        return item;
    }
    
    // teturn an iterator that iterates over the items on the deque from front to back
    public Iterator<Item> iterator()  {
        return new FtBIterator();  
    }
    
    // an iterator, doesn't implement remove() since it's optional
    // (copied form lecture slides)
    private class FtBIterator implements Iterator<Item> {
        private Node current = first;
        
        public boolean hasNext()  { return current != null;                     }
        public void remove()      { throw new UnsupportedOperationException();  }
        
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next; 
            return item;
        }
    }
    
    // private method for debugging
    private void check() {
        System.out.println("Check: ");
        if (first != null)
            System.out.println(this.first.item);
        if (last != null)
            System.out.println(this.last.item);
        System.out.println(this.N);
        System.out.println();
    }
    
    // main method for testing the deque object
    public static void main(String[] args) {
        
        // test a deque of strings
        Deque<String> test = new Deque<String>();
        System.out.println("Empty? " + test.isEmpty());
        System.out.println("Size: " + test.size());
        
        test.addFirst("FirstFirst");
        System.out.println(test.removeFirst());
        test.addFirst("FirstLast");
        System.out.println(test.removeLast());
        test.addLast("LastFirst");
        System.out.println(test.removeFirst());
        test.addLast("LastLast");
        System.out.println(test.removeLast());
        
        test.addLast("you");
        test.addFirst("Hello");
        test.addLast("and you!");
        test.addLast("to");
        System.out.println();
        System.out.println("Empty? " + test.isEmpty());
        System.out.println("Size: " + test.size());
        System.out.println(test.removeFirst());
        System.out.println(test.removeLast());
        System.out.println(test.removeFirst());
        System.out.println(test.removeLast());
        System.out.println();
        
        // test a deque of ints
        // (code adapted from Queue.java on booksite)        
        Deque<Integer> test2 = new Deque<Integer>();
        for (int i = 0; i < 10; i++)
            test2.addLast(i);
        
        // test out iterator
        for (int i : test2)
            System.out.println(i);
        
        }
}
